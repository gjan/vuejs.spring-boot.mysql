package de.gdevelop.taskagile.domain.model.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import de.gdevelop.taskagile.domain.model.board.events.BoardCreatedEvent;

@Component
public class BoardCreatedEventHandler {

  private final static Logger log = LoggerFactory.getLogger(BoardCreatedEventHandler.class);

  @EventListener(BoardCreatedEvent.class)
  public void handleEvent(BoardCreatedEvent event) {
    log.info("Handling `{}` board creation event", event.getBoard().toString());
    // This is only a demonstration of the domain event listener
  }

}
