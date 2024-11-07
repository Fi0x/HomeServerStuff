package io.fi0x.util.dto;

import io.fi0x.util.rest.validation.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatch(message = "Both passwords must match")
public class UserDto
{
    @NotBlank(message = "Username must not be empty")
    @Size(min = 3, message = "Username must have at least 3 characters")
    @Size(max = 50, message = "Username must not be longer than 50 characters")
    private String username;

    @Size(min = 4, message = "Password must have at least 4 characters")
    @Size(max = 72, message = "Password must not be longer than 72 characters")
    private String password;

    @Size(min = 4, message = "Matching password must have at least 4 characters")
    @Size(max = 72, message = "Matching password must not be longer than 72 characters")
    private String matchingPassword;
}
