package org.olzhas.projectnic.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class SignInRequest {
    @Email(message = "email is not")
    private String email;
    private String password;
}
