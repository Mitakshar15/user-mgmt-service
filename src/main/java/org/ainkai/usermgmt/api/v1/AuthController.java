package org.ainkai.usermgmt.api.v1;

import io.netty.util.Constant;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.ainkai.usermgmt.api.builder.ApiResponseBuilder;
import org.ainkai.usermgmt.api.data.model.User;
import org.ainkai.usermgmt.api.exceptions.UserMgmtServiceException;
import org.ainkai.usermgmt.api.mapper.UserMgmtMapper;
import org.ainkai.usermgmt.api.service.AuthService;
import org.ainkai.usermgmt.api.service.UserService;
import org.ainkai.usermgmt.api.utils.UConstants;
import org.ainkai.usermgmt.dtos.*;
import org.ainkai.usermgmt.v1.AuthControllerV1Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerV1Api {

  private final AuthService authService;
  private final ApiResponseBuilder builder;
  private final UserMgmtMapper mapper;
  private final UserService userService;

  @Override
  public ResponseEntity<AuthResponse> signUp(@Valid SignUpRequest signUpRequest)
          throws MessagingException, UserMgmtServiceException {
    User user = authService.signUp(signUpRequest);
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    AuthResponse response =
        mapper.toAuthResponse(builder.buildSuccessApiResponse(UConstants.SIGN_UP_SUCCESS_MESSAGE));
    response.data(builder.buildAuthResponseDto(authentication));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  public ResponseEntity<AuthResponse> signIn(@Valid SignInRequest signInRequest)
      throws UserMgmtServiceException {
    AuthResponse response =
        mapper.toAuthResponse(builder.buildSuccessApiResponse(UConstants.LOGIN_SUCCESS_MESSAGE));
    response.data(authService.signIn(signInRequest));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  public ResponseEntity<ActivationResponse> activateUser(
      @NotNull @RequestHeader(value = UConstants.AUTHORIZATION_HEADER_NAME) String authorization,
      @Valid @RequestBody ActivationRequest activationRequest) {
    User user = userService.findUserByToken(authorization);
    authService.activateUser(user, activationRequest.getActivationCode());
    ActivationResponse response = mapper.toActivationResponse(
        builder.buildSuccessApiResponse(UConstants.USER_ACTIVATION_SUCCESS_MESSAGE));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<UserMgmtBaseApiResponse> regenerateActivationCode(String authorization)
      throws Exception {
    UserMgmtBaseApiResponse response = mapper.toUserMgmtBaseApiResponse(
        builder.buildSuccessApiResponse(UConstants.REGENERATE_ACTIVATION_CODE_SUCCESS_MESSAGE));
    User user = userService.findUserByToken(authorization);
    authService.generateActivationToken(user);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<UserMgmtBaseApiResponse> validateUser(String authorization)
      throws Exception {
    UserMgmtBaseApiResponse response = mapper
        .toUserMgmtBaseApiResponse(builder.buildSuccessApiResponse(UConstants.VALID_USER_MESSAGE));
    userService.validateUser(authorization);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
