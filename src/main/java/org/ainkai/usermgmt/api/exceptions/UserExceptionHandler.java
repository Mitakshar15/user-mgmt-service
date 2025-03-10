package org.ainkai.usermgmt.api.exceptions;


import lombok.RequiredArgsConstructor;
import org.ainkai.digimart.lib.dto.api.*;
import org.ainkai.usermgmt.api.utils.UConstants;
import org.ainkai.usermgmt.dtos.UserMgmtBaseApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class UserExceptionHandler {

  private static final String ERROR_SUFFIX = "ERROR";

  @ExceptionHandler(UserMgmtServiceException.class)
  public ResponseEntity<UserMgmtBaseApiResponse> serviceExceptionHandler(
      UserMgmtServiceException ex, WebRequest request) {

    HttpStatusCode statusCode = switch (ex.getMessageKey()) {
      case UConstants.DATA_NOT_FOUND_KEY -> HttpStatus.NOT_FOUND;
      case UConstants.REQUEST_ERROR_KEY -> HttpStatus.BAD_REQUEST;
      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };

    UserMgmtBaseApiResponse apiResponse = (UserMgmtBaseApiResponse) new UserMgmtBaseApiResponse()
        .metadata(new Metadata().timestamp(Instant.now()))
        .status(new Status().statusCode(statusCode.value()).statusMessage(ex.getMessage())
            .statusMessageKey(ex.getMessageKey()));

    return new ResponseEntity<>(apiResponse, statusCode);
  }

  @ExceptionHandler(HttpClientErrorException.BadRequest.class)
  public ResponseEntity<UserMgmtBaseApiResponse> badRequestExceptionHandler(
      HttpClientErrorException.BadRequest ex, WebRequest request) {
    UserMgmtBaseApiResponse apiResponse = (UserMgmtBaseApiResponse) new UserMgmtBaseApiResponse()
        .metadata(new Metadata().timestamp(Instant.now()))
        .status(new Status().statusCode(HttpStatus.BAD_REQUEST.value())
            .statusMessage(UConstants.REQUEST_ERROR_MSG)
            .statusMessageKey(UConstants.REQUEST_ERROR_KEY));
    return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<UserMgmtBaseApiResponse> methodArgsExceptionHandler(
      MethodArgumentNotValidException ex, WebRequest request) {

    List<ApiMessage> errorMsgs = new ArrayList<>();
    for (ObjectError error : ex.getBindingResult().getAllErrors()) {
      String defaultMessage =
          null != error.getDefaultMessage() ? ((FieldError) error).getDefaultMessage()
              : "field error";
      errorMsgs.add(new ApiMessage().messageType(MessageType.ERROR)
          .messageKey(((FieldError) error).getField().toUpperCase() + "_" + ERROR_SUFFIX)
          .fieldName(((FieldError) error).getField()).errorType(ErrorType.REQUEST_ERROR)
          .value(defaultMessage));
    }
    UserMgmtBaseApiResponse apiResponse = (UserMgmtBaseApiResponse) new UserMgmtBaseApiResponse()
        .messages(errorMsgs).metadata(new Metadata().timestamp(Instant.now()))
        .status(new Status().statusCode(HttpStatus.BAD_REQUEST.value())
            .statusMessage(UConstants.REQUEST_ERROR_MSG)
            .statusMessageKey(UConstants.REQUEST_ERROR_KEY));
    return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<UserMgmtBaseApiResponse> missingServletRequestParameterExceptionHandler(
      MissingServletRequestParameterException ex, WebRequest request) {
    List<ApiMessage> errorMsgs = new ArrayList<>();
    errorMsgs.add(new ApiMessage().messageType(MessageType.ERROR)
        .messageKey(ex.getParameterName().toUpperCase() + "_" + ERROR_SUFFIX)
        .fieldName(ex.getParameterName()).errorType(ErrorType.REQUEST_ERROR)
        .value("Request Parameter Missing or Blank"));
    UserMgmtBaseApiResponse apiResponse = (UserMgmtBaseApiResponse) new UserMgmtBaseApiResponse()
        .messages(errorMsgs).metadata(new Metadata().timestamp(Instant.now()))
        .status(new Status().statusCode(HttpStatus.BAD_REQUEST.value())
            .statusMessage(UConstants.REQUEST_ERROR_MSG)
            .statusMessageKey(UConstants.REQUEST_ERROR_KEY));
    return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<UserMgmtBaseApiResponse> missingPathVariableExceptionHandler(
      MissingPathVariableException ex, WebRequest request) {
    List<ApiMessage> errorMsgs = new ArrayList<>();
    errorMsgs.add(new ApiMessage().messageType(MessageType.ERROR)
        .messageKey(ex.getVariableName().toUpperCase() + "_" + ERROR_SUFFIX)
        .fieldName(ex.getVariableName()).errorType(ErrorType.REQUEST_ERROR).value(ex.getMessage()));
    UserMgmtBaseApiResponse apiResponse = (UserMgmtBaseApiResponse) new UserMgmtBaseApiResponse()
        .messages(errorMsgs).metadata(new Metadata().timestamp(Instant.now()))
        .status(new Status().statusCode(HttpStatus.BAD_REQUEST.value())
            .statusMessage(UConstants.REQUEST_ERROR_MSG)
            .statusMessageKey(UConstants.REQUEST_ERROR_KEY));
    return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<UserMgmtBaseApiResponse> handleMaxSizeException(
      MaxUploadSizeExceededException exc, WebRequest request) {
    UserMgmtBaseApiResponse apiResponse = (UserMgmtBaseApiResponse) new UserMgmtBaseApiResponse()
        .messages(null).metadata(new Metadata().timestamp(Instant.now()))
        .status(new Status().statusCode(HttpStatus.BAD_REQUEST.value())
            .statusMessage(UConstants.REQUEST_SIZE_ERROR_MSG)
            .statusMessageKey(UConstants.REQUEST_ERROR_KEY));
    return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
  }

}
