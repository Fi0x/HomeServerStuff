package io.fi0x.homeserver.general.service;


import io.fi0x.homeserver.general.logic.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService
{
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserDto userDto) throws DuplicateKeyException
    {
        log.trace("registerUser() called with userDto={}", userDto);
        if(userDetailsManager.userExists(userDto.getUsername()))
            throw new DuplicateKeyException("A user with that username already exists");

        userDetailsManager.createUser(User.builder()
                .passwordEncoder(passwordEncoder::encode)
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .roles("USER").build());
    }
}
