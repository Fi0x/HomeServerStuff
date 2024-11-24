package io.github.fi0x.util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This dto is used to log in existing users.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto
{
    private String username;
    private String password;
}
