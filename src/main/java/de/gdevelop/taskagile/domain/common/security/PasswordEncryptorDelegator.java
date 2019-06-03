package de.gdevelop.taskagile.domain.common.security;

//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptorDelegator implements PasswordEncryptor {

  /*
   * PasswordEncoder encoder;
   *
   * public PasswordEncryptorDelegator(PasswordEncoder encoder) { this.encoder =
   * encoder; }
   */
  @Override
  public String encrypt(String rawPassword) {

    // TODO implement this
    // return encoder.encode(rawPassword);
    return rawPassword;
  }
}
