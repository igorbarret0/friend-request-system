package com.ms_ma_backend_test.ms_ma.service;

import com.ms_ma_backend_test.ms_ma.dtos.UserDto;
import com.ms_ma_backend_test.ms_ma.entity.FriendShip;
import com.ms_ma_backend_test.ms_ma.entity.User;
import com.ms_ma_backend_test.ms_ma.repository.FriendShipRepository;
import com.ms_ma_backend_test.ms_ma.repository.UserRepository;
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

    public void requestFriendShip(Long requesterId, Long friendId) {

        var requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        var friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        if (friend.getProfileVisibility().equals(User.ProfileVisibility.HIDDEN)) {
            throw new RuntimeException("This user cannot receive friendships requests");
        }

        FriendShip friendShip = new FriendShip();
        friendShip.setRequester(requester);
        friendShip.setFriend(friend);
        friendShip.setStatus(FriendShip.FriendshipStatus.PENDING);
        friendShipRepository.save(friendShip);

    }

    public void acceptFriendShip(Long friendId, Long requesterId) {

        var requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        var friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        var friendShip = friendShipRepository.findByRequesterAndFriend(requester, friend)
                .orElseThrow(() -> new RuntimeException("This friendship request does not exist"));

        friendShip.setStatus(FriendShip.FriendshipStatus.ACCEPTED);
        friendShipRepository.save(friendShip);
    }

    public void declineFriendShip(Long friendId, Long requesterId) {

        var requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        var friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        var friendShip = friendShipRepository.findByRequesterAndFriend(requester, friend)
                .orElseThrow(() -> new RuntimeException("This friendship request does not exist"));

        friendShip.setStatus(FriendShip.FriendshipStatus.DECLINED);
        friendShipRepository.save(friendShip);

    }

    public List<UserDto> getUserFriends(Long userId) {

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    private UserDto convertUserToDto(User user) {

        UserDto userDTO = new UserDto();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setSignUpDate(user.getSignUpDate());
        userDTO.setProfileVisibility(user.getProfileVisibility());
        userDTO.setBadge(user.getBadge());
        return userDTO;
    }


}
