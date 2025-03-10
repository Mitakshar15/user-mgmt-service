package org.ainkai.usermgmt.api.builder;


import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.ainkai.digimart.lib.dto.api.BaseApiResponse;
import org.ainkai.digimart.lib.dto.api.Metadata;
import org.ainkai.digimart.lib.dto.api.Status;
import org.ainkai.usermgmt.api.config.jwt.JwtProvider;
import org.ainkai.usermgmt.api.utils.UConstants;
import org.ainkai.usermgmt.dtos.AuthResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ApiResponseBuilder {
  private final Tracer tracer;
  private final JwtProvider jwtProvider;

  public BaseApiResponse buildSuccessApiResponse(String statusMessage) {
    BaseApiResponse response = new BaseApiResponse()
        .metadata(new Metadata().timestamp(Instant.now())
            .traceId(null != tracer.currentSpan()
                ? Objects.requireNonNull(tracer.currentSpan()).context().traceId()
                : ""))
        .status(new Status().statusCode(HttpStatus.OK.value()).statusMessage(statusMessage)
            .statusMessageKey(UConstants.RESPONSE_MESSAGE_KEY_SUCCESS));
    return response;
  }

  public AuthResponseDto buildAuthResponseDto(Authentication authentication) {
    String token = jwtProvider.generateToken(authentication);
    AuthResponseDto authResponseDto = new AuthResponseDto();
    authResponseDto.setJwt(token);
    return authResponseDto;
  }
}
