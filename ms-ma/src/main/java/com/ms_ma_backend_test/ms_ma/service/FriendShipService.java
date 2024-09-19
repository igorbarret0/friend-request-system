package com.ms_ma_backend_test.ms_ma.service;

import com.ms_ma_backend_test.ms_ma.dtos.UserDto;
import com.ms_ma_backend_test.ms_ma.entity.FriendShip;
import com.ms_ma_backend_test.ms_ma.entity.User;
import com.ms_ma_backend_test.ms_ma.exceptions.InviteYourselfException;
import com.ms_ma_backend_test.ms_ma.exceptions.RequestAlreadyBeenAnsweredException;
import com.ms_ma_backend_test.ms_ma.exceptions.RequestUnexistException;
import com.ms_ma_backend_test.ms_ma.exceptions.UserNotFoundException;
import com.ms_ma_backend_test.ms_ma.repository.FriendShipRepository;
import com.ms_ma_backend_test.ms_ma.repository.UserRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendShipService {

    private UserRepository userRepository;
    private FriendShipRepository friendShipRepository;

    public FriendShipService(UserRepository userRepository, FriendShipRepository friendShipRepository) {
        this.userRepository = userRepository;
        this.friendShipRepository = friendShipRepository;
    }

    public void requestFriendShip(Long friendId, JwtAuthenticationToken token) {


        var requesterId = Long.valueOf(token.getName());
        var requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new UserNotFoundException("Requester not found"));

        var friend = userRepository.findById(friendId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (requesterId.equals(friendId)) {
            throw new InviteYourselfException();
        }

        FriendShip friendShip = new FriendShip();
        friendShip.setRequester(requester);
        friendShip.setFriend(friend);
        friendShip.setStatus(FriendShip.FriendshipStatus.PENDING);
        friendShipRepository.save(friendShip);

    }

    public void acceptFriendShip(Long requesterId, JwtAuthenticationToken token) {

        var requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new UserNotFoundException("Requester not found"));

        var userId = Long.valueOf(token.getName());
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        var friendShip = friendShipRepository.findByRequesterAndFriend(requester, user)
                .orElseThrow(() -> new RequestUnexistException("This friendship request does not exist"));

        if (friendShip.getStatus() != FriendShip.FriendshipStatus.PENDING) {
            throw new RequestAlreadyBeenAnsweredException();
        }

        friendShip.setStatus(FriendShip.FriendshipStatus.ACCEPTED);
        friendShipRepository.save(friendShip);
    }

    public void declineFriendShip(Long requesterId, JwtAuthenticationToken token) {

        var requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new UserNotFoundException("Requester not found"));

        var userId = Long.valueOf(token.getName());
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        var friendShip = friendShipRepository.findByRequesterAndFriend(requester, user)
                .orElseThrow(() -> new RequestUnexistException("This friendship request does not exist"));

        if (friendShip.getStatus() != FriendShip.FriendshipStatus.PENDING) {
            throw new RequestAlreadyBeenAnsweredException();
        }

        friendShip.setStatus(FriendShip.FriendshipStatus.DECLINED);
        friendShipRepository.save(friendShip);

    }

    public List<UserDto> getUserFriends(JwtAuthenticationToken token) {

        var userId = Long.valueOf(token.getName());
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Set<Long> friendIds = new HashSet<>();

        user.getReceivedFriendships().forEach(friendShip -> {
            if (friendShip.getStatus().equals(FriendShip.FriendshipStatus.ACCEPTED)) {
                friendIds.add(friendShip.getRequester().getId());
            }
        });

        user.getSentFriendships().forEach(friendShip -> {
            if (friendShip.getStatus().equals(FriendShip.FriendshipStatus.ACCEPTED)) {
                friendIds.add(friendShip.getFriend().getId());
            }
        });

        List<UserDto> allFriends = friendIds.stream()
                .map(id -> userRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .map(this::convertUserToDto)
                .collect(Collectors.toList());

        return allFriends;
    }

    UserDto convertUserToDto(User user) {

        UserDto userDTO = new UserDto();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setSignUpDate(user.getSignUpDate());
        userDTO.setProfileVisibility(user.getProfileVisibility());
        userDTO.setBadge(user.getBadge());
        return userDTO;
    }


}
