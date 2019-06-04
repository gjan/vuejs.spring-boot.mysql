package de.gdevelop.taskagile.domain.application;

import org.springframework.security.core.userdetails.UserDetailsService;

import de.gdevelop.taskagile.domain.application.commands.RegistrationCommand;
import de.gdevelop.taskagile.domain.model.user.RegistrationException;

public interface UserService extends UserDetailsService {

  void register(RegistrationCommand command) throws RegistrationException;
}
