package io.fi0x.homeserver.general.rest;

import io.fi0x.homeserver.general.components.Authenticator;
import io.fi0x.homeserver.general.logic.dto.LoginDto;
import io.fi0x.homeserver.general.logic.dto.UserDto;
import io.fi0x.homeserver.general.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"registerError", "redirect", "userDto", "username"})
public class UserController
{
    private AuthenticationService authenticationService;

	private Authenticator authenticator;

    @GetMapping("/custom-login")
    public String showLogin(ModelMap model)
    {
        log.info("showLogin() called");

		model.put("username", authenticator.getAuthenticatedUsername());
        model.put("loginDto", new LoginDto());

        return "login";
    }

    @GetMapping("/register")
    public String showRegister(ModelMap model, HttpServletRequest request)
    {
        log.info("showRegister() called");

        model.put("userDto", new UserDto());
        request.getSession().setAttribute("redirect", "redirect:register");

        return "signup";
    }

    @Transactional
    @PostMapping("/register")
    public String registerUser(ModelMap model, HttpServletRequest request, @Valid UserDto userDto)
    {
        log.info("registerUser() called with userDto={}", userDto);

        try
        {
            authenticationService.registerUser(userDto);
        } catch (DuplicateKeyException e)
        {
            model.put("userDto", userDto);
            model.put("registerError", "A user with this name already exists.");
            return "redirect:register";
        }

        model.remove("registerError");
        request.getSession().removeAttribute("redirect");

        return "redirect:/";
    }
}
