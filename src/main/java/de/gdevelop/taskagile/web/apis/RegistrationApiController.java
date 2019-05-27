package de.gdevelop.taskagile.web.apis;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import de.gdevelop.taskagile.domain.application.UserService;
import de.gdevelop.taskagile.domain.model.user.EmailAddressExistsException;
import de.gdevelop.taskagile.domain.model.user.RegistrationException;
import de.gdevelop.taskagile.domain.model.user.UsernameExistsException;
import de.gdevelop.taskagile.web.payload.RegistrationPayload;
import de.gdevelop.taskagile.web.results.ApiResult;
import de.gdevelop.taskagile.web.results.Result;

@Controller
public class RegistrationApiController {

  private UserService service;

  public RegistrationApiController(UserService service) {
    this.service = service;
  }

  @PostMapping("/api/registrations")
  public ResponseEntity<ApiResult> register(@Valid @RequestBody RegistrationPayload payload) {
    try {
      service.register(payload.toCommand());
      return Result.created();
    } catch (RegistrationException e) {
      String errorMessage = "Registration failed";
      if (e instanceof UsernameExistsException) {
        errorMessage = "Username already exists";
      } else if (e instanceof EmailAddressExistsException) {
        errorMessage = "Email address already exists";
      }
      return Result.failure(errorMessage);
    }
  }
}
