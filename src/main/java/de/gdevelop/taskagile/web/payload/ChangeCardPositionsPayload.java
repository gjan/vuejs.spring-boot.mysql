package de.gdevelop.taskagile.web.payload;

import de.gdevelop.taskagile.domain.application.commands.ChangeCardPositionsCommand;
import de.gdevelop.taskagile.domain.model.board.BoardId;
import de.gdevelop.taskagile.domain.model.card.CardPosition;

import java.util.List;

public class ChangeCardPositionsPayload {

  private long boardId;
  private List<CardPosition> cardPositions;

  public ChangeCardPositionsCommand toCommand() {
    return new ChangeCardPositionsCommand(new BoardId(boardId), cardPositions);
  }

  public void setBoardId(long boardId) {
    this.boardId = boardId;
  }

  public void setCardPositions(List<CardPosition> cardPositions) {
    this.cardPositions = cardPositions;
  }
}
