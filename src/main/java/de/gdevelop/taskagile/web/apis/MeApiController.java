package de.gdevelop.taskagile.web.apis;

import de.gdevelop.taskagile.domain.application.BoardService;
import de.gdevelop.taskagile.domain.application.TeamService;
import de.gdevelop.taskagile.domain.common.security.CurrentUser;
import de.gdevelop.taskagile.domain.model.board.Board;
import de.gdevelop.taskagile.domain.model.team.Team;
import de.gdevelop.taskagile.domain.model.user.SimpleUser;
import de.gdevelop.taskagile.web.results.ApiResult;
import de.gdevelop.taskagile.web.results.MyDataResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MeApiController {

  private TeamService teamService;
  private BoardService boardService;

  public MeApiController(TeamService teamService, BoardService boardService) {
    this.teamService = teamService;
    this.boardService = boardService;
  }

  @GetMapping("/api/me")
  public ResponseEntity<ApiResult> getMyData(@CurrentUser SimpleUser currentUser) {
    List<Team> teams = teamService.findTeamsByUserId(currentUser.getUserId());
    List<Board> boards = boardService.findBoardsByMembership(currentUser.getUserId());
    return MyDataResult.build(currentUser, teams, boards);
  }
}
