package com.example.domibe.global.security.auth;

import com.example.domibe.domain.user.User;
import com.example.domibe.domain.user.UserRepository;
import com.example.domibe.global.security.exception.UserNofFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String accountId) {
    User user = userRepository.findByAccountId(accountId)
        .orElseThrow(() -> new UserNofFoundException("dsf"));
    return new CustomUserDetails(user);
  }
}
