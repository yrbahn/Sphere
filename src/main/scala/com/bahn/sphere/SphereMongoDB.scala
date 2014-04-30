import reactivemongo.api._
import scala.concurrent.ExecutionContext.Implicits.global

class SphereMongoDB(serverList:List[String]) {
  private val connection = (new MongoDriver).connection(serverList)
  val db = connection("sphere_db")


}


object SphereMongoDB {
  def apply(hosts:List[String]) =
    new SphereMongoDB(hosts)

}

