package de.gdevelop.taskagile.web.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.gdevelop.taskagile.domain.application.commands.RegistrationCommand;

public class RegistrationPayload {

  static final String USERNAME_LENGTH_INVALID = "Username must be between 2 and 50 charcters.";
  static final String EMAILADDRES_LENGTH_INVALID = "Email address must be shorter than 100 charcters.";
  static final String EMAILADDRES_INVALID = "Email address should be valid.";
  static final String PASSWORD_LENGTH_INVALID = "Password must be between 6 and 30 characters.";

  @Size(min = 2, max = 50, message = USERNAME_LENGTH_INVALID)
  @NotNull
  String username;

  @Size(max = 100, message = EMAILADDRES_LENGTH_INVALID)
  @Email(message = EMAILADDRES_INVALID)
  @NotNull
  private String emailAddress;

  @Size(min = 6, max = 30, message = PASSWORD_LENGTH_INVALID)
  @NotNull
  private String password;

  public RegistrationCommand toCommand() {
    return new RegistrationCommand(this.username, this.emailAddress, this.password);
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmailAddress() {
    return this.emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
