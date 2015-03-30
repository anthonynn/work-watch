package controllers.application

import models.project.Projects
import models.user._
import org.slf4j.{LoggerFactory, Logger}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import views.html

/**
 * Created by afynn on 3/25/2015.
 */
object RegistrationController extends Controller {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Controller])

  val registerForm: Form[UserRegistration] = Form(
    mapping(
      "email" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "userType" -> nonEmptyText,
      "project" -> nonEmptyText,
      "password" -> optional(text)
    )(UserRegistration.apply)(UserRegistration.unapply)
  )

  def index = Action {
    Ok(html.register(registerForm))
  }


  def submit = Action { implicit request =>
    logger.info("submit ..." + registerForm.bindFromRequest)
    //logger.info("get from request ..." + registerForm.bindFromRequest.get)


    registerForm.bindFromRequest.fold(
      // Form has errors, redisplay it
      errors => BadRequest(html.register(errors)),

      // We got a valid User value, display the summary
      userLogin =>
        registerForm.bindFromRequest.get match {
          case _ =>
            addUser(registerForm.bindFromRequest.get)
            Ok(html.login(LoginController.loginForm))
        }
    )
  }

  def addUser (userReg: UserRegistration) = {
    val user =
      User(userReg.email, userReg.firstName, userReg.lastName, UserTypes.findById(userReg.userType).get, UserProfile("", Projects.findById(userReg.project).get))

    logger.info("user " + user)
    Users.add(user)
    logger.info("users " + Users.users)
  }

}
