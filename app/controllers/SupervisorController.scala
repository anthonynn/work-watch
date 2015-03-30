package controllers

import controllers.WorkersController._
import models.project.{Projects, Project}
import models.user.{UserTypes, UserType, UserProfile, User}
import play.api.mvc.{Action, Controller}
import views.html.supervisor

/**
 * Created by afynn on 3/24/2015.
 */
object SupervisorController extends Controller {

  def index = Action {
    Ok(supervisor.supervisor(
      User("supervisor@ww.com", "Super", "Visor",
        UserTypes.findByName(UserTypes.SUPERVISOR).get,
        UserProfile("", Projects.findByName(Projects.WORK_WATCH).get))
      )
    );
  }
}
