package com.dvaults.recipecatalogue.security.authentication.basic;

import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.auth.repositories.UserRepository;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.BasicAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public User loadUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException(
                AuthenticationErrorDictionary.USERNAME_NOT_FOUND_001.getDetails().toString(),
                new BasicAuthenticationException(AuthenticationErrorDictionary.USERNAME_NOT_FOUND_001))
        );
  }

}
