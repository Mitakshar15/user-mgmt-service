package org.ainkai.usermgmt.api.service;

import lombok.RequiredArgsConstructor;
import org.ainkai.usermgmt.api.config.jwt.JwtProvider;
import org.ainkai.usermgmt.api.data.UserRepository;
import org.ainkai.usermgmt.api.data.model.User;
import org.ainkai.usermgmt.api.exceptions.UserMgmtServiceException;
import org.ainkai.usermgmt.api.utils.UConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Override
  public void validateUser(String token)throws UserMgmtServiceException {
    User user = findUserByToken(token);
    if (user == null) {
      throw new UserMgmtServiceException(UConstants.DATA_NOT_FOUND_KEY,UConstants.INVALID_USER_MESSAGE);
    }
    // TODO: implement other validation logic here later
  }
}
