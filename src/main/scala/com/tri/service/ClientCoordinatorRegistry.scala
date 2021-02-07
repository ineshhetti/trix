package com.tri.service

import java.util.concurrent.ConcurrentHashMap

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.tri.models._
import scala.jdk.CollectionConverters._

object ClientCoordinatorRegistry {
  def apply(coordinator: ConcurrentHashMap[Int, List[Coordinator]]): Behavior[CoordinatorCommand] = registry(Set.empty, coordinator)

  private def registry(messages: Set[Coordinator], coordinator: ConcurrentHashMap[Int, List[Coordinator]]): Behavior[CoordinatorCommand] =
    Behaviors.receiveMessage {
      case GetCoordinators(fromClientId, replyTo) =>
        replyTo ! Coordinators(messages.toSeq)
        Behaviors.same
      case CreateCoordinator(message, replyTo) =>
        replyTo ! CoordinatorActionPerformed(s"Coordinator for from Client Id ${message.fromClientId} to Client Id ${message.fromClientId} is created.")
        if (coordinator.get(message.fromClientId) == null || coordinator.get(message.fromClientId).isEmpty) {
          coordinator.put(message.fromClientId, List(message))
        } else {
          coordinator.put(message.fromClientId, coordinator.get(message.fromClientId) ++ List(message))
        }
        registry(messages + message, coordinator)
      case GetCoordinator(fromClientId, toClientId, replyTo) =>
        replyTo ! GetCoordinatorResponse(messages.find(m => m.fromClientId == fromClientId && m.toClientId == toClientId))
        Behaviors.same
      case DeleteCoordinator(fromClientId, toClientId, replyTo) =>
        replyTo ! CoordinatorActionPerformed(s"Coordinator from Client $fromClientId to to Client $toClientId is deleted.")
        if (coordinator.get(fromClientId) != null && coordinator.get(fromClientId).nonEmpty) {
          coordinator.put(fromClientId, coordinator.get(fromClientId).filterNot(_.toClientId == toClientId))
        }
        registry(messages.filterNot(m => m.fromClientId == fromClientId && m.toClientId == toClientId),coordinator)
    }


}
