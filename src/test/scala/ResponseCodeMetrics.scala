package backline.http.metrics
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.model.StatusCodes
import io.dropwizard.metrics.MetricRegistry
import scala.concurrent.ExecutionContext

object ResponseCodeMetricsSpec extends RouteSpecification with ResponseCodeMetrics with Directives {
  val metricRegistry = new MetricRegistry()

  "count up for successful requests" in {
    (1 to 10) foreach { _ =>
      Get("/ok") ~> routes ~> check {
        status === StatusCodes.OK
      }
    }
    val counts = metricRegistry.counter("ok.GET-2xx")
    counts.getCount() must be_==(10)
  }

  "count up for redirecting requests" in {
    (1 to 10) foreach { _ =>
      Get("/redirect") ~> routes ~> check {
        status === StatusCodes.Found
      }
    }
    val counts = metricRegistry.counter("redirect.GET-3xx")
    counts.getCount() must be_==(10)
  }

  "count up for bad requests" in {
    (1 to 10) foreach { _ =>
      Get("/bad") ~> routes ~> check {
        status === StatusCodes.BadRequest
      }
    }
    val counts = metricRegistry.counter("bad.GET-4xx")
    counts.getCount() must be_==(10)
  }

  "count up for failing requests" in {
    (1 to 10) foreach { _ =>
      Get("/fail") ~> routes ~> check {
        status === StatusCodes.InternalServerError
      }
    }
    val counts = metricRegistry.counter("fail.GET-5xx")
    counts.getCount() must be_==(10)
  }

  def routes =
    responseCodeMetrics(ExecutionContext.global) {
      (get & path("ok")) {
        complete(StatusCodes.OK)
      } ~
      (get & path("redirect")) {
        redirect("/other", StatusCodes.Found)
      } ~
      (get & path("bad")) {
        complete(StatusCodes.BadRequest)
      } ~
      (get & path("fail")) {
        complete(StatusCodes.InternalServerError)
      }
    }
}
