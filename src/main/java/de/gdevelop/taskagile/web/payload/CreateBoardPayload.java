package de.gdevelop.taskagile.web.payload;

import de.gdevelop.taskagile.domain.application.commands.CreateBoardCommand;
import de.gdevelop.taskagile.domain.model.team.TeamId;
import de.gdevelop.taskagile.domain.model.user.UserId;

public class CreateBoardPayload {

  private String name;
  private String description;
  private long teamId;

  public CreateBoardCommand toCommand(UserId userId) {
    return new CreateBoardCommand(userId, name, description, new TeamId(teamId));
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }

  @Override
  public String toString() {
    return "{" + " name='" + name + "'" + ", description='" + description + "'" + ", teamId='" + teamId + "'" + "}";
  }

}
