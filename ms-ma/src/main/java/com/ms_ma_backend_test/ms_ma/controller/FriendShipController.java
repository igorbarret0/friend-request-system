package com.ms_ma_backend_test.ms_ma.controller;

import com.ms_ma_backend_test.ms_ma.dtos.UserDto;
import com.ms_ma_backend_test.ms_ma.service.FriendShipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendship")
public class FriendShipController {

    private FriendShipService friendShipService;

    public FriendShipController(FriendShipService friendShipService) {
        this.friendShipService = friendShipService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestFriendShip(@RequestParam Long requesterId,
                                                    @RequestParam Long friendId) {

        friendShipService.requestFriendShip(requesterId, friendId);
        return ResponseEntity.ok("Your request was sent successfully");
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendShip(@RequestParam Long friendId,
                                                   @RequestParam Long requesterId) {

        friendShipService.acceptFriendShip(friendId, requesterId);
        return ResponseEntity.ok("Request accepted with success");
    }

    @PostMapping("/decline")
    public ResponseEntity<String> declineFriendShip(@RequestParam Long friendId,
                                                   @RequestParam Long requesterId) {

        friendShipService.declineFriendShip(friendId, requesterId);
        return ResponseEntity.ok("Request declined with success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<UserDto>> getUserFriends(@PathVariable(value = "id") Long userId) {

        var response = friendShipService.getUserFriends(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
