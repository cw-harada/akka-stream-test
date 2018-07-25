import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import scalikejdbc.ConnectionPool

import scala.concurrent.Future


object Main extends App {

  implicit val system = ActorSystem("simple-stream")
  implicit val materializer = ActorMaterializer()

  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.singleton("jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8&useSSL=false", "root", "")

  val source: Source[User, NotUsed] = Source.fromPublisher(User.streamAll())

  /*
  def convertToCSVLine(u: User): immutable.Iterable[String] = immutable.Seq(u.id.toString, u.name, u.age.toString)
  val flow = Flow[User].map(convertToCSVLine).via(CsvFormatting.format(charset = Charset.forName("UTF-8")))
  val csvOutputPath = Paths.get("./output.csv")
  val sink =  FileIO.toPath(csvOutputPath)
*/

  val flow: Flow[User, User, NotUsed] = Flow[User].map(_.copy(age = 5))

  val sink: Sink[User, Future[Done]] = Sink.foreach[User](NewUser.insertAll(_))

  val runnableGraph: RunnableGraph[Future[Done]] = source.via(flow).toMat(sink)(Keep.right)

  val result = runnableGraph.run()

  implicit val executionContext = system.dispatcher

  result.onComplete(_ => system.terminate())


}