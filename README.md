# akka-http-metrics

![](https://travis-ci.org/backline/akka-http-metrics.svg)

> A metrics wrapper around akka-http routes and directives.

Current versions:

- `akka-http-*`: `1.0`
- `metrics-core`: `3.1.2`

## Install & Usage

```scala
resolvers += "bintray-backline-open-source-releases" at "https://dl.bintray.com/backline/open-source"
libraryDependencies ++= Seq(
  "backline" %% "akka-http-metrics" % "0.2.0"
)
```

## Example Usage

```scala
import akka.http.scaladsl.server.Directives
import backline.http.metrics.HttpTimerMetrics

trait MyRoute extends Directives {
  def myRoute = {
    timerDirective {
      (get & path("ping")) {
        complete("pong")
      }
    }
  }
}
```

## Contributing

- Open a PR and submit it!

## License

This software is licensed under the [Apache 2.0 license](LICENSE).
