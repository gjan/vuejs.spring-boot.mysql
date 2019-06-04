package de.gdevelop.taskagile.domain.common.mail;

import freemarker.template.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DefaultMailManagerTests {

  @TestConfiguration
  static class DefaultMessageCreatorConfiguration {
    @Bean
    public FreeMarkerConfigurationFactoryBean getFreemarkerConfiguration() {
      FreeMarkerConfigurationFactoryBean factoryBean = new FreeMarkerConfigurationFactoryBean();
      factoryBean.setTemplateLoaderPath("/mail-templates/");
      return factoryBean;
    }
  }

  @Autowired
  private Configuration configuration;
  private Mailer mailerMock;
  private DefaultMailManager instance;

  @Before
  public void setUp() {
    mailerMock = mock(Mailer.class);
    instance = new DefaultMailManager("noreply@taskagile.com", mailerMock, configuration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendNullEmailAddressShouldFail() {
    instance.send(null, "Test subject", "test.ftl");
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendEmptyEmailAddressShouldFail() {
    instance.send("", "Test subject", "test.ftl");
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendNullSubjectShouldFail() {
    instance.send("test@taskagile.com", null, "test.ftl");
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendEmptySubjectShouldFail() {
    instance.send("test@taskagile.com", "", "test.ftl");
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendNullTemplateNameShouldFail() {
    instance.send("test@taskagile.com", "Test subject", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendEmptyTemplateNameShouldFail() {
    instance.send("test@taskagile.com", "Test subject", "");
  }

  @Test
  public void sendValidParametersShouldSucceed() {
    String to = "user@example.com";
    String subject = "Test subject";
    String templateName = "test.ftl";

    instance.send(to, subject, templateName, MessageVariable.from("name", "test"));
    ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
    verify(mailerMock).send(messageArgumentCaptor.capture());

    Message messageSent = messageArgumentCaptor.getValue();
    assertEquals(to, messageSent.getTo());
    assertEquals(subject, messageSent.getSubject());
    assertEquals("noreply@taskagile.com", messageSent.getFrom());
    assertEquals("Hello, test", messageSent.getBody().trim());
  }
}
