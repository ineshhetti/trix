package com.tri.util

import com.tri.models._
import spray.json.DefaultJsonProtocol

object JsonFormats  {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)

  implicit val contextJsonFormat = jsonFormat1(Context)
  implicit val messageJsonFormat = jsonFormat3(Message)
  implicit val messagesJsonFormat = jsonFormat1(Messages)

  implicit val messageActionPerformedJsonFormat = jsonFormat1(MessageActionPerformed)

  implicit val mobileMessageJsonFormat = jsonFormat2(MobileMessage)
  implicit val mobileMessagesJsonFormat = jsonFormat1(MobileMessages)

  implicit val mobileMessageActionPerformedJsonFormat = jsonFormat1(MobileMessageActionPerformed)
}