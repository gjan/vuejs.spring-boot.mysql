package de.gdevelop.taskagile.domain.application;

import de.gdevelop.taskagile.domain.application.BoardService;
import de.gdevelop.taskagile.domain.application.commands.CreateBoardCommand;
import de.gdevelop.taskagile.domain.common.event.DomainEventPublisher;
import de.gdevelop.taskagile.domain.model.board.Board;
import de.gdevelop.taskagile.domain.model.board.BoardManagement;
import de.gdevelop.taskagile.domain.model.board.BoardRepository;
import de.gdevelop.taskagile.domain.model.board.events.BoardCreatedEvent;
import de.gdevelop.taskagile.domain.model.user.UserId;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

  private BoardRepository boardRepository;
  private BoardManagement boardManagement;
  private DomainEventPublisher domainEventPublisher;

  public BoardServiceImpl(BoardRepository boardRepository, BoardManagement boardManagement,
      DomainEventPublisher domainEventPublisher) {
    this.boardRepository = boardRepository;
    this.boardManagement = boardManagement;
    this.domainEventPublisher = domainEventPublisher;
  }

  @Override
  public List<Board> findBoardsByMembership(UserId userId) {
    return boardRepository.findBoardsByMembership(userId);
  }

  @Override
  public Board createBoard(CreateBoardCommand command) {
    Board board = boardManagement.createBoard(command.getUserId(), command.getName(), command.getDescription(),
        command.getTeamId());
    domainEventPublisher.publish(new BoardCreatedEvent(this, board));
    return board;
  }
}
