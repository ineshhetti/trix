package com.tri.service

import akka.actor.ActorSystem
import akka.stream.Attributes
import akka.stream.scaladsl.Sink
import com.tri.models.User
import com.tri.util.Settings

import scala.collection.immutable
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.util.Try
//#imports
import akka.stream.alpakka.google.firebase.fcm.FcmNotificationModels._
import akka.stream.alpakka.google.firebase.fcm._
import akka.stream.alpakka.google.firebase.fcm.scaladsl.GoogleFcm

//#imports
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, Materializer}
import scala.language.postfixOps
import scala.util.{Try,Success,Failure}

object MobilePushService {
  implicit val system = ActorSystem()
  implicit val mat: Materializer = ActorMaterializer()

  def send(message: String, users: List[User]) = {
    users.map(user => {
      fire(message, user.name, user.token)
    })
  }

  private def fire(message: String, name: String, token: String): Unit = {
    val notification = FcmNotification(s"Message Test $name ..", message, Token(token))

      val result1: Future[immutable.Seq[FcmResponse]] =
        Source
          .single(notification)
          .via(GoogleFcm.send(Settings.fcmConfig))
          .map {
            case res@FcmSuccessResponse(name) =>
              println(s"Successful $name")
              res
            case res@FcmErrorResponse(errorMessage) =>
              println(s"Send error $errorMessage")
              res
          }
          .runWith(Sink.seq)
      Try(Await.result(result1, 30 seconds)) match{
        case Success(w) => {
          println(s"success")
        }
        case Failure(x) => {
          println(s"fail")
        }
      }


  }

  private def fire2(message: String, name: String, token: String): Unit = {
    val notification = FcmNotification(s"Message Test $name ..", message, Token(token))
    Source
      .single((notification, "superData"))
      .via(GoogleFcm.sendWithPassThrough(Settings.fcmConfig))
      .runWith(Sink.seq)
  }

  private def fire3(message: String, name: String, token: String): Unit = {
    val notification = FcmNotification(s"Message Test $name ..", message, Token(token))
    Source
      .single(notification)
      .runWith(GoogleFcm.fireAndForget(Settings.fcmConfig))
  }
}
