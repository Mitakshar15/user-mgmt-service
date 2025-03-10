package org.ainkai.usermgmt.api.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.ainkai.usermgmt.api.config.jwt.JwtProvider;
import org.ainkai.usermgmt.api.data.VerificationTokenRepository;
import org.ainkai.usermgmt.api.data.UserRepository;
import org.ainkai.usermgmt.api.data.model.VerificationToken;
import org.ainkai.usermgmt.api.data.model.User;
import org.ainkai.usermgmt.api.utils.UConstants;
import org.ainkai.usermgmt.api.utils.UserUtils;
import org.ainkai.usermgmt.api.utils.enums.TokenType;
import org.ainkai.usermgmt.dtos.AuthResponseDto;
import org.ainkai.usermgmt.dtos.SignInRequest;
import org.ainkai.usermgmt.dtos.SignUpRequest;
import org.ainkai.usermgmt.api.mapper.UserMgmtMapper;
import org.ainkai.usermgmt.api.utils.enums.Gender;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final CustomUserServiceImpl customUserServiceImpl;
  private final JwtProvider jwtProvider;
  private final UserMgmtMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final VerificationTokenRepository verificationTokenRepository;
  private final UserUtils userUtils;

  @Override
  public User signUp(SignUpRequest signUpRequest) throws MessagingException {
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new RuntimeException("User already exists");
    }
    User user = mapper.toUserEntity(signUpRequest);
    switch (signUpRequest.getGender()) {
      case "M":
        user.setGender(Gender.MALE);
        break;
      case "F":
        user.setGender(Gender.FEMALE);
        break;
      case "O":
        user.setGender(Gender.OTHER);
        break;
    }
    user.setDateOfBirth(LocalDate.now());
    user.setActive(false);
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    User savedUser = userRepository.save(user);
    generateActivationToken(savedUser);
    return savedUser;
  }

  @Override
  public AuthResponseDto signIn(SignInRequest signInRequest) {
    Authentication authentication =
        customUserServiceImpl.authenticate(signInRequest.getEmail(), signInRequest.getPassword());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = jwtProvider.generateToken(authentication);
    AuthResponseDto authResponseDto = new AuthResponseDto();
    authResponseDto.setJwt(token);
    return authResponseDto;
  }

  @Override
  public void activateUser(User user, String activationCode) {
    if (!user.isActive() && !user.isEmailVerified()) {
      VerificationToken verificationToken =
          verificationTokenRepository.findByUserId(user.getUserId());
      if (verificationToken.getActivationCode().equals(activationCode)) {
        if (!verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
          user.setActive(true);
          user.setEmailVerified(true);
          verificationToken.setUsed(Boolean.TRUE);
          verificationTokenRepository.save(verificationToken);
          userRepository.save(user);
        } else {
          throw new RuntimeException(UConstants.EMAIL_ACTIVATION_CODE_EXPIRED_MESSAGE);
        }
      } else {
        throw new RuntimeException(UConstants.INVALID_EMAIL_ACTIVATION_CODE_MESSAGE);
      }
    } else {
      throw new RuntimeException(UConstants.ALREADY_USED_EMAIL_ACTIVATION_CODE_MESSAGE);
    }
  }

  @Override
  public VerificationToken generateActivationToken(User user) throws MessagingException {
    VerificationToken token = new VerificationToken();
    token.setUserId(user.getUserId());
    token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
    token.setTokenType(TokenType.EMAIL_VERIFICATION);
    token.setUsed(Boolean.FALSE);
    // TODO: Implement Proper Activation code generating algorithm
    token.setActivationCode(RandomStringUtils.randomAlphanumeric(6));
    System.out.println("TOKEN CODE :: " + token.getActivationCode());
    boolean isMailSent = userUtils.sendActivationEmail(token.getActivationCode(), user);
    if (!isMailSent) {
      throw new RuntimeException(UConstants.EMAIL_SENDING_FAILURE_MSG);
    }
    return verificationTokenRepository.save(token);
  }


}
