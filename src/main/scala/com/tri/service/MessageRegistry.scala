package com.tri.service

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.tri.models._

object MessageRegistry {

  def apply(): Behavior[MessageCommand] = registry(Set.empty)

  private def registry(messages: Set[Message]): Behavior[MessageCommand] =
    Behaviors.receiveMessage {
      case GetMessages(replyTo) =>
        replyTo ! Messages(messages.toSeq)
        Behaviors.same
      case CreateMessage(message, replyTo) =>
        replyTo ! MessageActionPerformed(s"Message ${message.id} created.")
        registry(messages + message)
      case GetMessage(id, replyTo) =>
        replyTo ! GetMessageResponse(messages.find(_.id == id))
        Behaviors.same
      case DeleteMessage(id, replyTo) =>
        replyTo ! MessageActionPerformed(s"Message $id deleted.")
        registry(messages.filterNot(_.id == id))
      case DeleteMessages(replyTo) =>
        replyTo ! MessageActionPerformed(s"All Messagea are deleted.")
        registry(Set.empty[Message])
    }
}
