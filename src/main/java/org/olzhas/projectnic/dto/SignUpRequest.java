package org.olzhas.projectnic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.olzhas.projectnic.entity.Role;

/**
 * DTO for {@link org.olzhas.projectnic.entity.Users}
 */

@Data
public class SignUpRequest {
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    @NotBlank(message = "Username cannot be blank")
    String username;

    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @NotBlank(message = "First name cannot be blank")
    String firstName;

    @Size(max = 255, message = "Last name must be no longer than 255 characters")
    @NotBlank(message = "Last name cannot be blank")
    String lastName;

    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    String email;

    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    @NotBlank(message = "Password cannot be blank")
    String password;

    Role role;
}
