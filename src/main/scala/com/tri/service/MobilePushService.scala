package com.tri.service
import akka.actor.ActorSystem
import com.tri.util.Settings
//#imports
import akka.stream.alpakka.google.firebase.fcm.FcmNotificationModels._
import akka.stream.alpakka.google.firebase.fcm.scaladsl.GoogleFcm
import akka.stream.alpakka.google.firebase.fcm._

//#imports
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, Materializer}


object MobilePushService {
  implicit val system = ActorSystem()
  implicit val mat: Materializer = ActorMaterializer()
  def send(message:String) = {
    val notification = FcmNotification("Message Test",message, Token("token"))
    Source
      .single(notification)
      .runWith(GoogleFcm.fireAndForget(Settings.fcmConfig))
  }
}
