package com.tri.service

import java.util.concurrent.ConcurrentHashMap

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.tri.models._
import scala.jdk.CollectionConverters._
object UserRegistry {

  def apply( users : ConcurrentHashMap[Int,User]): Behavior[Command] = registry(users)

  //

  private def registry(users : ConcurrentHashMap[Int,User]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetUsers(replyTo) =>
        replyTo ! Users(users.values().asScala.toSeq)
        Behaviors.same
      case CreateUser(user, replyTo) =>
        replyTo ! ActionPerformed(s"User ${user.name} created.")
        users.put(user.clientId,user)
        Behaviors.same
      case GetUser(clientId, replyTo) =>
        val user = users.asScala.toMap.get(clientId)
        replyTo ! GetUserResponse(user)
        Behaviors.same
      case DeleteUser(clientId, replyTo) =>
        replyTo ! ActionPerformed(s"User $clientId deleted.")
        users.remove(clientId)
        Behaviors.same
    }
}
