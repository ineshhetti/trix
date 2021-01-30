package com.tri.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.Future
import com.tri.models._

class   UserRoutes(userRegistry: ActorRef[Command])(implicit val system: ActorSystem[_]) {

  //#user-routes-class
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import com.tri.util.JsonFormats._
  //#import-json-formats

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getUsers(): Future[Users] =
    userRegistry.ask(GetUsers)
  def getUser(clientId: Int): Future[GetUserResponse] =
    userRegistry.ask(GetUser(clientId, _))
  def createUser(user: User): Future[ActionPerformed] =
    userRegistry.ask(CreateUser(user, _))
  def deleteUser(clientId: Int): Future[ActionPerformed] =
    userRegistry.ask(DeleteUser(clientId, _))

  //#all-routes
  //#users-get-post
  //#users-get-delete
  val userRoutes: Route =
    pathPrefix("users") {
      concat(
        //#users-get-delete
        pathEnd {
          concat(
            get {
              complete(getUsers())
            },
            post {
              entity(as[User]) { user =>
                onSuccess(createUser(user)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#users-get-delete
        //#users-get-post
        path(Segment) { clientId =>
          concat(
            get {
              //#retrieve-user-info
              rejectEmptyResponse {
                onSuccess(getUser(clientId.toInt)) { response =>
                  complete(response.maybeUser)
                }
              }
              //#retrieve-user-info
            },
            delete {
              //#users-delete-logic
              onSuccess(deleteUser(clientId.toInt)) { performed =>
                complete((StatusCodes.OK, performed))
              }
              //#users-delete-logic
            })
        })
      //#users-get-delete
    }

  //#all-routes
}
