package com.ms_ma_backend_test.ms_ma.service;

import com.ms_ma_backend_test.ms_ma.dtos.UserDto;
import com.ms_ma_backend_test.ms_ma.entity.FriendShip;
import com.ms_ma_backend_test.ms_ma.entity.User;
import com.ms_ma_backend_test.ms_ma.repository.FriendShipRepository;
import com.ms_ma_backend_test.ms_ma.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendShipServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    FriendShipRepository friendShipRepository;

    @Mock
    JwtAuthenticationToken token;

    @Spy
    @InjectMocks
    FriendShipService friendShipService;

    private User user;
    private FriendShip acceptedFriendship;
    private FriendShip pendingFriendship;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        token = Mockito.mock(JwtAuthenticationToken.class);

        user = new User();
        user.setId(1L);

        User friend1 = new User();
        friend1.setId(2L);

        User friend2 = new User();
        friend2.setId(3L);

        acceptedFriendship = new FriendShip();
        acceptedFriendship.setRequester(friend1);
        acceptedFriendship.setFriend(user);
        acceptedFriendship.setStatus(FriendShip.FriendshipStatus.ACCEPTED);

        pendingFriendship = new FriendShip();
        pendingFriendship.setRequester(friend2);
        pendingFriendship.setFriend(user);
        pendingFriendship.setStatus(FriendShip.FriendshipStatus.PENDING);

        user.setReceivedFriendships(Arrays.asList(acceptedFriendship, pendingFriendship));
    }

    @Test
    @DisplayName("Should save friendship request successfully")
    void requestFriendShip_Case1() {

        Long requesterId = 1L;
        Long friendId = 2L;

        var requester = new User();
        requester.setId(requesterId);

        var friend = new User();
        friend.setId(friendId);

        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        when(token.getName())
                .thenReturn(requesterId.toString());

        when(userRepository.findById(requesterId))
                .thenReturn(Optional.of(requester));

        when(userRepository.findById(friendId))
                .thenReturn(Optional.of(friend));

        friendShipService.requestFriendShip(friendId, token);

        ArgumentCaptor<FriendShip> friendShipCaptor = ArgumentCaptor.forClass(FriendShip.class);
        verify(friendShipRepository).save(friendShipCaptor.capture());

        FriendShip capturedFriendShip = friendShipCaptor.getValue();

        assertEquals(requester, capturedFriendShip.getRequester());
        assertEquals(friend, capturedFriendShip.getFriend());
        assertEquals(FriendShip.FriendshipStatus.PENDING, capturedFriendShip.getStatus());

        verify(friendShipRepository).save(capturedFriendShip);

    }

    @Test
    @DisplayName("Should throw exception when requester sends friendship to themselves")
    void requestFriendShip_Case2() {

        Long requesterId = 1L;
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        when(token.getName()).thenReturn(requesterId.toString());

        var requester = new User();
        requester.setId(requesterId);

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                friendShipService.requestFriendShip(requesterId, token));

        assertEquals("It is not possible sent a friendship request for yourself", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when requester is not found")
    void requestFriendShip_Case3() {

        Long requesterId = 1L;
        Long friendId = 2L;

        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        when(token.getName()).thenReturn(requesterId.toString());

        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                friendShipService.requestFriendShip(friendId, token));

        assertEquals("Requester not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when friend is not found")
    void requestFriendShip_Case4() {

        Long requesterId = 1L;
        Long friendId = 2L;

        var requester = new User();
        requester.setId(requesterId);

        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        when(token.getName()).thenReturn(requesterId.toString());

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(friendId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                friendShipService.requestFriendShip(friendId, token));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should accept friendship request successfully")
    void acceptFriendShip_Case1() {

        Long requesterId = 1L;
        Long userId= 2L;

        var requester = new User();
        requester.setId(requesterId);

        var user = new User();
        user.setId(userId);

        FriendShip friendShip = new FriendShip(requester, user, FriendShip.FriendshipStatus.PENDING);

        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        when(token.getName())
                .thenReturn(userId.toString());

        when(userRepository.findById(requesterId))
                .thenReturn(Optional.of(requester));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(friendShipRepository.findByRequesterAndFriend(requester, user))
                .thenReturn(Optional.of(friendShip));

        friendShipService.acceptFriendShip(requesterId, token);

        ArgumentCaptor<FriendShip> friendShipCaptor = ArgumentCaptor.forClass(FriendShip.class);
        verify(friendShipRepository).save(friendShipCaptor.capture());

        FriendShip capturedFriendShip = friendShipCaptor.getValue();

        assertEquals(FriendShip.FriendshipStatus.ACCEPTED, capturedFriendShip.getStatus());
        verify(friendShipRepository, times(1)).save(capturedFriendShip);

    }

    @Test
    @DisplayName("Should throw nan exception when the request has already been answered")
    void acceptFriendShip_Cse2() {


        Long requesterId = 1L;
        Long userId = 2L;

        var requester = new User();
        requester.setId(requesterId);

        var user = new User();
        user.setId(userId);

        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        when(token.getName())
                .thenReturn(userId.toString());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userRepository.findById(requesterId))
                .thenReturn(Optional.of(requester));

        FriendShip friendShip = new FriendShip(requester, user, FriendShip.FriendshipStatus.ACCEPTED);

        when(friendShipRepository.findByRequesterAndFriend(requester, user))
                .thenReturn(Optional.of(friendShip));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                friendShipService.acceptFriendShip(requesterId, token));

        assertEquals("This request has already been answered", exception.getMessage());

    }

    @Test
    @DisplayName("Should decline friendship request successfully")
    void declineFriendShip_Case1() {

        Long requesterId = 1L;
        Long userId = 2L;

        var requester = new User();
        requester.setId(requesterId);

        var user = new User();
        user.setId(userId);

        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        when(token.getName())
                .thenReturn(userId.toString());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userRepository.findById(requesterId))
                .thenReturn(Optional.of(requester));

        FriendShip friendShip = new FriendShip(requester, user, FriendShip.FriendshipStatus.PENDING);

        when(friendShipRepository.findByRequesterAndFriend(requester, user))
                .thenReturn(Optional.of(friendShip));

        friendShipService.declineFriendShip(requesterId, token);

        ArgumentCaptor<FriendShip> friendShipArgumentCaptor = ArgumentCaptor.forClass(FriendShip.class);
        verify(friendShipRepository).save(friendShipArgumentCaptor.capture());

        FriendShip capturedFriendShip = friendShipArgumentCaptor.getValue();

        assertEquals(FriendShip.FriendshipStatus.DECLINED, capturedFriendShip.getStatus());

    }

    @Test
    @DisplayName("Should throw exception when the friendsip request does not exist")
    void declineFriendShip_Case2() {

        Long requesterId = 1L;
        Long userId = 2L;

        var requester = new User();
        requester.setId(requesterId);

        var user = new User();
        user.setId(userId);

        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        when(token.getName())
                .thenReturn(userId.toString());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userRepository.findById(requesterId))
                .thenReturn(Optional.of(requester));

        when(friendShipRepository.findByRequesterAndFriend(requester, user))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> friendShipService.declineFriendShip(requesterId, token)
        );

        assertEquals("This friendship request does not exist", exception.getMessage());

    }

    @Test
    @DisplayName("Get user friends when there is no friends")
    void getUserFriends_Case1() {
        user.setReceivedFriendships(new ArrayList<>());
        user.setSentFriendships(new ArrayList<>());

        when(token.getName()).thenReturn("1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<UserDto> friends = friendShipService.getUserFriends(token);

        assertTrue(friends.isEmpty());
    }

    @Test
    @DisplayName("Get user friends when there is a friendship accepted")
    void getUserFriends_Case2() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(userRepository.findById(2L)).thenReturn(Optional.of(acceptedFriendship.getRequester()));

        when(token.getName()).thenReturn("1");


        UserDto friendDto = new UserDto();
        doReturn(friendDto).when(friendShipService).convertUserToDto(acceptedFriendship.getRequester());

        List<UserDto> friends = friendShipService.getUserFriends(token);

        assertEquals(1, friends.size());
        assertEquals(friendDto, friends.getFirst());
    }

    @Test
    @DisplayName("Get user friends when there is mixed friendship")
    void getUserFriends_Case3() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(userRepository.findById(2L)).thenReturn(Optional.of(acceptedFriendship.getRequester()));

        when(token.getName()).thenReturn("1");

        UserDto friendDto = new UserDto();
        when(friendShipService.convertUserToDto(acceptedFriendship.getRequester())).thenReturn(friendDto);

        List<UserDto> friends = friendShipService.getUserFriends(token);

        assertEquals(1, friends.size());
        assertEquals(friendDto, friends.getFirst());
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void getUserFriends_Case4() {

        when(token.getName()).thenReturn("1");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> friendShipService.getUserFriends(token));
    }



}
