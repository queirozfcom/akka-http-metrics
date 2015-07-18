package backline.http.metrics
import com.codahale.metrics._
import akka.http.scaladsl.server.Directive0
import scala.util.control.NonFatal

trait HttpTimerMetrics extends MetricsBase {
  def timerDirective: Directive0 = {
    mapInnerRoute { inner => ctx =>
      val timer = findAndRegisterTimer(getMetricName(ctx)).time()
      try {
        inner(ctx)
      } catch {
        case NonFatal(err) =>
          ctx.fail(err)
      } finally {
        timer.stop()
      }
    }
  }
}
