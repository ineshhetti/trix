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

import scala.concurrent.Future

class MobileMessageRoutes( mobileMessageRegistry: ActorRef[MobileMessageCommand])(implicit val system: ActorSystem[_]) {

  //#message-routes-class
  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  //#import-json-formats

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getMobileMessages(): Future[MobileMessages] =
    mobileMessageRegistry.ask(GetMobileMessages)
  def getMobileMessage(id: String): Future[GetMobileMessageResponse] =
    mobileMessageRegistry.ask(GetMobileMessage(id, _))
  def createMobileMessage(mobileMessage: MobileMessage): Future[MobileMessageActionPerformed] =
    mobileMessageRegistry.ask(CreateMobileMessage(mobileMessage, _))
  def deleteMobileMessage(id: String): Future[MobileMessageActionPerformed] =
    mobileMessageRegistry.ask(DeleteMobileMessage(id, _))

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
        //#Messages-get-delete
        //#Messages-get-post
        path(Segment) { id  =>
          concat(
            get {
              //#retrieve-Message-info
              rejectEmptyResponse {
                onSuccess(getMobileMessage(id)) { response =>
                  complete(response.maybeMobileMessage)
                }
              }
              //#retrieve-Message-info
            },
            delete {
              //#Messages-delete-logic
              onSuccess(deleteMobileMessage(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
              //#Messages-delete-logic
            })
        })
      //#Messages-get-delete
    }

  //#all-routes
}
