package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class ProjectsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def get(projectId: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(s"Project id: $projectId")
  }

}
