package com.eugeneburak.trycatch.model;

import com.eugeneburak.trycatch.controller.TryCatchController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class UserResourceAssembler extends ResourceAssemblerSupport<TryCatchUser, UserResource> {

  public UserResourceAssembler() {
    super(TryCatchController.class, UserResource.class);
  }

  @Override
  protected UserResource instantiateResource(TryCatchUser tryCatchUser) {
    return new UserResource(tryCatchUser);
  }

  @Override
  public UserResource toResource(TryCatchUser tryCatchUser) {
    return new UserResource(tryCatchUser);
  }
}
