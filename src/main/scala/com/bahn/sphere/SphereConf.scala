import scala.collection.JavaConversions._
import com.typesafe.config.Config

case class SphereConf(host:String, port:Int, mongoDBConf:MongoDBConf)
case class MongoDBConf(hosts:List[String], user:Option[String] = None, pass:Option[String] = None)

object SphereConf{
  def apply(config:Config) : SphereConf =
    SphereConf(config.getString("sphere.host"), config.getInt("sphere.port"),
      MongoDBConf(config.getStringList("sphere.mongo.hosts").toList))
}