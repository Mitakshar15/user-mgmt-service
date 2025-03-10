package org.ainkai.usermgmt.api.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
  @Id
  private String id;

  @Indexed
  private String userId;

  private String token;

  private LocalDateTime expiryDate;

  private boolean isRevoked;

  private String deviceInfo;
}
