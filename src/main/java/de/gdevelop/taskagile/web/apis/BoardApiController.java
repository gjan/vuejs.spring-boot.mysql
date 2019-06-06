package de.gdevelop.taskagile.web.apis;

import de.gdevelop.taskagile.domain.application.BoardService;
import de.gdevelop.taskagile.domain.application.CardListService;
import de.gdevelop.taskagile.domain.application.CardService;
import de.gdevelop.taskagile.domain.application.TeamService;
import de.gdevelop.taskagile.domain.common.security.CurrentUser;
import de.gdevelop.taskagile.domain.model.board.Board;
import de.gdevelop.taskagile.domain.model.board.BoardId;
import de.gdevelop.taskagile.domain.model.card.Card;
import de.gdevelop.taskagile.domain.model.cardlist.CardList;
import de.gdevelop.taskagile.domain.model.team.Team;
import de.gdevelop.taskagile.domain.model.user.SimpleUser;
import de.gdevelop.taskagile.domain.model.user.User;
import de.gdevelop.taskagile.domain.model.user.UserNotFoundException;
import de.gdevelop.taskagile.web.payload.AddBoardMemberPayload;
import de.gdevelop.taskagile.web.payload.CreateBoardPayload;
import de.gdevelop.taskagile.web.results.ApiResult;
import de.gdevelop.taskagile.web.results.BoardResult;
import de.gdevelop.taskagile.web.results.CreateBoardResult;
import de.gdevelop.taskagile.web.results.Result;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class BoardApiController {

  private final static Logger log = LoggerFactory.getLogger(BoardApiController.class);

  private BoardService boardService;
  private TeamService teamService;
  private CardListService cardListService;
  private CardService cardService;

  public BoardApiController(BoardService boardService, TeamService teamService, CardListService cardListService,
      CardService cardService) {
    this.boardService = boardService;
    this.teamService = teamService;
    this.cardListService = cardListService;
    this.cardService = cardService;
  }

  @PostMapping("/api/boards")
  public ResponseEntity<ApiResult> createBoard(@RequestBody CreateBoardPayload payload,
      @CurrentUser SimpleUser currentUser) {
    Board board = boardService.createBoard(payload.toCommand(currentUser.getUserId()));
    log.info("Handling `{}` board creation api call", payload.toString());

    return CreateBoardResult.build(board);
  }

  @GetMapping("/api/boards/{boardId}")
  public ResponseEntity<ApiResult> getBoard(@PathVariable("boardId") long rawBoardId) {
    BoardId boardId = new BoardId(rawBoardId);
    Board board = boardService.findById(boardId);
    if (board == null) {
      return Result.notFound();
    }
    List<User> members = boardService.findMembers(boardId);

    Team team = null;
    if (!board.isPersonal()) {
      team = teamService.findById(board.getTeamId());
    }

    List<CardList> cardLists = cardListService.findByBoardId(boardId);
    List<Card> cards = cardService.findByBoardId(boardId);

    return BoardResult.build(team, board, members, cardLists, cards);
  }

  @PostMapping("/api/boards/{boardId}/members")
  public ResponseEntity<ApiResult> addMember(@PathVariable("boardId") long rawBoardId,
      @RequestBody AddBoardMemberPayload payload) {
    BoardId boardId = new BoardId(rawBoardId);
    Board board = boardService.findById(boardId);
    if (board == null) {
      return Result.notFound();
    }

    try {
      User member = boardService.addMember(boardId, payload.getUsernameOrEmailAddress());

      ApiResult apiResult = ApiResult.blank().add("id", member.getId().value()).add("shortName", member.getInitials());
      return Result.ok(apiResult);
    } catch (UserNotFoundException e) {
      return Result.failure("No user found");
    }
  }
}
