package controllers

import models.activity._
import models.user.Users
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat}
import org.slf4j.{LoggerFactory, Logger}
import play.api.mvc.{Action, Controller}


/**
 * Created by afynn on 3/23/2015.
 */
object ActivitiesController extends Controller {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Controller])

  def get(id: String) = Action {
    logger.info("Getting activities for id - " + id)
    logger.info("activities - " + UserActivities(id))
    val activities = UserActivities(id)

    logger.info("activities for id - " + activities)
    Ok(JsonFormats.getJson(activities))
  }


  def post(id: String, lat: Double, long: Double) = Action {

    val user = Users.findByEmail(id).getOrElse(null)

    val activities = UserActivities(user, DateTime.now, Coordinate(lat, long))
    logger.info("activities for id - " + activities)
    Ok(JsonFormats.getJson(activities))
  }
}
