package com.example.paymentApp.Services;

import com.example.paymentApp.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


}
