package com.example.domibe.global.security.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNofFoundException extends UsernameNotFoundException {
  public UserNofFoundException(String message) {
    super(message);
  }
}
