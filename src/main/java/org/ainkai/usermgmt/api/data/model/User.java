package org.ainkai.usermgmt.api.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ainkai.usermgmt.api.utils.enums.Gender;
import org.ainkai.usermgmt.api.utils.enums.UserRole;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
  @Id
  private String userId;

  @Indexed(unique = true, direction = IndexDirection.ASCENDING)
  private String email;

  private String username;

  private String password;

  private String firstName;

  private String lastName;

  private String profilePicture;

  private String bio;

  private boolean isActive;

  private boolean isEmailVerified;

  private UserRole role;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  private List<String> followers;

  private List<String> following;

  private LocalDate dateOfBirth;

  private Gender gender;

  @DBRef
  private Address address;
}
