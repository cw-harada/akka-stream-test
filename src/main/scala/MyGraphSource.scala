import akka.stream._
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}

class MyGraphSource extends GraphStage[SourceShape[Int]] {
  val out: Outlet[Int] = Outlet("NumberSource")
  override val shape: SourceShape[Int] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {

      private var counter = 1

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          if (counter < 10) {
            push(out, counter)
            counter += 1
          }
        }
      })
    }
}
