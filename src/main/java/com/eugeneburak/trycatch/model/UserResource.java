package com.eugeneburak.trycatch.model;

import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

public class UserResource extends ResourceSupport {

  @Getter private String email;

  /** Constructor. */
  UserResource(TryCatchUser user) {
    this.email = user.getEmail();
  }
}
