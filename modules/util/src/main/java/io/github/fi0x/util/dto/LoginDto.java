package io.github.fi0x.util.dto;

import lombok.Data;

/**
 * This dto is used to log in existing users.
 */
@Data
public class LoginDto
{
    private String username;
    private String password;
}
