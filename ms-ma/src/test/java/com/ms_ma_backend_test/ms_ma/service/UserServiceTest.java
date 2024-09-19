package com.ms_ma_backend_test.ms_ma.service;

import com.ms_ma_backend_test.ms_ma.dtos.SignUpRequest;
import com.ms_ma_backend_test.ms_ma.entity.User;
import com.ms_ma_backend_test.ms_ma.exceptions.UserAlreadyExistsException;
import com.ms_ma_backend_test.ms_ma.factory.Factory;
import com.ms_ma_backend_test.ms_ma.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given a signup request object should save object successfully")
    void signUp_Case1() {

        var request = Factory.buildSignUpRequestValid();
        String encodedPassword = "encodedPassword";

        when(userRepository.findByUsername(request.username()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.password()))
                .thenReturn(encodedPassword);

        userService.signUp(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(request.username(), capturedUser.getUsername());
        assertEquals(request.profileVisibility(), capturedUser.getProfileVisibility());
        assertEquals(encodedPassword, capturedUser.getPassword());

        verify(userRepository, times(1)).save(capturedUser);
    }

    @Test
    @DisplayName("Should throw exception when tryna signup a user who is already registered")
    void signUp_Case2() {

        SignUpRequest request = new SignUpRequest("existingUser", User.ProfileVisibility.PUBLIC, "password123");
        User existingUser = new User();
        existingUser.setUsername(request.username());

        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(existingUser));

        // Testar que a exceção é lançada
        assertThrows(UserAlreadyExistsException.class, () -> userService.signUp(request));

        // Verificar que o repositório foi chamado para encontrar o usuário
        verify(userRepository).findByUsername(request.username());
    }

}
