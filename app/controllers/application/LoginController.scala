package controllers.application

import models.user._
import org.slf4j.{Logger, LoggerFactory}
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import views._


/**
 * Created by afynn on 3/3/2015.
 */
object LoginController extends Controller {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Controller])

  val loginForm: Form[UserLogin] = Form(
    mapping(
      "email" -> nonEmptyText,
      "password" -> optional(text)
    )(UserLogin.apply)(UserLogin.unapply)
  )

  def index = Action {
    Ok(html.login(loginForm))
  }

  def submit = Action { implicit request =>
    logger.info("submit ..." + loginForm.bindFromRequest)

    loginForm.bindFromRequest.fold(
      errors => BadRequest(html.login(errors)),

      userLogin =>
        Users.findByEmail(userLogin.email) match  {
            case Some(user) if user.userType.name == UserTypes.ADMIN =>
              //TODO: send to admin page
              Ok(html.supervisor.supervisor(user))
            case Some(user) if user.userType.name == UserTypes.SUPERVISOR =>
              Ok(html.supervisor.supervisor(Users.findByEmail(userLogin.email).get))
            case Some(user) if user.userType.name == UserTypes.WORKER =>
              Ok(html.worker.worker(Users.findByEmail(userLogin.email).get))
            case _ =>
              Ok(html.login(loginForm))
        }
    )
  }

}
