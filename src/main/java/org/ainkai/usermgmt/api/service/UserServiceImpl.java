package org.ainkai.usermgmt.api.service;

import lombok.RequiredArgsConstructor;
import org.ainkai.usermgmt.api.config.jwt.JwtProvider;
import org.ainkai.usermgmt.api.data.UserRepository;
import org.ainkai.usermgmt.api.data.model.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;

  @Override
  public User findUserByToken(String token) {
    String email = jwtProvider.getEmailFromJwtToken(token);
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new RuntimeException("USER NOT FOUND");
    }
    return user;
  }
}
