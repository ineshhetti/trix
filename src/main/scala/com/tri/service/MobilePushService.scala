package com.tri.service

import akka.actor.ActorSystem
import akka.event.Logging
import com.tri.models.User
import com.tri.util.Settings
//#imports
import akka.stream.alpakka.google.firebase.fcm.FcmNotificationModels._
import akka.stream.alpakka.google.firebase.fcm._
import akka.stream.alpakka.google.firebase.fcm.scaladsl.GoogleFcm

//#imports
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, Materializer}


object MobilePushService {
  implicit val system = ActorSystem()
  implicit val mat: Materializer = ActorMaterializer()

  def send(message: String, users: List[User]) = {
    users.map(user => {
      fire(message,user.name,user.token)
    })
  }

  private def fire(message: String,name:String, token: String): Unit = {
    val notification = FcmNotification(s"Message Test $name ..", message, Token("token"))
    Source
      .single(notification)
      .runWith(GoogleFcm.fireAndForget(Settings.fcmConfig))
  }
}
