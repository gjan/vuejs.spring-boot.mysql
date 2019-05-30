package de.gdevelop.taskagile.domain.application;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import de.gdevelop.taskagile.domain.application.commands.RegistrationCommand;
import de.gdevelop.taskagile.domain.common.event.DomainEventPublisher;
import de.gdevelop.taskagile.domain.common.mail.MailManager;
import de.gdevelop.taskagile.domain.common.mail.MessageVariable;
import de.gdevelop.taskagile.domain.model.user.EmailAddressExistsException;
import de.gdevelop.taskagile.domain.model.user.RegistrationException;
import de.gdevelop.taskagile.domain.model.user.RegistrationManagement;
import de.gdevelop.taskagile.domain.model.user.User;
import de.gdevelop.taskagile.domain.model.user.UsernameExistsException;
import de.gdevelop.taskagile.domain.model.user.events.UserRegisteredEvent;

public class UserServiceImplTest {

  private RegistrationManagement registrationManagementMock;
  private DomainEventPublisher eventPublisherMock;
  private MailManager mailManagerMock;
  private UserServiceImpl instance;

  @Before
  public void setUp() {
    registrationManagementMock = mock(RegistrationManagement.class);
    eventPublisherMock = mock(DomainEventPublisher.class);
    mailManagerMock = mock(MailManager.class);
    instance = new UserServiceImpl(registrationManagementMock, eventPublisherMock, mailManagerMock);
  }

  @Test(expected = IllegalArgumentException.class)
  public void registerNullCommandShouldFail() throws RegistrationException {
    instance.register(null);
  }

  @Test(expected = UsernameExistsException.class)
  public void registerExistingUsernameShouldFail() throws RegistrationException {
    String username = "existing";
    String emailAddress = "gerhard@taskagile.com";
    String password = "MyPassword!";
    doThrow(UsernameExistsException.class).when(registrationManagementMock).register(username, emailAddress, password);

    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
    instance.register(command);

  }

  @Test(expected = EmailAddressExistsException.class)
  public void registerExistingEmailAddressShouldFail() throws RegistrationException {
    String username = "existing";
    String emailAddress = "gerhard@taskagile.com";
    String password = "MyPassword!";
    doThrow(EmailAddressExistsException.class).when(registrationManagementMock).register(username, emailAddress,
        password);

    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
    instance.register(command);
  }

  @Test
  public void registerValidCommandShouldSucceed() throws RegistrationException {
    String username = "Gerhard";
    String emailAddress = "gerhard@taskagile.com";
    String password = "MyPassword!";
    User newUser = User.create(username, emailAddress, password);
    when(registrationManagementMock.register(username, emailAddress, password)).thenReturn(newUser);
    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);

    instance.register(command);

    verify(mailManagerMock).send(emailAddress, "Welcome to TaskAgile", "welcome.ftl",
        MessageVariable.from("user", newUser));
    verify(eventPublisherMock).publish(new UserRegisteredEvent(newUser));

  }
}
