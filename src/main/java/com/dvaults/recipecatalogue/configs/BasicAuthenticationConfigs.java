package com.dvaults.recipecatalogue.configs;

import com.dvaults.recipecatalogue.modules.auth.mappers.AuthenticationMapper;
import com.dvaults.recipecatalogue.modules.auth.repositories.UserRepository;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.UserService;
import com.dvaults.recipecatalogue.security.authentication.basic.SignInBasicAuthenticationProvider;
import com.dvaults.recipecatalogue.security.authentication.basic.SignUpBasicAuthenticationProvider;
import com.dvaults.recipecatalogue.security.authentication.basic.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class BasicAuthenticationConfigs {

  public static final String SIGN_UP_URI = "/auth/basic/sign-up";
  public static final String SIGN_IN_URI = "/auth/basic/sign-in";

  @Bean
  public PasswordEncoder passwordEncoder() {

    String defaultAlgorithm = "bcrypt";
    Map<String, PasswordEncoder> supportedPasswordEncoderAlgorithms = new HashMap<>();
    supportedPasswordEncoderAlgorithms.put(defaultAlgorithm, new BCryptPasswordEncoder());

    return new DelegatingPasswordEncoder(defaultAlgorithm, supportedPasswordEncoderAlgorithms);

  }

  @Bean
  public UserDetailsService accountDetailsService(UserRepository userRepository) {
    return new UserDetailsServiceImpl(userRepository);
  }

  @Bean
  public SignInBasicAuthenticationProvider basicAuthenticationProvider(
      PasswordEncoder passwordEncoder,
      UserDetailsService userDetailsService,
			AuthenticationMapper authenticationMapper
  ) {
    return new SignInBasicAuthenticationProvider(
        passwordEncoder,
        userDetailsService,
        authenticationMapper
    );
  }

  @Bean
  public SignUpBasicAuthenticationProvider basicAuthenticationSignupProvider(
      SignInBasicAuthenticationProvider signInBasicAuthenticationProvider,
      AuthenticationService authenticationService
  ) {
    return new SignUpBasicAuthenticationProvider(
        signInBasicAuthenticationProvider,
        authenticationService
    );
  }

}
