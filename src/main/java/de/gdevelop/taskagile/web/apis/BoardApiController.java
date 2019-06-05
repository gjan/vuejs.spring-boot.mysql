package de.gdevelop.taskagile.web.apis;

import de.gdevelop.taskagile.domain.application.BoardService;
import de.gdevelop.taskagile.domain.common.security.CurrentUser;
import de.gdevelop.taskagile.domain.model.board.Board;
import de.gdevelop.taskagile.domain.model.user.SimpleUser;
import de.gdevelop.taskagile.web.payload.CreateBoardPayload;
import de.gdevelop.taskagile.web.results.ApiResult;
import de.gdevelop.taskagile.web.results.CreateBoardResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class BoardApiController {

  private BoardService boardService;

  public BoardApiController(BoardService boardService) {
    this.boardService = boardService;
  }

  @PostMapping("/api/boards")
  public ResponseEntity<ApiResult> createBoard(@RequestBody CreateBoardPayload payload,
      @CurrentUser SimpleUser currentUser) {
    Board board = boardService.createBoard(payload.toCommand(currentUser.getUserId()));
    return CreateBoardResult.build(board);
  }
}
