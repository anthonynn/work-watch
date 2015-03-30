package models.activity

import models.user.{Users, User}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.slf4j.{LoggerFactory, Logger}
import scala.collection.mutable.{Seq => MutableSeq}
import play.api.libs.json.Json

/**
 * Created by afynn on 3/23/2015.
 */
case class Activity (
                      id: String,
                      checkIn: Space,
                      checkOut: Space
                      )

case class Space(
                  coordinate: Coordinate,
                  time: DateTime
                  )

case class Coordinate(
                       latitude: Double,
                       longitude: Double
                       )


/*
object Activities {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Activity])

  var activities : Seq[Activity] = Seq(  )

  def apply(i: Int): Unit = {
    activities(i)
  }

  def add (activity : Activity): Seq[Activity] = {
    activities = activities :+ activity
    activities
  }

  def getById(id: String) =
    activities.filter(_.id == id)


  def apply(id: String, dateTime: DateTime, coordinates: Coordinate) : Unit = {

    val userActivities = getById(id)
    logger.info("userActivities - " + userActivities)

    if(userActivities.nonEmpty && userActivities.last != null && userActivities.last.checkOut == null){
      val lastActivity = userActivities.last
      activities = activities.dropWhile(_ == lastActivity) :+ lastActivity.copy(checkOut = Space(coordinates, dateTime))
    }
    else {
      val activity = Activity(id, Space(coordinates, dateTime), null)
      add(activity)
    }

    logger.info("activities - " + activities)
  }
}

object UserActivities {
  def userActivitiesMap: Map[String, Seq[Activity]] = Activities.activities.groupBy(_.id)

  def activities = userActivitiesMap.values.flatten

}*/



object UserActivities {

  var userActivities: Map[String, Seq[Activity]] = Map()

  def apply (user: User) : Seq[Activity] = {
    userActivities.getOrElse(user.email, Seq[Activity]())
  }

  def apply (id: String) : Seq[Activity] = {
    userActivities.getOrElse(id, Seq[Activity]())
  }

  def apply (user: User, activity : Activity) = {
    val activities = userActivities.getOrElse(user.email, Seq[Activity]()) :+ activity
    userActivities = userActivities + (user.email -> activities)
  }


  def apply(user: User, dateTime: DateTime, coordinates: Coordinate) : Seq[Activity] = {

    val activities = userActivities.getOrElse(user.email, Seq[Activity]())

    if(activities.nonEmpty && activities.last != null && activities.last.checkOut == null){
      val lastActivity = activities.last
      val newActivities = activities.dropRight(1) :+ lastActivity.copy(checkOut = Space(coordinates, dateTime))
      userActivities = userActivities + (user.email -> newActivities)
    }
    else {
      val activity = Activity(user.email, Space(coordinates, dateTime), null)
      apply(user, activity)
    }

    apply(user)
  }
}

object JsonFormats {

  import play.api.libs.json._

  val dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
  val timeFormatter = DateTimeFormat.forPattern("hh:mm:ss a");

  implicit val coordinateWrites = new Writes[Coordinate] {
    def writes(coordinate: Coordinate) = Json.obj(
      "lat" -> coordinate.latitude,
      "long" -> coordinate.longitude
    )
  }

  implicit val spaceWrites = new Writes[Space] {
    def writes(space: Space) =
      space match {
        case null => JsNull
        case _ =>  Json.obj(
          "coordinate" -> space.coordinate,
          "date" -> dateFormatter.print (space.time),
          "time" -> timeFormatter.print (space.time)
        )
      }
  }

  implicit val activityWrites = new Writes[Activity] {
    def writes (activity: Activity) = Json.obj(
      "in" -> activity.checkIn,
      "out" -> activity.checkOut
    )
  }

  def getJson(activities: Seq[Activity]) = Json.toJson(activities)
}