package com.tri.service

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.tri.models._

object UserRegistry {

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(users: Set[User]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetUsers(replyTo) =>
        replyTo ! Users(users.toSeq)
        Behaviors.same
      case CreateUser(user, replyTo) =>
        replyTo ! ActionPerformed(s"User ${user.name} created.")
        registry(users + user)
      case GetUser(clientId, replyTo) =>
        replyTo ! GetUserResponse(users.find(_.clientId == clientId))
        Behaviors.same
      case DeleteUser(clientId, replyTo) =>
        replyTo ! ActionPerformed(s"User $clientId deleted.")
        registry(users.filterNot(_.clientId == clientId))
    }
}
