import akka.stream._
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler}


class MyGraphSink extends GraphStage[SinkShape[Int]] {
  val in: Inlet[Int] = Inlet("NumberSink")
  override val shape: SinkShape[Int] = SinkShape(in)


  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {

      override def preStart(): Unit = pull(in)

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          println(grab(in))
          pull(in)
        }
      })
    }
}
