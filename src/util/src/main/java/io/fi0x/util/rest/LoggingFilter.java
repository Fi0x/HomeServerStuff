package io.fi0x.util.rest;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Order(1)
@Component
public class LoggingFilter implements Filter
{
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		log.trace("Uri called: '{}'", httpRequest.getRequestURI());

		filterChain.doFilter(servletRequest, servletResponse);
	}
}