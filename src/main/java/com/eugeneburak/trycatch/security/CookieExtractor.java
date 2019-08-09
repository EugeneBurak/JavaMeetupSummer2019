package com.eugeneburak.trycatch.security;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;

public class CookieExtractor {

  /**
   * Transform array of Cookie into Map Cookie Name - Cookie Value.
   *
   * @param request - HttpServletRequest with cookies
   * @return Map(String,String) with Entry("cookie name", "cookie value")
   */
  public Map<String, String> extractFromRequest(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    Assert.notNull(cookies, "Cookies should not be empty");
    return convertToMap(cookies);
  }

  private Map<String, String> convertToMap(Cookie[] cookies) {
    return Arrays.stream(cookies)
        .collect(Collectors.toMap(
            Cookie::getName, Function.identity(),
            BinaryOperator.maxBy(Comparator.comparingInt(Cookie::getMaxAge))
        )).values().stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
  }
}