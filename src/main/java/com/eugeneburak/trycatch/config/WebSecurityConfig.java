package com.eugeneburak.trycatch.config;

import com.eugeneburak.trycatch.config.profiles.ProfileNotDev;
import com.eugeneburak.trycatch.security.CookieExtractor;
import com.eugeneburak.trycatch.security.ExternalTokenAuthenticationFilter;
import com.eugeneburak.trycatch.security.ExternalTokenAuthenticationProvider;
import com.eugeneburak.trycatch.security.RestAuthenticationEntryPoint;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@ProfileNotDev
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {

  public static final String ANY_CHAR_REGEX = ".*";

  /**
   *  ALLOWED_API_PATHS represent an array of non secured API sub paths,
   *  for all sub paths that not exist in array will be used default external authentication service
   *  to add new non secured sub path, just add it to array.
   *  Note: this not affected common resources,
   *  @see com.eugeneburak.trycatch.controller.TryCatchController
   *  Note: API sub path means path that follows after this.apiPath
  */
  private static final String[] ALLOWED_API_PATHS = new String[] {
      "/points"
  };

  @Value("${spring.data.rest.base-path}")
  private String apiPath;

  @Autowired
  RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Autowired
  private ExternalTokenAuthenticationProvider externalTokenAuthenticationProvider;

  @SneakyThrows
  protected ExternalTokenAuthenticationFilter buildExternalTokenAuthenticationFilter() {
    return new ExternalTokenAuthenticationFilter(buildSecuredMatcher(),
        super.authenticationManager(), new CookieExtractor());
  }

  @SneakyThrows
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() {
    return super.authenticationManagerBean();
  }

  @Autowired
  @Override
  public void configure(AuthenticationManagerBuilder builder) {
    builder.authenticationProvider(externalTokenAuthenticationProvider);
  }

  @SneakyThrows
  @Override
  protected void configure(HttpSecurity http) {
    http.addFilterBefore(buildExternalTokenAuthenticationFilter(),
        UsernamePasswordAuthenticationFilter.class);
    http.exceptionHandling().authenticationEntryPoint(this.restAuthenticationEntryPoint);
    http.authorizeRequests().requestMatchers(buildSecuredMatcher()).authenticated()
        .anyRequest().permitAll()
        .antMatchers(HttpMethod.DELETE).denyAll();
    http.csrf()
        .disable();
  }

  /** Pattern.
   @param subpaths - String array
   @return regex substring for ignoring all elements from array,
       if any element of array will be present in string, java matcher will return false,
       see https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html,
       lookup for zero-width negative lookahead
  */
  private String buildIgnoredApiRegexSubPatches(String[] subpaths) {
    return Arrays
        .stream(subpaths)
        .collect(
            Collectors.joining(")|(","(?!((",")))")
        );
  }

  private RequestMatcher buildSecuredMatcher() {
    StringBuilder sb = new StringBuilder(ANY_CHAR_REGEX)
        .append(this.apiPath)
        .append(buildIgnoredApiRegexSubPatches(ALLOWED_API_PATHS))
        .append(ANY_CHAR_REGEX);
    return new RegexRequestMatcher(sb.toString(), null);
  }

  public void setApiPath(String apiPath) {
    this.apiPath = apiPath;
  }
}