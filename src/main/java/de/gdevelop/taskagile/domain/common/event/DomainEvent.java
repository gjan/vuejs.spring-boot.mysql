package de.gdevelop.taskagile.domain.common.event;

import org.springframework.context.ApplicationEvent;

public abstract class DomainEvent extends ApplicationEvent {

  private static final long serialVersionUID = 25024256305234652l;

  public DomainEvent(Object source) {
    super(source);
  }

  public long occurredAt() {
    // Return the underlying implementation's timestamp
    return getTimestamp();
  }
}
