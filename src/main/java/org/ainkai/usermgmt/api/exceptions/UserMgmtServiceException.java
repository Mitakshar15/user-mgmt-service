package org.ainkai.usermgmt.api.exceptions;

import lombok.Getter;

import java.io.Serial;

@Getter
public class UserMgmtServiceException extends Exception {

  @Serial
  private static final long serialVersionUID = 1L;
  private final String messageKey;

  public UserMgmtServiceException(String messageKey, String message, Throwable cause) {
    super(message, cause);
    this.messageKey = messageKey;
  }

  public UserMgmtServiceException(String messageKey, String message) {
    super(message);
    this.messageKey = messageKey;
  }
}
