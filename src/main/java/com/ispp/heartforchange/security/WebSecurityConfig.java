package com.ispp.heartforchange.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ispp.heartforchange.security.jwt.AuthEntryPointJwt;
import com.ispp.heartforchange.security.jwt.AuthTokenFilter;
import com.ispp.heartforchange.security.service.AccountDetailsServiceImpl;

@Configuration
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
	@Autowired
	AccountDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/accounts/signin/**").permitAll()
				.antMatchers("/ongs/signup/**").permitAll()
				.antMatchers("/ongs/**").permitAll()
				.antMatchers("/volunteer/signup/**").hasAnyAuthority("ONG")
				.antMatchers("/beneficiaries/signup").hasAnyAuthority("ONG")
				.antMatchers("/task/**").hasAnyAuthority("ONG")
				.antMatchers("/attendance/**").permitAll()
				//.antMatchers("/attendance/ong/**", "/attendance/accept/**", "/attendance/deny/**", "/attendance/confirm/**", "/attendance/add/**", "/attendance/quit/**").hasAnyAuthority("ONG")
				//.antMatchers("/attendance/volunteer/**", "/attendance/task/new/**", "/attendance/task/delete/**", "/attendance/confirm/**", "/attendance/add/**", "/attendance/quit/**").hasAnyAuthority("VOLUNTEER")
				.antMatchers("/grants/**").hasAnyAuthority("ONG")
				.anyRequest().authenticated();

		http.authenticationProvider(authenticationProvider());

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}