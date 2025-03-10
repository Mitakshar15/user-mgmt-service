package org.ainkai.usermgmt.api.mapper;


import org.ainkai.digimart.lib.dto.api.BaseApiResponse;
import org.ainkai.usermgmt.api.data.model.User;
import org.ainkai.usermgmt.dtos.ActivationResponse;
import org.ainkai.usermgmt.dtos.AuthResponse;
import org.ainkai.usermgmt.dtos.SignUpRequest;
import org.ainkai.usermgmt.dtos.UserMgmtBaseApiResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMgmtMapper {
  @Mapping(target = "gender", ignore = true)
  User toUserEntity(SignUpRequest signUpRequest);

  AuthResponse toAuthResponse(BaseApiResponse apiResponse);

  ActivationResponse toActivationResponse(BaseApiResponse apiResponse);

  UserMgmtBaseApiResponse toUserMgmtBaseApiResponse(BaseApiResponse apiResponse);
}
