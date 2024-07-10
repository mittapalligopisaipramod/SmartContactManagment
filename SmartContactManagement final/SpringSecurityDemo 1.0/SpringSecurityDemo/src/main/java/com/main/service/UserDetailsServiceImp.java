package com.main.service;

import com.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class UserDetailsServiceImp implements UserDetailsService {
	
	private final UserRepository repository;
	
	
	@Autowired
	public UserDetailsServiceImp (UserRepository repository ) {
		this.repository = repository;
	}
	
	@Override
	public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
		if(repository.existsByUsername (username)) {
			return repository.findByUsername (username).get ();//.orElseThrow (() -> new UsernameNotFoundException ("user is not found"));
		}
		System.out.println ("user is not found");
	 	throw new UsernameNotFoundException ("user is not found");
	}
}
