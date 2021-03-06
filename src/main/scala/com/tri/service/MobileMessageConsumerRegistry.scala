package com.tri.service

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.tri.models._
import com.tri.service.UserRegistry.registry


object MobileMessageConsumerRegistry {

  def apply(): Behavior[MobileMessageCommand] = registry(Set.empty)

  private def registry(mobileMessages: Set[MobileMessage]): Behavior[MobileMessageCommand] =
    Behaviors.receiveMessage {
      case GetMobileMessages(replyTo) =>
        replyTo ! MobileMessages(mobileMessages.toSeq)
        Behaviors.same
      case CreateMobileMessage(mobileMessage, replyTo) =>
        replyTo ! MobileMessageActionPerformed(s"MobileMessage ID : ${mobileMessage.clientId} Action: ${mobileMessage.action} created.")
        registry(mobileMessages.filterNot(_.clientId == mobileMessage.clientId) + mobileMessage)
      case GetMobileMessage(id, replyTo) =>
        replyTo ! GetMobileMessageResponse(mobileMessages.find(_.id == id))
        Behaviors.same
      case DeleteMobileMessage(id, replyTo) =>
        replyTo ! MobileMessageActionPerformed(s"MobileMessage $id deleted.")
        registry(mobileMessages.filterNot(_.clientId == id))
    }
}
