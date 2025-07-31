package com.example.domibe.global.security.exception;

public class JwtExpiredException extends RuntimeException {
  public JwtExpiredException(String message) {
    super(message);
  }
}
