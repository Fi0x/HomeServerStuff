package io.fi0x.util.rest;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"username"})
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController
{
	@GetMapping("/error")
	public String showError(ModelMap model, HttpServletRequest request)
	{
		log.info("showError() called");

		model.put("errorCode", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
		model.put("errorMessage", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

		return "error";
	}

	//TODO: Find out, if these controllers and filters work in other modules as well. Otherwise might need to build
	// this as a war again
	@PostMapping("/error")
	public String showPostError(ModelMap model, HttpServletRequest request)
	{
		log.info("showPostError() called");

		model.put("errorCode", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
		model.put("errorMessage", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

		return "error";
	}
}
