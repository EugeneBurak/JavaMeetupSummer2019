package com.eugeneburak.trycatch.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenVerificationService {


  /**
   * Do not need.
   *
   * @param uid - usually email of the user
   * @param token - token received via request from cookies from browser
   * @return false
   */
  public boolean verify(String uid, String token) {
    return false;
  }
}