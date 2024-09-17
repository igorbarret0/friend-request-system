package com.ms_ma_backend_test.ms_ma.service;

import com.ms_ma_backend_test.ms_ma.dtos.SignUpRequest;
import com.ms_ma_backend_test.ms_ma.entity.User;
import com.ms_ma_backend_test.ms_ma.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(SignUpRequest request) {

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setProfileVisibility(request.profileVisibility());
        newUser.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(newUser);
    }

}
