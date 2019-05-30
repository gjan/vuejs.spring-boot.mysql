package de.gdevelop.taskagile.domain.common.event;

public interface DomainEventPublisher {
  void publish(DomainEvent event);
}
