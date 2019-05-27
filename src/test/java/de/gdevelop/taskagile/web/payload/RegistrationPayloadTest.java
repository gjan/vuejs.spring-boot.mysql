package de.gdevelop.taskagile.web.payload;

import org.apache.commons.lang3.RandomStringUtils;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

public class RegistrationPayloadTest {

  private Validator validator;

  private static String validUsername = "Gerhard";
  private static String invalidUsernameMinLength = "G";
  private static String invalidUsernameMaxLength = "G12345678901234567890123456789012345678901234567890";
  private static String validEmailAddress = "Gerhard@home.de";
  private static String invalidEmailAddress = "Gerhard";
  private static String validPassword = "password";
  private static String invalidPasswordMinLength = "passw";
  private static String invalidPasswordMaxLength = "G1234567890123456789012345678901";

  @Before
  public void setup() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void validateBlankPayloadShouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    assertEquals(3, violations.size());
  }

  @Test
  public void validatePayloadWithInvalidEmailShouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setEmailAddress(invalidEmailAddress);
    payload.setUsername(validUsername);
    payload.setPassword(validPassword);

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    assertEquals(1, violations.size());

  }

  @Test
  public void validatePayloadWithEmailAddressLongerThan100ShouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    int maxLocalPartLength = 64;

    String localPart = RandomStringUtils.random(maxLocalPartLength, true, true);
    int usedLength = maxLocalPartLength + "@".length() + ".com".length();
    String domain = RandomStringUtils.random(101 - usedLength, true, true);

    payload.setEmailAddress(localPart + "@" + domain + ".com");
    payload.setUsername(validUsername);
    payload.setPassword(validPassword);

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    assertEquals(1, violations.size());
    ConstraintViolation<RegistrationPayload> violation = violations.stream().findFirst().get();
    assertEquals(violation.getMessage(), RegistrationPayload.EMAILADDRES_LENGTH_INVALID);
  }

  @Test
  public void validatePayloadWithUsernameShorterThan2ShouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setEmailAddress(validEmailAddress);
    payload.setUsername(invalidUsernameMinLength);
    payload.setPassword(validPassword);

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    assertEquals(1, violations.size());
    ConstraintViolation<RegistrationPayload> violation = violations.stream().findFirst().get();
    assertEquals(violation.getMessage(), RegistrationPayload.USERNAME_LENGTH_INVALID);
  }

  @Test
  public void validatePayloadWithUsernameLongerThan50ShouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setEmailAddress(validEmailAddress);
    payload.setUsername(invalidUsernameMaxLength);
    payload.setPassword(validPassword);

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    assertEquals(1, violations.size());
    ConstraintViolation<RegistrationPayload> violation = violations.stream().findFirst().get();
    assertEquals(violation.getMessage(), RegistrationPayload.USERNAME_LENGTH_INVALID);
  }

  @Test
  public void validatePayloadWithPasswordShorterThan6ShouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setEmailAddress(validEmailAddress);
    payload.setUsername(validUsername);
    payload.setPassword(invalidPasswordMinLength);

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    assertEquals(1, violations.size());
    ConstraintViolation<RegistrationPayload> violation = violations.stream().findFirst().get();
    assertEquals(violation.getMessage(), RegistrationPayload.PASSWORD_LENGTH_INVALID);
  }

  @Test
  public void validatePayloadWithPasswordLongerThan30ShouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setEmailAddress(validEmailAddress);
    payload.setUsername(validUsername);
    payload.setPassword(invalidPasswordMaxLength);

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    assertEquals(1, violations.size());
    ConstraintViolation<RegistrationPayload> violation = violations.stream().findFirst().get();
    assertEquals(violation.getMessage(), RegistrationPayload.PASSWORD_LENGTH_INVALID);
  }

}
