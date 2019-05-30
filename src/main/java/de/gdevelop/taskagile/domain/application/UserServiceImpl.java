package de.gdevelop.taskagile.domain.application;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import de.gdevelop.taskagile.domain.application.commands.RegistrationCommand;
import de.gdevelop.taskagile.domain.common.event.DomainEventPublisher;
import de.gdevelop.taskagile.domain.common.mail.MailManager;
import de.gdevelop.taskagile.domain.common.mail.MessageVariable;
import de.gdevelop.taskagile.domain.model.user.RegistrationException;
import de.gdevelop.taskagile.domain.model.user.RegistrationManagement;
import de.gdevelop.taskagile.domain.model.user.User;
import de.gdevelop.taskagile.domain.model.user.events.UserRegisteredEvent;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private RegistrationManagement registrationManagement;
  private DomainEventPublisher domainEventPublisher;
  private MailManager mailManager;

  public UserServiceImpl(RegistrationManagement registrationManagement, DomainEventPublisher domainEventPublisher,
      MailManager mailManager) {
    this.registrationManagement = registrationManagement;
    this.domainEventPublisher = domainEventPublisher;
    this.mailManager = mailManager;
  }

  @Override
  public void register(RegistrationCommand command) throws RegistrationException {
    Assert.notNull(command, "Parameter `command` must not be null");
    User newUser = registrationManagement.register(command.getUsername(), command.getEmailAddress(),
        command.getPassword());

    sendWelcomeMessage(newUser);
    domainEventPublisher.publish(new UserRegisteredEvent(newUser));
  }

  private void sendWelcomeMessage(User user) {
    mailManager.send(user.getEmailAddress(), "Welcome to TaskAgile", "welcome.ftl", MessageVariable.from("user", user));
  }

}
