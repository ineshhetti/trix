package com.tri.models

import akka.actor.typed.ActorRef

import scala.collection.immutable

case class Models()
//push
final case class PushMessage(messageId: String,clientId: String,message: String)
final case class PushMessages(pushMessages: immutable.Seq[PushMessage])
sealed trait PushMessageCommand
final case class GetPushMessages(replyTo: ActorRef[PushMessages]) extends PushMessageCommand
final case class CreatePushMessage(pushMessage: PushMessage, replyTo: ActorRef[PushMessageActionPerformed]) extends PushMessageCommand
final case class GetPushMessage(id: String, replyTo: ActorRef[GetPushMessageResponse]) extends PushMessageCommand
final case class DeletePushMessage(id: String, replyTo: ActorRef[PushMessageActionPerformed]) extends PushMessageCommand
final case class GetPushMessageResponse(maybePushMessage: Option[PushMessage])
final case class PushMessageActionPerformed(description: String)
//Mobile
final case class MobileMessage(action: String,clientId: String)
final case class MobileMessages(mobileMessages: immutable.Seq[MobileMessage])
sealed trait MobileMessageCommand
final case class GetMobileMessages(replyTo: ActorRef[MobileMessages]) extends MobileMessageCommand
final case class CreateMobileMessage(mobileMessage: MobileMessage, replyTo: ActorRef[MobileMessageActionPerformed]) extends MobileMessageCommand
final case class GetMobileMessage(id: String, replyTo: ActorRef[GetMobileMessageResponse]) extends MobileMessageCommand
final case class GetMobileMessageResponse(maybeMobileMessage: Option[MobileMessage])
final case class MobileMessageActionPerformed(description: String)

//Message
final case class Context(requestId: String)
final case class Message(context: Context,id: String, body: String)
final case class Messages(messages: immutable.Seq[Message])
sealed trait MessageCommand
final case class GetMessages(replyTo: ActorRef[Messages]) extends MessageCommand
final case class CreateMessage(message: Message, replyTo: ActorRef[MessageActionPerformed]) extends MessageCommand
final case class GetMessage(id: String, replyTo: ActorRef[GetMessageResponse]) extends MessageCommand
final case class DeleteMessage(id: String, replyTo: ActorRef[MessageActionPerformed]) extends MessageCommand
final case class GetMessageResponse(maybeMessage: Option[Message])
final case class MessageActionPerformed(description: String)

//User
final case class User(name: String, age: Int, countryOfResidence: String)
final case class Users(users: immutable.Seq[User])
sealed trait Command
final case class GetUsers(replyTo: ActorRef[Users]) extends Command
final case class CreateUser(user: User, replyTo: ActorRef[ActionPerformed]) extends Command
final case class GetUser(name: String, replyTo: ActorRef[GetUserResponse]) extends Command
final case class DeleteUser(name: String, replyTo: ActorRef[ActionPerformed]) extends Command
final case class GetUserResponse(maybeUser: Option[User])
final case class ActionPerformed(description: String)
