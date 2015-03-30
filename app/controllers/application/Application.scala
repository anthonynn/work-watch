package controllers.application

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.login(null))
  }

}