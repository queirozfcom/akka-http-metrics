package backline.http.metrics
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, MediaTypes, MessageEntity}
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.{RoutingLog, RoutingSettings, RoutingSetup}
import akka.http.scaladsl.testkit.{TestFrameworkInterface, RouteTest, RouteTestTimeout}
import org.specs2.execute.{Failure, FailureException}
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import scalaz.NonEmptyList
import scala.concurrent.Future
import scala.concurrent.duration._

// until akka-http gets support
trait Specs2Interface extends TestFrameworkInterface {

  // from spray-testkit
  def failTest(msg: String): Nothing = {
    val trace = new Exception().getStackTrace.toList
    val fixedTrace = trace.drop(trace.indexWhere(_.getClassName.startsWith("org.specs2")) - 1)
    throw new FailureException(Failure(msg, stackTrace = fixedTrace))
  }
}

class RouteSpecification extends Specification with RouteTest with Specs2Interface { spec =>
  implicit def routeTestTimeout: RouteTestTimeout = RouteTestTimeout(FiniteDuration(5, "seconds"))

  protected implicit lazy val routingSettings = RoutingSettings.apply(system)
  protected implicit lazy val routingLog = RoutingLog.fromActorSystem(system)

  implicit def routingSetup: RoutingSetup = RoutingSetup.apply
}
