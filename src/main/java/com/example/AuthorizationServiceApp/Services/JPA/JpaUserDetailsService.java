package com.example.AuthorizationServiceApp.Services.JPA;

import com.example.AuthorizationServiceApp.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public JpaUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return (UserDetails) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
	}
}
