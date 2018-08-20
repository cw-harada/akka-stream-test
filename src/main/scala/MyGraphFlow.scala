import akka.stream._
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}


class MyGraphFlow extends GraphStage[FlowShape[Int, Int]] {
  val in: Inlet[Int] = Inlet("NumberIn")
  val out: Outlet[Int] = Outlet("NumberOut")
  override val shape: FlowShape[Int, Int] = FlowShape.of(in, out)


  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          push(out, grab(in) * 10)
        }
      })

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          pull(in)
        }
      })
    }
}
