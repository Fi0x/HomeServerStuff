package io.fi0x.util.rest;


import io.fi0x.util.components.Authenticator;
import io.fi0x.util.dto.LoginDto;
import io.fi0x.util.dto.UserDto;
import io.fi0x.util.service.AuthenticationService;
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

/**
 * This controller provides endpoints for registering and login of users.
 */
@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"registerError", "redirect", "userDto", "username"})
public class UserController
{
	private AuthenticationService authenticationService;

	private Authenticator authenticator;

	/**
	 * This method shows a login-page to log in as an existing user.
	 *
	 * @param model The {@link ModelMap} that will be filled with data, that is required for the jsp-file.
	 * @return The jsp-file that shows the login.
	 */
	@GetMapping("/custom-login")
	public String showLogin(ModelMap model)
	{
		log.info("showLogin() called");

		model.put("username", authenticator.getAuthenticatedUsername());
		model.put("loginDto", new LoginDto());

		return "login";
	}

	/**
	 * This method shows a register page for new users to create a new account.
	 *
	 * @param model   The model to use by the jsp-file.
	 * @param request The request in which the redirect attribute will be set for correct navigation after registering.
	 * @return The jsp-file that will show the register-page.
	 */
	@GetMapping("/register")
	public String showRegister(ModelMap model, HttpServletRequest request)
	{
		log.info("showRegister() called");

		model.put("userDto", new UserDto());
		request.getSession().setAttribute("redirect", "redirect:register");

		return "signup";
	}

	/**
	 * This method registers a user if it is not yet registered.
	 *
	 * @param model   The model, that will be used to store information for the jsp-file.
	 * @param request The original request where the redirect attribute will get removed.
	 * @param userDto The dto with the user-data of the new user, that should get registered.
	 * @return A redirect to the main-page.
	 */
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
