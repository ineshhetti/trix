package com.tri.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.tri.models._
import com.tri.util.JsonFormats

import scala.concurrent.Future

class MobileMessageRoutes(mobileMessageRegistry: ActorRef[MobileMessageCommand], pushMessageRegistry: ActorRef[PushMessageCommand], userRegistry: ActorRef[Command])(implicit val system: ActorSystem[_]) {

  //#message-routes-class

  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  //#import-json-formats

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getMobileMessages(): Future[MobileMessages] =
    mobileMessageRegistry.ask(GetMobileMessages)

  def getMobileMessage(id: Int): Future[GetMobileMessageResponse] =
    mobileMessageRegistry.ask(GetMobileMessage(id, _))

  def createMobileMessage(mobileMessage: MobileMessage): Future[MobileMessageActionPerformed] =
    mobileMessageRegistry.ask(CreateMobileMessage(mobileMessage, _))

  def getPushMessages(): Future[PushMessages] =
    pushMessageRegistry.ask(GetPushMessages)

  def createPushMessage(pushMessage: PushMessage): Future[PushMessageActionPerformed] =
    pushMessageRegistry.ask(CreatePushMessage(pushMessage, _))

  def getUsers(): Future[Users] =
    userRegistry.ask(GetUsers)

  def getUser(id: Int): Future[GetUserResponse] =
    userRegistry.ask(GetUser(id, _))

  def createUser(user: User): Future[ActionPerformed] =
    userRegistry.ask(CreateUser(user, _))

  def deleteUser(clientId: Int): Future[ActionPerformed] =
    userRegistry.ask(DeleteUser(clientId, _))

  val messageRoutes: Route =
    pathPrefix("messages") {
      concat(
        //#messages-get-delete
        pathEnd {
          concat(
            get {
              complete(getMobileMessages())
            },
            post {
              entity(as[MobileMessage]) { message =>
                onSuccess(createMobileMessage(message)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getMobileMessage(id.toInt)) { response =>
                  complete(response.maybeMobileMessage)
                }
              }
            })
        })
    } ~ path("push") {
      pathEnd {
        concat(
          get {
            complete(getPushMessages())
          }
          ,
          post {
            entity(as[PushMessage]) { message =>
              onSuccess(createPushMessage(message)) { performed =>
                complete((StatusCodes.Created, performed))
              }
            }
          })
      }
    } ~ path("users") {
      //pathEnd {
        concat(
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
          path(Segment) { clientId =>

              concat(
                get {
                  rejectEmptyResponse {
                    onSuccess(getUser(clientId.toInt)) { response =>
                      complete(response.maybeUser)
                    }
                  }
                },
                delete {
                  onSuccess(deleteUser(clientId.toInt)) { performed =>
                    complete((StatusCodes.OK, performed))
                  }
                }
              )

          }
        )
    //  }
    }
}