package com.tri.service


import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.tri.models._
import org.slf4j.Logger
object PushMessageRegistry extends {

  def apply(): Behavior[PushMessageCommand] = registry(Set.empty)

  private def registry(pushMessages: Set[PushMessage]): Behavior[PushMessageCommand] =
    Behaviors.receiveMessage {
      case GetPushMessages(replyTo) =>
        replyTo ! PushMessages(pushMessages.toSeq)
        Behaviors.same
      case CreatePushMessage(pushMessage, replyTo) =>
        replyTo ! PushMessageActionPerformed(s"PushMessage ClientId : ${pushMessage.clientId} Message: ${pushMessage.message} Message: ${pushMessage.messageId} created.")
        MobilePushService.send(pushMessage.message)
        registry(pushMessages + pushMessage)
      case GetPushMessage(id, replyTo) =>
        replyTo ! GetPushMessageResponse(pushMessages.find(_.clientId == id))
        Behaviors.same
      case DeletePushMessage(id, replyTo) =>
        replyTo ! PushMessageActionPerformed(s"Push Message $id deleted.")
        registry(pushMessages.filterNot(_.clientId == id))
    }
}