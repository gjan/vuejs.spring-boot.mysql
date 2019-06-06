package de.gdevelop.taskagile.domain.model.user;

import de.gdevelop.taskagile.domain.common.security.PasswordEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RegistrationManagementTest {

  UserRepository repositoryMock;
  PasswordEncryptor passwordEncryptorMock;
  RegistrationManagement instance;

  @Before
  public void setup() {
    repositoryMock = mock(UserRepository.class);
    passwordEncryptorMock = mock(PasswordEncryptor.class);

    instance = new RegistrationManagement(repositoryMock, passwordEncryptorMock);
  }

  @Test(expected = UsernameExistsException.class)
  public void registerExistentUsernameShouldFail() throws RegistrationException {
    String username = "existUsername";
    String emailAddress = "sunny@taskagile.com";
    String firstName = "Existing";
    String lastName = "User";
    String password = "MyPassword!";

    // We just return an empty user object to indicate an existing user
    when(repositoryMock.findByUsername(username)).thenReturn(new User());
    instance.register(username, emailAddress, firstName, lastName, password);
  }

  @Test(expected = EmailAddressExistsException.class)
  public void registerExistentEmailAddressShouldFail() throws RegistrationException {
    String username = "sunny";
    String emailAddress = "exist@taskagile.com";
    String firstName = "Existing";
    String lastName = "User";
    String password = "MyPassword!";
    // We just return an empty user object to indicate an existing user
    when(repositoryMock.findByEmailAddress(emailAddress)).thenReturn(new User());
    instance.register(username, emailAddress, firstName, lastName, password);

  }

  @Test
  public void registerUppercaseEmailAddressShouldSucceedAndBecomeLowercase() throws RegistrationException {
    String username = "sunny";
    String emailAddress = "Sunny@TaskAgile.com";
    String firstName = "Sunny";
    String lastName = "Hu";
    String password = "MyPassword!";
    instance.register(username, emailAddress, firstName, lastName, password);
    User userToSave = User.create(username, emailAddress.toLowerCase(), firstName, lastName, password);
    verify(repositoryMock).save(userToSave);
  }

  @Test
  public void registerNewUserShouldSucceed() throws RegistrationException {
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    String firstName = "Sunny";
    String lastName = "Hu";
    String password = "MyPassword!";
    String encryptedPassword = "EncryptedPassword";
    User newUser = User.create(username, emailAddress, firstName, lastName, encryptedPassword);

    // Setup repository mock
    // Return null to indicate no user exists
    when(repositoryMock.findByUsername(username)).thenReturn(null);
    when(repositoryMock.findByEmailAddress(emailAddress)).thenReturn(null);
    doNothing().when(repositoryMock).save(newUser);
    // Setup passwordEncryptor mock
    when(passwordEncryptorMock.encrypt(password)).thenReturn("EncryptedPassword");

    User savedUser = instance.register(username, emailAddress, firstName, lastName, password);
    InOrder inOrder = inOrder(repositoryMock);
    inOrder.verify(repositoryMock).findByUsername(username);
    inOrder.verify(repositoryMock).findByEmailAddress(emailAddress);
    inOrder.verify(repositoryMock).save(newUser);
    verify(passwordEncryptorMock).encrypt(password);
    assertEquals("Saved user's password should be encrypted", encryptedPassword, savedUser.getPassword());
  }
}
