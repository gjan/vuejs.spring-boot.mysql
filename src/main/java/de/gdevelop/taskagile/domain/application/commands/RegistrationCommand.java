package de.gdevelop.taskagile.domain.application.commands;

import java.util.Objects;

import org.springframework.util.Assert;

public class RegistrationCommand {

  private String username;
  private String emailAddress;
  private String password;

  public RegistrationCommand(String username, String emailAddress, String password) {
    Assert.hasText(username, "Parameter `username` must not be empty");
    Assert.hasText(emailAddress, "Parameter `emailAddress` must not be empty");
    Assert.hasText(password, "Parameter `password` must not be empty");

    this.username = username;
    this.emailAddress = emailAddress;
    this.password = password;
  }

  public RegistrationCommand toCommand() {
    return new RegistrationCommand(this.username, this.emailAddress, this.password);
  }

  public String getUsername() {
    return this.username;
  }

  public String getEmailAddress() {
    return this.emailAddress;
  }

  public String getPassword() {
    return this.password;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof RegistrationCommand)) {
      return false;
    }
    RegistrationCommand registrationCommand = (RegistrationCommand) o;
    return Objects.equals(username, registrationCommand.username)
        && Objects.equals(emailAddress, registrationCommand.emailAddress)
        && Objects.equals(password, registrationCommand.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, emailAddress, password);
  }

  @Override
  public String toString() {
    return "RegistrationCommand {" + " username='" + getUsername() + "'" + ", emailAddress='" + getEmailAddress() + "'"
        + ", password='" + getPassword() + "'" + "}";
  }
}
