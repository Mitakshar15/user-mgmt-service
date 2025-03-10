package org.ainkai.usermgmt.api.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ainkai.usermgmt.api.utils.enums.TokenType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "verification_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {
  @Id
  private String id;

  private String activationCode;

  @Indexed
  private String userId;

  private TokenType tokenType;

  private LocalDateTime expiryDate;

  private boolean isUsed;

}
