package com.eugeneburak.trycatch.controller;

import com.eugeneburak.trycatch.model.TryCatchUser;
import com.eugeneburak.trycatch.model.UserResource;
import com.eugeneburak.trycatch.service.UserRepositoryService;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RepositoryRestController
@RequestMapping(path = "trycatch/api")
public class TryCatchRepositoryController {

  private final UserRepositoryService userRepositoryService;

  public TryCatchRepositoryController(UserRepositoryService userRepositoryService) {
    this.userRepositoryService = userRepositoryService;
  }

  /**
   * The controller returns the saved object and status 200 if everything OK or status 422 if
   * something wrong with an object.
   */
  @PostMapping(path = "/users")
  public ResponseEntity<Resource<UserResource>> saveNewUser(@RequestBody TryCatchUser user) {

    return userRepositoryService.save(user);
  }

  /**
   * The controller returns the updated object and status 200 if everything OK or status 422 if
   * something wrong with an object.
   */
  @PatchMapping(path = "/users/{id}")
  public ResponseEntity<Resource<UserResource>> updateUserCardNumber(
      @RequestBody TryCatchUser user, @PathVariable("id") String id) {

    return userRepositoryService.updateCardNumber(user, id);
  }

  /**
   * The controller returns the found object and status 200 if everything OK or status 422 if
   * something wrong with an object.
   */
  @GetMapping(path = "/users/{id}")
  public ResponseEntity<Resource<UserResource>> getUser(@PathVariable("id") String id) {

    return userRepositoryService.getUser(id);
  }
}
