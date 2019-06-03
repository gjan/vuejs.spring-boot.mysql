package de.gdevelop.taskagile.web.apis;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import de.gdevelop.taskagile.config.SecurityConfiguration;
import de.gdevelop.taskagile.domain.application.UserService;
import de.gdevelop.taskagile.domain.model.user.EmailAddressExistsException;
import de.gdevelop.taskagile.domain.model.user.UsernameExistsException;
import de.gdevelop.taskagile.utils.JsonUtils;
import de.gdevelop.taskagile.web.payload.RegistrationPayload;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { SecurityConfiguration.class, RegistrationApiController.class })
@WebMvcTest(RegistrationApiController.class)
public class RegistrationApiControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserService serviceMock;

  @Test
  public void registerBlankPayloadShouldFailAndReturn400() throws Exception {
    mvc.perform(post("/api/registrations")).andExpect(status().is(400));
  }

  @Test
  public void registerExistedUsernameShouldFailAndReturn400() throws Exception {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setUsername("exist");
    payload.setEmailAddress("test@taskagile.com");
    payload.setPassword("MyPassword!");

    doThrow(UsernameExistsException.class).when(serviceMock).register(payload.toCommand());

    mvc.perform(post("/api/registrations").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(payload)))
        .andExpect(status().is(400)).andExpect(jsonPath("$.message").value("Username already exists"));
  }

  @Test
  public void registerExistedEmailAddressShouldFailAndReturn400() throws Exception {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setUsername("test");
    payload.setEmailAddress("exist@taskagile.com");
    payload.setPassword("MyPassword!");

    doThrow(EmailAddressExistsException.class).when(serviceMock).register(payload.toCommand());

    mvc.perform(post("/api/registrations").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(payload)))
        .andExpect(status().is(400)).andExpect(jsonPath("$.message").value("Email address already exists"));

  }

  @Test
  public void registerValidPayloadShouldSucceedAndReturn201() throws Exception {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setUsername("sunny");
    payload.setEmailAddress("sunny@taskagile.com");
    payload.setPassword("MyPassword!");

    doNothing().when(serviceMock).register(payload.toCommand());

    mvc.perform(post("/api/registrations").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(payload)))
        .andExpect(status().is(201));
  }

}
