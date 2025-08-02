package com.example.domibe.global.security.jwt;

import com.example.domibe.global.security.auth.CustomUserDetails;
import com.example.domibe.global.security.auth.CustomUserDetailsService;
import com.example.domibe.global.security.exception.JwtExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final JwtProperties jwtProperties;
  private final CustomUserDetailsService authDetailsService;
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

  //토큰에서 값 가져오기
  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.replace("Bearer ", "");
    }
    return null;
  }

  //토큰의 유효성을 감사
  public boolean validateToken(String token){
    try {
      Claims claims = Jwts.parser()
          .setSigningKey(jwtProperties.getSecretKey())
          .parseClaimsJws(token)
          .getBody();
      return true;
    }catch (RuntimeException e){
      System.out.println("Invalid JWT");
      throw new RuntimeException();

    }
  }

  public UsernamePasswordAuthenticationToken getAuthentication(String token) {
    Claims claims=getClaims(token);
    CustomUserDetails customUserDetails=(CustomUserDetails) authDetailsService.loadUserByUsername(claims.getSubject());
    return new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());
  }

  private Claims getClaims(String token) {
    try {
      return Jwts.parser()
          .setSigningKey(jwtProperties.getSecretKey())
          .parseClaimsJws(token)
          .getBody();
    } catch (JwtExpiredException e) {
      throw new JwtExpiredException("토큰이 유효하지 않습니다");
    }
  }
}
