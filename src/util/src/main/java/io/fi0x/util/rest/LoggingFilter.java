package io.fi0x.util.rest;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class provides filter-options, that will be triggered on each request.
 */
@Slf4j
@Order(1)
@Component
public class LoggingFilter implements Filter
{
	/**
	 * This method is triggered whenever a request is made to the server. It will log the URI of the request, if
	 * trace-logging is enabled.
	 *
	 * @param servletRequest  The request that was made to the server.
	 * @param servletResponse The response, that the server sends to the client.
	 * @param filterChain     The {@link FilterChain} that will be triggered, to continue the request.
	 * @throws IOException      If the {@code filterChain} fails.
	 * @throws ServletException If the {@code filterChain} fails.
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		log.trace("Uri called: '{}'", httpRequest.getRequestURI());

		filterChain.doFilter(servletRequest, servletResponse);
	}
}
