package models.project

/**
 * Created by afynn on 3/26/2015.
 */
case class Project (id : String, name: String)

object Projects {
  val WORK_WATCH = "Work Watch"

  var projects = Seq(
    Project("1", WORK_WATCH)
  )

  def options =
    for {
      project <- projects
    } yield (project.id, project.name)

  def findById(id: String) = projects.find(_.id == id)
  def findByName(name: String) = projects.find(_.name == name)
}