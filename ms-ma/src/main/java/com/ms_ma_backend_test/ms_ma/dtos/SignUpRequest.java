package com.ms_ma_backend_test.ms_ma.dtos;

import com.ms_ma_backend_test.ms_ma.entity.User;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
        String username,
        User.ProfileVisibility profileVisibility,
        @Pattern(regexp = "^[a-zA-Z0-9]{8,12}$", message = "Password must be 8-12 alphanumeric characters.")
        String password
) {
}
