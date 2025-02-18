package com.studio.trackflicks.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationRequest {

    @NotBlank(message = "{verification.code.empty}")
    private String verificationCode;

    @NotBlank(message = "{verification.userId.empty}")
    private Long userId;

}
