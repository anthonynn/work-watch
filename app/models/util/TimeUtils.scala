package models.util

import org.joda.time.format.DateTimeFormat

/**
 * Created by afynn on 3/26/2015.
 */
class TimeUtils {

  val dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
  val timeFormatter = DateTimeFormat.forPattern("hh:mm:ss a");
}
