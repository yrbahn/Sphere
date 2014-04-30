
sealed trait Command {
  def userId : String
}

case class Login(userId:String) extends Command
case class LogOut(userId:String) extends Command
case class Timeout(userId:String) extends Command

sealed trait Event
case class GoldAdded(gold:Int) extends Event
