package com.tri.server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.tri.routes.MobileMessageRoutes
import com.tri.service.{MessageRegistry, MobileMessageConsumerRegistry, PushMessageRegistry}

import scala.util.{Failure, Success}

//#main-class
object QuickstartApp {
  //#start-http-server
  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    import system.executionContext
    //Http().bindAndHandle(MainRouter.routes, config.getString("http.interface"), config.getInt("http.port"))
    val futureBinding = Http().newServerAt("0.0.0.0", 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
  //#start-http-server
  def main(args: Array[String]): Unit = {
    //#server-bootstrapping
    val rootBehavior = Behaviors.setup[Nothing] { context =>
      val mobileActor = context.spawn(MobileMessageConsumerRegistry(), "MobileRegistryActor")
      context.watch(mobileActor)
      val pushActor = context.spawn(PushMessageRegistry(), "PushRegistryActor")
      context.watch(pushActor)

      val routes = new MobileMessageRoutes(mobileActor,pushActor)(context.system)
      startHttpServer(routes.messageRoutes)(context.system)

      Behaviors.empty
    }
    val system = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
    //#server-bootstrapping
  }
}
//#main-class
