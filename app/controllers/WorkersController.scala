package controllers

import models.project.Projects
import models.user._
import play.api.mvc.{Action, Controller}
import views.html.worker

/**
 * Created by afynn on 3/7/2015.
 */
object WorkersController extends Controller {

  def index = Action {
    Ok(worker.worker(
      User("worker@ww.com", "Wor", "Kerr",
        UserTypes.findByName(UserTypes.WORKER).get,
        UserProfile("", Projects.findByName(Projects.WORK_WATCH).get))
    ));
  }

  def showWorker(user: User) = Action {
    Ok(worker.worker(user));
  }

  def get(projectId: String) = Action {
    val workers = Users.users //.getByType("worker")
    Ok(JsonFormats.getJson(workers))
  }

}
