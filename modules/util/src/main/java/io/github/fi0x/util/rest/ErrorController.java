package io.github.fi0x.util.rest;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This controller provides endpoints for POST and GET errors
 */
@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"username"})
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController
{
	/**
	 * Endpoint for error handling. Accepts a {@link ModelMap}, where it puts an {@code errorCode} and {@code
	 * errorMessage} key that can be used in the returned jsp-file. The values for these keys will be obtained from
	 * the attributes in the  provided {@link HttpServletRequest}.
	 *
	 * @param model   The {@link ModelMap} where the keys will be filled with error information.
	 * @param request The request that contains the error in its attributes.
	 * @return The jsp-file that will display the error.
	 */
	@GetMapping("/error")
	public String showError(ModelMap model, HttpServletRequest request)
	{
		log.info("showError() called");

		model.put("errorCode", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
		model.put("errorMessage", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

		return "error";
	}

	/**
	 * This is basically the same method as the one for the {@code GetMaping}, but for POST-requests.
	 *
	 * @param model   The {@link ModelMap} where the keys will be filled with error information.
	 * @param request The request that contains the error in its attributes.
	 * @return The jsp-file that will display the error.
	 */
	@PostMapping("/error")
	public String showPostError(ModelMap model, HttpServletRequest request)
	{
		log.info("showPostError() called");

		model.put("errorCode", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
		model.put("errorMessage", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

		return "error";
	}

	/**
	 * This is basically the same method as the one for the {@code GetMaping}, but for DELETE-requests.
	 *
	 * @param model   The {@link ModelMap} where the keys will be filled with error information.
	 * @param request The request that contains the error in its attributes.
	 * @return The jsp-file that will display the error.
	 */
	@DeleteMapping("/error")
	public String showDeleteError(ModelMap model, HttpServletRequest request)
	{
		log.info("showPostError() called");

		model.put("errorCode", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
		model.put("errorMessage", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

		return "error";
	}
}
