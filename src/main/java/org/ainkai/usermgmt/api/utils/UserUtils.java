package org.ainkai.usermgmt.api.utils;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.ainkai.usermgmt.api.data.model.User;
import org.ainkai.usermgmt.api.utils.email.ActivationEmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtils {

  @Autowired
  private ActivationEmailSender activationEmailSender;

  public boolean sendActivationEmail(String activationCode, User user) throws MessagingException {
    return activationEmailSender.sendEmail(user.getEmail(),
        UConstants.ACTIVATION_CODE_EMAIL_SUBJECT, activationCode);
  }


}
