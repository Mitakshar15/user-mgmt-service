package org.ainkai.usermgmt.api.service;

import org.ainkai.usermgmt.api.data.model.User;
import org.ainkai.usermgmt.api.exceptions.UserMgmtServiceException;

public interface UserService {

  User findUserByToken(String token);

  void validateUser(String token) throws UserMgmtServiceException;

}
