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
				.antMatchers("/tasks/volunteer/get/{id}/attendances").hasAnyAuthority("ONG", "VOLUNTEER")
				.antMatchers("/beneficiaries/signup").hasAnyAuthority("ONG")
				.antMatchers("/tasks/ong/**", "/tasks/get/**").hasAnyAuthority("ONG", "VOLUNTEER")
				.antMatchers("/tasks/new/", "/tasks/update/**", "/tasks/delete/**", "/tasks/count").hasAnyAuthority("ONG")
				.antMatchers("/courses/**").hasAnyAuthority("ONG")
				.antMatchers("/workshops/**").hasAnyAuthority("ONG")
				.antMatchers("/activities/new", 
						"/activities/update/**",
						"/activities/delete/**",
						"/activities/get/{id}/attendances/volunteer",
						"/activities/get/{id}/attendances/list",
						"/activities/volunteer/get/{id}/attendances",
						"/activities/get/{id}/petitions").hasAnyAuthority("ONG")
				.antMatchers("/activities/volunteer/get/attendances").hasAnyAuthority("VOLUNTEER")
				.antMatchers("/activities/ong/get/all", "/activities/ong/get/date" ).hasAnyAuthority("ONG", "VOLUNTEER")
				.antMatchers("/attendances/ong/**",
						"/attendances/{id}",
						"/attendances/accept/**",
						"/attendances/deny/**",
						"/attendances/confirm/**",
						"/attendances/add/**",
						"/attendances/quit/**").hasAnyAuthority("ONG")
				.antMatchers("/attendances/volunteer/**", "/attendances/{id}", "/attendances/new/**", "/attendances/cancel/**").hasAnyAuthority("VOLUNTEER")
				.antMatchers("/grants/**").hasAnyAuthority("ONG")
				.antMatchers("/workExperiences/**").authenticated()
				.antMatchers("/appointments/**").hasAnyAuthority("ONG")
				.antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/webjars/**").permitAll()
				.anyRequest().authenticated();
		http.headers().frameOptions().disable();
		http.authenticationProvider(authenticationProvider());

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}