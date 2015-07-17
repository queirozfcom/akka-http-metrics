package backline.http.metrics
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.model.StatusCodes
import io.dropwizard.metrics.MetricRegistry

object HttpTimerMetricsSpec extends RouteSpecification with HttpTimerMetrics with Directives {
  val metricRegistry = new MetricRegistry()

  "record timings for /ping" in {
    (1 to 10) foreach { _ =>
      Get("/ping") ~> routes ~> check {
        status === StatusCodes.OK
        responseAs[String].contains("pong") must beTrue
      }
    }
    val counts = metricRegistry.timer("ping.GET")
    counts.getCount() must be_==(10)
  }

  "the slow route should take over a second" in {
    (1 to 5) foreach { _ =>
      Get("/slow") ~> routes ~> check {
        status === StatusCodes.OK
        responseAs[String].contains("awake") must beTrue
      }
    }
    val counts = metricRegistry.timer("slow.GET")
    counts.getCount() must be_==(5)
    scala.math.abs(1D - counts.getMeanRate()) must be_<=(0.01D)
  }

  def routes =
    timerDirective {
      (get & path("ping")) {
        complete("pong")
      } ~
      (get & path("slow")) {
        Thread.sleep(1000)
        complete("awake")
      }
    }
}
