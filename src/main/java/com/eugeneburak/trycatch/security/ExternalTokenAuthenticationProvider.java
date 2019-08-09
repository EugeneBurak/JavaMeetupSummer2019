package com.eugeneburak.trycatch.security;

import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Log
@Data
@Component
public class ExternalTokenAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private TokenVerificationService tokenVerificationService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    ExternalAuthenticationToken externalAuthenticationToken =
        (ExternalAuthenticationToken) authentication;

    String uid = externalAuthenticationToken.getPrincipal();
    String externalToken = externalAuthenticationToken.getExternalToken();

    boolean isVerified = tokenVerificationService.verify(uid, externalToken);
    return isVerified
        ? ExternalAuthenticationToken.builder()
        .principal(uid)
        .externalToken(externalToken)
        .verified(isVerified).build()
        : null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(ExternalAuthenticationToken.class);
  }
}