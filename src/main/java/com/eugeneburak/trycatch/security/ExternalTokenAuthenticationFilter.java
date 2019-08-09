package com.eugeneburak.trycatch.security;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

@Log
public class ExternalTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
  private final CookieExtractor cookieExtractor;

  /**
   * Creates ExternalTokenAuthenticationFilter
   * an instance of AbstractAuthenticationProcessingFilter.
   *
   * @param matcher - string relative URL,
   *                request to which should be processed by ExternalTokenAuthenticationFilter
   * @param authenticationManager - Implementation class of AuthenticationManager used by default)
   *                              delegates actual authentication process to
   *                              implementation class of AuthenticationProvider
   * @param cookieExtractor - instance of class that provide method
   *                        #extract to convert array of cookies into Map for friendly using
   */
  @Autowired
  public ExternalTokenAuthenticationFilter(final RequestMatcher matcher,
                                           AuthenticationManager authenticationManager,
                                           CookieExtractor cookieExtractor) {
    super(matcher);
    super.setAuthenticationManager(authenticationManager);
    this.cookieExtractor = cookieExtractor;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response)
      throws AuthenticationException {
    Map<String, String> cookiesMap = cookieExtractor.extractFromRequest(request);
    String uid = cookiesMap.get("userid");
    String token = cookiesMap.get("token");

    if (!StringUtils.hasText(uid)
        || !StringUtils.hasText(token)) {
      log.warning("uid: " + uid + " | token: " + token);
      throw new AuthenticationServiceException("uid: " + uid + " | " + "token: " + token);
    }

    uid = decode(uid);
    ExternalAuthenticationToken externalAuthenticationToken =
        ExternalAuthenticationToken.builder().principal(uid).externalToken(token).build();
    return getAuthenticationManager().authenticate(externalAuthenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain, Authentication authResult)
      throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(authResult);
    chain.doFilter(request, response);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException failed) {
    SecurityContextHolder.clearContext();
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  }

  @SneakyThrows
  private String decode(String source) {
    return source = URLDecoder.decode(source, "UTF-8");
  }
}