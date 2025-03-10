package org.ainkai.usermgmt.api.service;

import jakarta.mail.MessagingException;
import org.ainkai.usermgmt.api.data.model.VerificationToken;
import org.ainkai.usermgmt.api.data.model.User;
import org.ainkai.usermgmt.dtos.AuthResponseDto;
import org.ainkai.usermgmt.dtos.SignInRequest;
import org.ainkai.usermgmt.dtos.SignUpRequest;

public interface AuthService {
  User signUp(SignUpRequest signUpRequest) throws MessagingException;

  AuthResponseDto signIn(SignInRequest signInRequest);

  void activateUser(User user, String activationCode);

  VerificationToken generateActivationToken(User user) throws MessagingException;
}
