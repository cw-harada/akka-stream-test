import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{RunnableGraph, Source}

object GraphMain extends App {

  implicit val system = ActorSystem("simple-stream")
  implicit val materializer = ActorMaterializer()

  val mySource: Graph[SourceShape[Int], NotUsed] = new MyGraphSource

  val mySink: Graph[SinkShape[Int], NotUsed] = new MyGraphSink

  val myFlow: Graph[FlowShape[Int, Int], NotUsed] = new MyGraphFlow

  val source: Source[Int, NotUsed] = Source.fromGraph(mySource)

  val runnableGraph: RunnableGraph[NotUsed] = source.via(myFlow).to(mySink)

  val result = runnableGraph.run()

  implicit val executionContext = system.dispatcher

  system.terminate()
}
