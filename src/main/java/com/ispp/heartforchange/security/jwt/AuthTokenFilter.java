package com.ispp.heartforchange.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ispp.heartforchange.security.service.AccountDetailsServiceImpl;

public class AuthTokenFilter extends OncePerRequestFilter{
	
	  @Autowired
	  private JwtUtils jwtUtils;

	  @Autowired
	  private AccountDetailsServiceImpl accountDetailsServiceImpl;

	  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	  @Override
	  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	      throws ServletException, IOException {
		  if(request.getServletPath().equals("/api/accounts/signin") || request.getServletPath().equals("/api/accounts/refresh")){  
			  filterChain.doFilter(request, response);     
		  } else {
			  try {
			      String jwt = parseJwt(request);
			      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
			        String username = jwtUtils.getUserNameFromJwtToken(jwt);
			        UserDetails userDetails = null;
			        try {
			            userDetails = accountDetailsServiceImpl.loadUserByUsername(username);
			        } catch (UsernameNotFoundException e) {
			           logger.error("User not found with username: {}", username);
			        }
			        if (userDetails != null) {
			            UsernamePasswordAuthenticationToken authentication =
			                new UsernamePasswordAuthenticationToken(
			                    userDetails,
			                    null,
			                    userDetails.getAuthorities());
			            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			            SecurityContextHolder.getContext().setAuthentication(authentication);
			        }
			      }
			    } catch (Exception e) {
			      logger.error("Cannot set user authentication: {}", e);
			    }
			  filterChain.doFilter(request, response);
		  }
	  }

	  private String parseJwt(HttpServletRequest request) {
	    String headerAuth = request.getHeader("Authorization");

	    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
	      return headerAuth.substring(7, headerAuth.length());
	    }

	    return null;
	  }
}
