package de.gdevelop.taskagile.domain.application;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.gdevelop.taskagile.domain.application.commands.RegistrationCommand;
import de.gdevelop.taskagile.domain.common.event.DomainEventPublisher;
import de.gdevelop.taskagile.domain.common.mail.MailManager;
import de.gdevelop.taskagile.domain.common.mail.MessageVariable;
import de.gdevelop.taskagile.domain.model.user.EmailAddressExistsException;
import de.gdevelop.taskagile.domain.model.user.RegistrationException;
import de.gdevelop.taskagile.domain.model.user.RegistrationManagement;
import de.gdevelop.taskagile.domain.model.user.SimpleUser;
import de.gdevelop.taskagile.domain.model.user.User;
import de.gdevelop.taskagile.domain.model.user.UserRepository;
import de.gdevelop.taskagile.domain.model.user.UsernameExistsException;
import de.gdevelop.taskagile.domain.model.user.events.UserRegisteredEvent;

public class UserServiceImplTest {

  private RegistrationManagement registrationManagementMock;
  private DomainEventPublisher eventPublisherMock;
  private MailManager mailManagerMock;
  private UserRepository userRepositoryMock;
  private UserServiceImpl instance;

  @Before
  public void setUp() {
    registrationManagementMock = mock(RegistrationManagement.class);
    eventPublisherMock = mock(DomainEventPublisher.class);
    mailManagerMock = mock(MailManager.class);
    userRepositoryMock = mock(UserRepository.class);
    instance = new UserServiceImpl(registrationManagementMock, eventPublisherMock, mailManagerMock, userRepositoryMock);
  }

  @Test
  public void loadUserByUsernameEmptyUsernameShouldFail() {
    Exception exception = null;
    try {
      instance.loadUserByUsername("");
    } catch (Exception e) {
      exception = e;
    }
    assertNotNull(exception);
    assertTrue(exception instanceof UsernameNotFoundException);
    verify(userRepositoryMock, never()).findByUsername("");
    verify(userRepositoryMock, never()).findByEmailAddress("");
  }

  @Test
  public void loadUserByUsername_notExistUsername_shouldFail() {
    String notExistUsername = "NotExistUsername";
    when(userRepositoryMock.findByUsername(notExistUsername)).thenReturn(null);
    Exception exception = null;
    try {
      instance.loadUserByUsername(notExistUsername);
    } catch (Exception e) {
      exception = e;
    }
    assertNotNull(exception);
    assertTrue(exception instanceof UsernameNotFoundException);
    verify(userRepositoryMock).findByUsername(notExistUsername);
    verify(userRepositoryMock, never()).findByEmailAddress(notExistUsername);
  }

  @Test
  public void loadUserByUsername_existUsername_shouldSucceed() throws IllegalAccessException {
    String existUsername = "ExistUsername";
    User foundUser = User.create(existUsername, "user@taskagile.com", "EncryptedPassword!");
    foundUser.updateName("Test", "User");
    // Found user from the database should have id. And since no setter of
    // id is available in User, we have to write the value to it using reflection
    //
    // Besides creating an actual instance of User, we can also create a user
    // mock, like the following.
    // User mockUser = Mockito.mock(User.class);
    // when(mockUser.getUsername()).thenReturn(existUsername);
    // when(mockUser.getPassword()).thenReturn("EncryptedPassword!");
    // when(mockUser.getId()).thenReturn(1L);
    FieldUtils.writeField(foundUser, "id", 1L, true);
    when(userRepositoryMock.findByUsername(existUsername)).thenReturn(foundUser);
    Exception exception = null;
    UserDetails userDetails = null;
    try {
      userDetails = instance.loadUserByUsername(existUsername);
    } catch (Exception e) {
      exception = e;
    }
    assertNull(exception);
    verify(userRepositoryMock).findByUsername(existUsername);
    verify(userRepositoryMock, never()).findByEmailAddress(existUsername);
    assertNotNull(userDetails);
    assertEquals(existUsername, userDetails.getUsername());
    assertTrue(userDetails instanceof SimpleUser);
  }

  @Test(expected = IllegalArgumentException.class)
  public void registerNullCommandShouldFail() throws RegistrationException {
    instance.register(null);
  }

  @Test(expected = UsernameExistsException.class)
  public void registerExistingUsernameShouldFail() throws RegistrationException {
    String username = "existing";
    String emailAddress = "gerhard@home.de";
    String password = "MyPassword!";
    doThrow(UsernameExistsException.class).when(registrationManagementMock).register(username, emailAddress, password);

    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
    instance.register(command);

  }

  @Test(expected = EmailAddressExistsException.class)
  public void registerExistingEmailAddressShouldFail() throws RegistrationException {
    String username = "existing";
    String emailAddress = "gerhard@home.de";
    String password = "MyPassword!";
    doThrow(EmailAddressExistsException.class).when(registrationManagementMock).register(username, emailAddress,
        password);

    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
    instance.register(command);
  }

  @Test
  public void registerValidCommandShouldSucceed() throws RegistrationException {
    String username = "Gerhard";
    String emailAddress = "gerhard@home.de";
    String password = "MyPassword!";
    User newUser = User.create(username, emailAddress, password);
    when(registrationManagementMock.register(username, emailAddress, password)).thenReturn(newUser);
    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);

    instance.register(command);

    verify(mailManagerMock).send(emailAddress, "Welcome to TaskAgile", "welcome.ftl",
        MessageVariable.from("user", newUser));
    verify(eventPublisherMock).publish(new UserRegisteredEvent(this, newUser));

  }
}
