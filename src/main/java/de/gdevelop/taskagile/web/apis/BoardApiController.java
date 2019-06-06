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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class BoardApiController {

  private final static Logger log = LoggerFactory.getLogger(BoardApiController.class);

  private BoardService boardService;

  public BoardApiController(BoardService boardService) {
    this.boardService = boardService;
  }

  @PostMapping("/api/boards")
  public ResponseEntity<ApiResult> createBoard(@RequestBody CreateBoardPayload payload,
      @CurrentUser SimpleUser currentUser) {
    Board board = boardService.createBoard(payload.toCommand(currentUser.getUserId()));
    log.info("Handling `{}` board creation api call", payload.toString());

    return CreateBoardResult.build(board);
  }
}
