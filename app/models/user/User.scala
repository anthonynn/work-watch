package models.user

import models.project.{Projects, Project}

/**
 * Created by afynn on 3/2/2015.
 */

case class User(
                 email: String,
                 firstName: String,
                 lastName: String,
                 userType: UserType,
                 userProfile: UserProfile
                 )


case class UserProfile(
                        id: String,
                        project: Project
                        )

case class UserLogin(
                        email: String,
                        password: Option[String]
                        )

case class UserRegistration(
                 email: String,
                 firstName: String,
                 lastName: String,
                 userType: String,
                 project: String,
                 password: Option[String]
                 )
object Users {
  val emptyUser = User("", "", "", UserType("", ""), UserProfile("", Project("", "")))
  var users = Set(
    User("admin@ww.com", "admin", "admin", UserTypes.findByName(UserTypes.ADMIN).get, UserProfile("", Projects.findByName(Projects.WORK_WATCH).get)),
    User("anthony.fynn@ww.com", "Anthony", "Fynn", UserTypes.findByName(UserTypes.WORKER).get, UserProfile("", Projects.findByName(Projects.WORK_WATCH).get))
  )

  def add (user : User)  {
    users = users + user
  }

  def findByEmail(email: String) = users.find(_.email == email)

  def getByType(userType: String) =
    for{
      user <- users
      if user.userType == userType
    } yield user

}


case class UserType (id: String, name: String)

object UserTypes {
  val ADMIN = "Admin"
  val SUPERVISOR = "Supervisor"
  val WORKER = "Worker"

  val userTypes = Seq(UserType("1", ADMIN), UserType("2",SUPERVISOR), UserType("3",WORKER))

  def findById(id: String) = userTypes.find(_.id == id)
  def findByName(name: String) = userTypes.find(_.name == name)

  def options =
    for {
      userType <- userTypes
    } yield (userType.id, userType.name)
}

object JsonFormats {

  import play.api.libs.json._

  implicit val userTypeWrites = new Writes[UserType] {
    def writes(userType: UserType) = Json.obj(
      "id" -> userType.id,
      "name" -> userType.name
    )
  }

  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "email" -> user.email,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "project" -> user.userProfile.project.name,
      "userType" -> Json.toJson(user.userType)
    )
  }

  def getJson(users: Set[User]) = Json.toJson(users)
}
