package org.ainkai.usermgmt.api.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.ainkai.digimart.lib.constants.JwtConstants;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtProvider {
  private final SecretKey key;

  {
    key = Keys.hmacShaKeyFor(JwtConstants.SECRET_KEY.getBytes());
  }

  public String generateToken(Authentication auth) {

    String jwt;
    jwt = Jwts.builder().setIssuedAt(new Date())
        .setExpiration(new Date(new Date().getTime() + 86400000)).claim("email", auth.getName())
        .signWith(key).compact();

    return jwt;
  }

  public String getEmailFromJwtToken(String jwt) {
    jwt = jwt.substring(7);

    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
    String email;
    email = String.valueOf(claims.get("email"));

    return email;
  }
}
