import scala.concurrent.duration._
import akka.actor._
import akka.contrib.pattern.ShardRegion
import akka.contrib.pattern.ShardRegion.Passivate
import akka.persistence.EventsourcedProcessor
import akka.persistence.Persistence
import scala.collection.mutable.HashMap

/**
 * Companion Object for Session manager
 */
object SessionManager {

  def props : Props = Props[SessionManager]

  def shardName : String = "SessionManager"

  def idExtractor : ShardRegion.IdExtractor = {
    case cmd : Command =>
      (cmd.userId, cmd)
  }

  def shardResolver : ShardRegion.ShardResolver = {
    case cmd : Command =>
      (math.abs(cmd.userId.hashCode) % 100).toString()
  }

  private case class State(gold:Int) {
    def updated(evt:Event) : State = evt match {
      case GoldAdded(g) =>
        copy(gold=g)
    }
  }

}

/**
 * Session Manager
 */
class SessionManager extends Actor with ActorLogging{
  import SessionManager._

  private val users = HashMap[String, ActorRef]()

  override def receive: Receive = {
    case Login(u) =>
      log.info("get login" + u)
      if (! users.contains(u)){
        val session = context.actorOf(Props(new Session(u, self)))
        context.watch(session)

        users.put(u, session)
        session ! Login(u)
      }

    case Timeout(userId) =>
      log.info("timeout:"+userId)
      users.remove(userId)
      context.stop(sender)

    case evt : GoldAdded =>

    case Terminated(child) =>
      log.error(s"$child terminated")

    case e =>
      log.info("error: " + e.toString)
  }

}


/**
 * Session
 * @param user
 * @param sessionManager
 */
class Session(user:String, sessionManager:ActorRef) extends Actor with ActorLogging {

  context.setReceiveTimeout(30 seconds)

  def getUser = user

  def receive = {
    case ReceiveTimeout =>
      log.info("timeout")
      sessionManager ! Timeout(user)

    case _ =>
      log.info("rev")

  }
}