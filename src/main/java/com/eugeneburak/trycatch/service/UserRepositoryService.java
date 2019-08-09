package com.eugeneburak.trycatch.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.eugeneburak.trycatch.controller.TryCatchRepositoryController;
import com.eugeneburak.trycatch.dto.CardCreatedOrUpdatedDto;
import com.eugeneburak.trycatch.exception.IncorrectInputDataException;
import com.eugeneburak.trycatch.model.TryCatchUser;
import com.eugeneburak.trycatch.model.UserResource;
import com.eugeneburak.trycatch.model.UserResourceAssembler;
import com.eugeneburak.trycatch.repository.TryCatchUserRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryService {

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  private final TryCatchUserRepository tryCatchUserRepository;

  private final SnsService snsService;

  public UserRepositoryService(
          TryCatchUserRepository tryCatchUserRepository, SnsService snsService) {
    this.tryCatchUserRepository = tryCatchUserRepository;
    this.snsService = snsService;
  }

  /** Save new user. */
  public ResponseEntity<Resource<UserResource>> save(TryCatchUser tryCatchUser) {

    TryCatchUser save =
        Optional.ofNullable(tryCatchUser)
            .map(tryCatchUserRepository::save)
            .orElseThrow(() -> new IncorrectInputDataException("can't save a new user"));
    return notify(save);
  }

  /** Update user. */
  public ResponseEntity<Resource<UserResource>> updateCardNumber(
      TryCatchUser tryCatchUser, String id) {

    TryCatchUser update =
        Optional.ofNullable(tryCatchUser)
            .map(
                updateUser -> {
                  TryCatchUser user = tryCatchUserRepository.findFirstById(id);
                  return tryCatchUserRepository.save(user);
                })
            .orElseThrow(() -> new IncorrectInputDataException("can't update a new user"));
    return notify(update);
  }

  /** Get user. */
  public ResponseEntity<Resource<UserResource>> getUser(String id) {

    TryCatchUser getUser =
        Optional.ofNullable(id)
            .map(tryCatchUserRepository::findFirstById)
            .orElseThrow(() -> new IncorrectInputDataException("can't save a new user"));
    return responseGenerator(getUser);
  }

  private ResponseEntity<Resource<UserResource>> notify(TryCatchUser tryCatchUser) {

    CardCreatedOrUpdatedDto cardCreatedOrUpdatedDto =
        CardCreatedOrUpdatedDto.builder()
            .mail(tryCatchUser.getEmail())
            .created(SIMPLE_DATE_FORMAT.format(new Date()))
            .build();

    snsService.notifyCardCreatedOrUpdated(cardCreatedOrUpdatedDto);

    return responseGenerator(tryCatchUser);
  }

  private ResponseEntity<Resource<UserResource>> responseGenerator(TryCatchUser tryCatchUser) {

    String linkTo =
        linkTo(methodOn(TryCatchRepositoryController.class).saveNewUser(tryCatchUser)).toString()
            + "/"
            + tryCatchUser.getId();

    UserResource userResource = new UserResourceAssembler().toResource(tryCatchUser); //

    Resource<UserResource> recentResources = new Resource<>(userResource);
    recentResources.add(new Link(linkTo, "tryCatchUser"));
    recentResources.add(new Link(linkTo, "self"));

    return new ResponseEntity<>(recentResources, HttpStatus.OK);
  }
}
