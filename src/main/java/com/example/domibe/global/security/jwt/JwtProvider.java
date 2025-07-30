package com.example.domibe.global.security.jwt;

import com.example.domibe.global.security.service.AuthDetailsService;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.print.DocFlavor;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

  private final JwtProperties jwtProperties;
  private final AuthDetailsService authDetailsService;
  private final static String ACCESS_TOKEN = "access_token";
  private final static String REFRESH_TOKEN = "refresh_token";

  //토큰 생성기능
  public String generateAccessToken(String accountId) {
    return generateToken(accountId,ACCESS_TOKEN,jwtProperties.getAccessTokenExpiresIn());
  }

  public String generateRefreshToken(String accountId) {
    return generateToken(accountId,REFRESH_TOKEN,jwtProperties.getRefreshTokenExpiresIn());
  }

  public String generateToken(String accountId,String type,Long time) {
    Date now = new Date();
    return Jwts.builder()
        .signWith(SignatureAlgorithm.HS256,jwtProperties.getSecretKey())
        .setSubject(accountId)
        .setIssuedAt(now)
        .setHeaderParam("typ",type)
        .setExpiration(new Date(now.getTime()+time))
        .compact();
  }
  //토큰 파싱 기능
  public String parseToken(String bearerToken) {
    if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.replace("Bearer ","");
    }
    return null;
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    return parseToken(bearerToken);
  }

  public UsernamePasswordAuthenticationToken resolveAuthentication(HttpServletRequest request) {
    UserDetails userDetails=authDetailsService.
  }


}
