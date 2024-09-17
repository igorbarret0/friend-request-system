package com.ms_ma_backend_test.ms_ma.controller;

import com.ms_ma_backend_test.ms_ma.dtos.UserDto;
import com.ms_ma_backend_test.ms_ma.service.FriendShipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
                                                    JwtAuthenticationToken token) {

        friendShipService.requestFriendShip(requesterId, token);
        return ResponseEntity.ok("Your request was sent successfully");
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendShip(@RequestParam Long requesterId,
                                                   JwtAuthenticationToken token) {

        friendShipService.acceptFriendShip(requesterId, token);
        return ResponseEntity.ok("Request accepted with success");
    }

    @PostMapping("/decline")
    public ResponseEntity<String> declineFriendShip(@RequestParam Long requesterId,
                                                    JwtAuthenticationToken token) {

        friendShipService.declineFriendShip(requesterId, token);
        return ResponseEntity.ok("Request declined with success");
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUserFriends(JwtAuthenticationToken token) {

        var response = friendShipService.getUserFriends(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
