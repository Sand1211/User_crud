package com.core.myapp.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.core.myapp.common.Utils;
import com.core.myapp.repo.UserRepository;
import com.core.myapp.repo.entity.UserDetail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UserDetail findByEmail = userRepository.findByEmail(email);
		if (Utils.isNull(findByEmail)) {
			throw new UsernameNotFoundException("User not found");
		}
		return new CustomUserDetails(findByEmail);

	}

}
