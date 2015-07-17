package backline.http.metrics
import io.dropwizard.metrics._
import akka.http.scaladsl.server.{Directive0, RouteResult}
import akka.http.scaladsl.model.{StatusCodes, StatusCode}
import scala.util.control.NonFatal
import scala.concurrent.ExecutionContext

trait ResponseCodeMetrics extends MetricsBase {
  def responseCodeMetrics(implicit ec: ExecutionContext): Directive0 = {
    mapInnerRoute { inner => ctx =>
      try {
        val fut = inner(ctx)
        fut foreach {
          case RouteResult.Complete(resp) =>
            findAndRegisterCounter(s"${getMetricName(ctx)}-${liftStatusCode(resp.status)}").inc
          case RouteResult.Rejected(_) =>
            findAndRegisterCounter(s"${getMetricName(ctx)}-rejections").inc
        }
        fut
      } catch {
        case NonFatal(err) =>
          findAndRegisterCounter(s"${getMetricName(ctx)}-failures").inc
          ctx.fail(err)
      }
    }
  }

  private[this] def liftStatusCode(code: StatusCode): String = code match {
    case c: StatusCodes.Informational         => "1xx"
    case c: StatusCodes.Success               => "2xx"
    case c: StatusCodes.Redirection           => "3xx"
    case c: StatusCodes.ClientError           => "4xx"
    case c: StatusCodes.ServerError           => "5xx"
    case StatusCodes.CustomStatusCode(custom) => custom.toString
  }
}
