package com.studio.trackflicks.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "{registration.username.empty}")
    private String username;

    @NotBlank(message = "{registration.email.empty}")
    private String email;

    @NotBlank(message = "{registration.password.empty}")
    private String password;

}
