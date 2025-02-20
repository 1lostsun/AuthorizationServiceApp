package com.example.AuthorizationServiceApp.Filters;

import com.example.AuthorizationServiceApp.Services.JWT.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;

	public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtService jwtService) {
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
	}


	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}

		String authToken = authHeader.substring(7);

			if (jwtService.validateToken(authToken)) {

				String usernameFromToken = jwtService.extractUsernameFromToken(authToken);
				UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);

				UsernamePasswordAuthenticationToken
						authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				authentication
						.setDetails(new WebAuthenticationDetailsSource()
								.buildDetails(request));

				SecurityContextHolder
						.getContext()
						.setAuthentication(authentication);
			}
		chain.doFilter(request, response);
	}
}
