package de.gdevelop.taskagile.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import de.gdevelop.taskagile.domain.model.user.User;
import de.gdevelop.taskagile.domain.model.user.UserRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
public class HibernateUserRepositoryTest {

  @TestConfiguration
  public static class UserRepositoryTestContextConfiguration {
    @Bean
    public UserRepository userRepository(EntityManager entityManager) {
      return new HibernateUserRepository(entityManager);
    }
  }

  @Autowired
  private UserRepository repository;

  @Test(expected = PersistenceException.class)
  public void saveNullUsernameUserShouldFail() {
    User invalidUser = User.create(null, "gerhard@home.de", "Any", "Body", "MyPassword!");
    repository.save(invalidUser);
  }

  @Test(expected = PersistenceException.class)
  public void saveNullEmailAddressUserShouldFail() {
    User invalidUser = User.create("Gerhard", null, "Any", "Body", "MyPassword!");
    repository.save(invalidUser);
  }

  @Test(expected = PersistenceException.class)
  public void saveNullPasswordUserShouldFail() {
    User invalidUser = User.create("Gerhard", "gerhard@home.de", "Any", "Body", null);
    repository.save(invalidUser);
  }

  @Test
  public void saveValidUserShouldSuccess() {

    String username = "Gerhard";
    String emailAddress = "gerhard@home.de";
    String firstName = "Gerhard";
    String lastName = "Jansen";
    User validUser = User.create(username, emailAddress, firstName, lastName, "MyPassword!");
    repository.save(validUser);
    assertNotNull("New user's id should be generated", validUser.getId());
    assertNotNull("New user's created date should be generated", validUser.getCreatedDate());
    assertEquals(username, validUser.getUsername());
    assertEquals(emailAddress, validUser.getEmailAddress());
    assertEquals(firstName, validUser.getFirstName());
    assertEquals(lastName, validUser.getLastName());

  }

  @Test
  public void saveUsernameAlreadyExistShouldFail() {
    String username = "Gerhard";
    String emailAddress = "gerhard@home.de";
    String firstName = "Gerhard";
    String lastName = "Jansen";
    User validUser = User.create(username, emailAddress, firstName, lastName, "MyPassword!");
    repository.save(validUser);

    try {
      User newUser = User.create(username, "home@home.de", firstName, lastName, "MyPassword!");
      repository.save(newUser);
    } catch (Exception e) {
      assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
    }
  }

  @Test
  public void saveEmailAddressAlreadyExistShouldFail() {
    String username = "Gerhard";
    String emailAddress = "gerhard@home.de";
    String firstName = "Gerhard";
    String lastName = "Jansen";
    User validUser = User.create(username, emailAddress, firstName, lastName, "MyPassword!");
    repository.save(validUser);

    try {
      User newUser = User.create("Gerd", emailAddress, firstName, lastName, "MyPassword!");
      repository.save(newUser);
    } catch (Exception e) {
      assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
    }
  }

  @Test
  public void findByEmailAddressNotExistShouldReturnEmptyResult() {
    String emailAddress = "gerhard@home.de";
    User noUser = repository.findByEmailAddress(emailAddress);
    assertNull("No user should be found", noUser);
  }

  @Test
  public void findByEmailAddressExistShouldReturnResult() {
    String username = "Gerhard";
    String emailAddress = "gerhard@home.de";
    String firstName = "Gerhard";
    String lastName = "Jansen";
    User validUser = User.create(username, emailAddress, firstName, lastName, "MyPassword!");
    repository.save(validUser);

    User foundUser = repository.findByEmailAddress(emailAddress);
    assertNotNull("User should be found", foundUser);
    assertEquals("Username should match", foundUser.getUsername(), username);
    assertEquals("Mailadderes should match", foundUser.getEmailAddress(), emailAddress);
    assertEquals("Firstname should match", firstName, foundUser.getFirstName());
    assertEquals("Lastname should match", lastName, foundUser.getLastName());
  }

  @Test
  public void findByUsernameNotExistShouldReturnEmptyResult() {
    String username = "Gerhard";
    User noUser = repository.findByUsername(username);
    assertNull("No user should be found", noUser);
  }

  @Test
  public void findByUsernameExistShouldReturnResult() {
    String username = "Gerhard";
    String emailAddress = "gerhard@home.de";
    String firstName = "Gerhard";
    String lastName = "Jansen";
    User validUser = User.create(username, emailAddress, firstName, lastName, "MyPassword!");
    repository.save(validUser);

    User foundUser = repository.findByUsername(username);
    assertNotNull("User should be found", foundUser);
    assertEquals("Username should match", foundUser.getUsername(), username);
    assertEquals("Mailadderes should match", foundUser.getEmailAddress(), emailAddress);
    assertEquals("Firstname should match", firstName, foundUser.getFirstName());
    assertEquals("Lastname should match", lastName, foundUser.getLastName());
  }
}
