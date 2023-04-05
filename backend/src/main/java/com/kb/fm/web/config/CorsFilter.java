package com.kb.fm.web.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CorsFilter extends OncePerRequestFilter {
	
	@Value("${app.allowedOrigins}")
	private String allowedOrigins;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest req, @NonNull HttpServletResponse resp,
									@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		setDefaultHeaders(resp);
		if(HttpMethod.OPTIONS.name().equalsIgnoreCase(req.getMethod())) {
			resp.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}
		filterChain.doFilter(req, resp);
	}
	
	private void setDefaultHeaders(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", allowedOrigins);
		response.setHeader("Access-Control-Allow-Headers", "content-type");
	}
	
	
	
	
}