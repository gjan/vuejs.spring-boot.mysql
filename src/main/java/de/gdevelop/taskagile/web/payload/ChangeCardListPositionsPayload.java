package de.gdevelop.taskagile.web.payload;

import de.gdevelop.taskagile.domain.application.commands.ChangeCardListPositionsCommand;
import de.gdevelop.taskagile.domain.model.board.BoardId;
import de.gdevelop.taskagile.domain.model.cardlist.CardListPosition;

import java.util.List;

public class ChangeCardListPositionsPayload {

  private long boardId;
  private List<CardListPosition> cardListPositions;

  public ChangeCardListPositionsCommand toCommand() {
    return new ChangeCardListPositionsCommand(new BoardId(boardId), cardListPositions);
  }

  public void setBoardId(long boardId) {
    this.boardId = boardId;
  }

  public void setCardListPositions(List<CardListPosition> cardListPositions) {
    this.cardListPositions = cardListPositions;
  }
}
