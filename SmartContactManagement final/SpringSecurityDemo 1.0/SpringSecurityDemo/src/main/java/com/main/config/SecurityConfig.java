package com.main.config;

import com.main.filter.JwtAuthenticationFilter;
import com.main.service.UserDetailsServiceImp;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UserDetailsServiceImp userDetailsServiceImp;
	
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsServiceImp userDetailsServiceImp  ) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.userDetailsServiceImp = userDetailsServiceImp;
		
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				
				.authorizeHttpRequests(req -> req
						.requestMatchers("/mail/sendEmail", "/mail/resendOtpAgain", "/mail/OtpVerification").permitAll()
						.requestMatchers("/image.png").permitAll()
						.requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()  // Allow access to all static resources
						.requestMatchers ("/home","/index.html").permitAll()
						.requestMatchers ("/user/changePassword/sendOtp/toMail", "/user/changePassword/verifyOtp/viaMail", "/user/changePassword/viaMail").permitAll()
						.requestMatchers("/user/toRegister", "/user/userLogin").permitAll()
						.requestMatchers("/contact/addContact", "/contact/deleteContact/", "/contact/updateContactNumber/", "/contact/getAllContacts").authenticated()
						.requestMatchers("/admin/**").hasAuthority("ADMIN")
				)
				.userDetailsService(userDetailsServiceImp)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder ();
	}
}

