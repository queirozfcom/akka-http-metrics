package backline.http.metrics
import com.codahale.metrics._
import akka.http.scaladsl.server.Directive0
import scala.util.control.NonFatal

trait HttpMeterMetrics extends MetricsBase {
  def meterDirective: Directive0 = {
    mapInnerRoute { inner => ctx =>
      findAndRegisterMeter(getMetricName(ctx)).mark
      try {
        inner(ctx)
      } catch {
        case NonFatal(err) =>
          ctx.fail(err)
      }
    }
  }
}
