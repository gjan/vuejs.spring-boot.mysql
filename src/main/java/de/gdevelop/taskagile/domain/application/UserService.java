package de.gdevelop.taskagile.domain.application;

import de.gdevelop.taskagile.domain.application.commands.RegistrationCommand;
import de.gdevelop.taskagile.domain.model.user.RegistrationException;

public interface UserService {

  void register(RegistrationCommand command) throws RegistrationException;
}
