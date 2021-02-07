package com.tri.service


import java.util.concurrent.ConcurrentHashMap

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.tri.models._
import scala.jdk.CollectionConverters._
object PushMessageRegistry {

  def apply(users : ConcurrentHashMap[Int,User],coordinator: ConcurrentHashMap[Int, List[Coordinator]]): Behavior[PushMessageCommand] = registry(Set.empty,users,coordinator)

  private def registry(pushMessages: Set[PushMessage],users : ConcurrentHashMap[Int,User],coordinator: ConcurrentHashMap[Int, List[Coordinator]]): Behavior[PushMessageCommand] =
    Behaviors.receiveMessage {
      case GetPushMessages(replyTo) =>
        replyTo ! PushMessages(pushMessages.toSeq)
        Behaviors.same
      case CreatePushMessage(pushMessage, replyTo) =>
        replyTo ! PushMessageActionPerformed(s"PushMessage ClientId : ${pushMessage.clientId} Message: ${pushMessage.message} Message: ${pushMessage.messageId} created.")
        val toClients:List[Int] = coordinator.get(pushMessage.clientId).map(_.toClientId)
        val distributionList = users.values().asScala.filter(u => toClients.contains(u.clientId))
        MobilePushService.send(pushMessage.message,distributionList.toList)
        registry(pushMessages + pushMessage,users,coordinator)
      case GetPushMessage(id, replyTo) =>
        replyTo ! GetPushMessageResponse(pushMessages.find(_.messageId == id))
        Behaviors.same
      case DeletePushMessage(id, replyTo) =>
        replyTo ! PushMessageActionPerformed(s"Push Message $id deleted.")
        registry(pushMessages.filterNot(_.messageId == id),users,coordinator)
    }
}