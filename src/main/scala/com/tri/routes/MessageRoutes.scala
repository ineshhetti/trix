package com.tri.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.tri.util.JsonFormats
import com.tri.models._
import scala.concurrent.Future

class MessageRoutes(messageRegistry: ActorRef[MessageCommand])(implicit val system: ActorSystem[_]) {

  //#message-routes-class
  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  //#import-json-formats

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getMessages(): Future[Messages] =
    messageRegistry.ask(GetMessages)
  def getMessage(name: String): Future[GetMessageResponse] =
    messageRegistry.ask(GetMessage(name, _))
  def createMessage(Message: Message): Future[MessageActionPerformed] =
    messageRegistry.ask(CreateMessage(Message, _))
  def deleteMessage(name: String): Future[MessageActionPerformed] =
    messageRegistry.ask(DeleteMessage(name, _))

  val messageRoutes: Route =
  pathPrefix("messages") {
    concat(
      //#messages-get-delete
      pathEnd {
        concat(
          get {
            complete(getMessages())
          },
          post {
            entity(as[Message]) { message =>
              onSuccess(createMessage(message)) { performed =>
                complete((StatusCodes.Created, performed))
              }
            }
          })
      },
      //#Messages-get-delete
      //#Messages-get-post
      path(Segment) { name =>
        concat(
          get {
            //#retrieve-Message-info
            rejectEmptyResponse {
              onSuccess(getMessage(name)) { response =>
                complete(response.maybeMessage)
              }
            }
            //#retrieve-Message-info
          },
          delete {
            //#Messages-delete-logic
            onSuccess(deleteMessage(name)) { performed =>
              complete((StatusCodes.OK, performed))
            }
            //#Messages-delete-logic
          })
      })
    //#Messages-get-delete
  }

  //#all-routes
}