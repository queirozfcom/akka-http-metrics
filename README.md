# akka-http-metrics

> A metrics wrapper around akka-http routes and directives.

Current versions:

- `akka-http-*`: `1.0`
- `metrics-core`: `4.0.0-SNAPSHOT`

## Install & Usage

```
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
