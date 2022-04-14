package com.kb.fm.web.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CorsFilter extends OncePerRequestFilter{
	
	@Value("${app.allowedOrigins}")
	private String allowedOrigins;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		setDefaultHeaders(response);
		if(HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}
		filterChain.doFilter(request, response);
	}
	
	private void setDefaultHeaders(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", allowedOrigins);
		response.setHeader("Access-Control-Allow-Headers", "content-type");
	}
	
	
	
	
}