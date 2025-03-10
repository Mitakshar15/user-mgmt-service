package org.ainkai.usermgmt.api.data.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
  @Id
  private String addressId;

  private String street;

  private String city;

  private String state;

  private String country;

  private String zipCode;
}
