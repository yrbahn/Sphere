import scala.concurrent.duration._
import akka.io.IO
import akka.actor._
import akka.contrib.pattern.ClusterSharding

import scala.Some
import spray.can.Http
import spray.http._
import spray.http.HttpMethods._
import spray.routing._

import com.typesafe.config.{Config, ConfigFactory}
import spray.http.HttpRequest
import spray.http.HttpResponse

object SphereMain extends App {
  private val config = ConfigFactory.load()
  private val system = ActorSystem("ClusterSystem", config)

  private val sphereConfig = SphereConf(config)
  private val mongoDB      = SphereMongoDB(sphereConfig.mongoDBConf.hosts)

  def getConfig  = sphereConfig
  def getMongoDB = mongoDB

  override def main(args:Array[String]): Unit = {

    ClusterSharding(system).start(
      typeName = SessionManager.shardName,
      entryProps = Some(SessionManager.props),
      idExtractor = SessionManager.idExtractor,
      shardResolver = SessionManager.shardResolver
    )

    val service = system.actorOf(Props[SphereServiceActor], name ="sphere-service")

    IO(Http)(system) ! Http.Bind(service, interface = sphereConfig.host, port = sphereConfig.port)

  }
}


class SphereServiceActor extends Actor  with SphereService with ActorLogging {

  def actorRefFactory = context


  def receive = runRoute(sphereRoute)

  /*
  def receive = {
    case _ : Http.Connected =>
      sender ! Http.Register(self)

    case u @ HttpRequest(GET, Uri.Path("/sphere"), _, _, _) =>
      log.info("sphere get!!!")
      sessionMgrRegion ! Login("yrbahn")
      log.info("debug")
      sender() ! HttpResponse(entity = "PONG")

    case _ =>
      sender() ! HttpResponse(entity= "NOT MATCHED")
  }*/

}

trait SphereService extends HttpService { this : Actor =>

  implicit def executionContext = actorRefFactory.dispatcher

  val sessionMgrRegion : ActorRef =
    ClusterSharding(context.system).shardRegion(SessionManager.shardName)

  val sphereRoute  = {
    path("sphere"){
      get {
        sessionMgrRegion ! Login("yrbahn")
        complete("test")
      }
    } ~ path ("join"){
      put {
        complete("")
      }
    }
  }

  private val joinRoute = {
    //entity(as[User])
  }
}
