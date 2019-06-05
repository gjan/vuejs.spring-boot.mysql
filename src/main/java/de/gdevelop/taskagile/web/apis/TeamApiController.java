package de.gdevelop.taskagile.web.apis;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import de.gdevelop.taskagile.domain.application.TeamService;
import de.gdevelop.taskagile.domain.common.security.CurrentUser;
import de.gdevelop.taskagile.domain.model.team.Team;
import de.gdevelop.taskagile.domain.model.user.SimpleUser;
import de.gdevelop.taskagile.web.payload.CreateTeamPayload;
import de.gdevelop.taskagile.web.results.ApiResult;
import de.gdevelop.taskagile.web.results.CreateTeamResult;

@Controller
public class TeamApiController {

  private TeamService teamService;

  public TeamApiController(TeamService teamService) {
    this.teamService = teamService;
  }

  @PostMapping("/api/teams")
  public ResponseEntity<ApiResult> createTeam(@RequestBody CreateTeamPayload payload,
      @CurrentUser SimpleUser currentUser) {
    Team team = teamService.createTeam(payload.toCommand(currentUser.getUserId()));
    return CreateTeamResult.build(team);
  }
}
