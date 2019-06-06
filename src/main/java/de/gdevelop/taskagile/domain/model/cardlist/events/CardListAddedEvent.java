package de.gdevelop.taskagile.domain.model.cardlist.events;

import de.gdevelop.taskagile.domain.common.event.DomainEvent;
import de.gdevelop.taskagile.domain.model.cardlist.CardList;

public class CardListAddedEvent extends DomainEvent {

  private static final long serialVersionUID = 26551114425630902L;

  private CardList cardList;

  public CardListAddedEvent(Object source, CardList cardList) {
    super(source);
    this.cardList = cardList;
  }

  public CardList getCardList() {
    return cardList;
  }
}
