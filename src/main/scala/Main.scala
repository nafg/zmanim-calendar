import org.scalajs.dom
import scalatags.JsDom.all._
import typings.kosherZmanim.mod._
import typings.luxon.mod.{DateTime, DurationObject}
import typings.std.global

import scala.scalajs.js.|


object Main extends App {
  implicit class notNullExtensionMethods[A](self: A | Null) {
    def nn: Option[A] = Option(self).map(_.asInstanceOf[A])
  }

  dom.window.location.hash match {
    case s"#$lat,$lon" => render(lat.toDouble, lon.toDouble)
    case _             =>
      global.navigator.geolocation.getCurrentPosition { pos =>
        dom.window.location.hash = s"#${pos.coords.latitude},${pos.coords.longitude}"
        render(pos.coords.latitude, pos.coords.longitude)
      }
  }

  private def render(latitude: Double, longitude: Double) = {
    val geoLocation =
      new GeoLocation(
        "X",
        latitude = latitude,
        longitude = longitude,
        timeZoneId = "America/New_York"
      )


    case class CalFun(label: String, getter: ZmanimCalendar => DateTime | Null)

    val zmanim = List(
      CalFun("Alos", _.getAlos72()),
      CalFun("Netz", _.getSunrise())
    )

    val oneDay = DurationObject().setDay(1)

    val startDate = DateTime.local(year = 2021, month = 1, day = 1, hour = 0, minute = 0, second = 0, millisecond = 0)
    dom.document.body.appendChild(
      table(
        thead(
          th("Date"),
          for (zman <- zmanim) yield
            th(zman.label)
        ),
        Iterator.iterate(startDate)(_.plus(oneDay)).take(20).toSeq.map { date =>
          val cal = new ZmanimCalendar(geoLocation)
          cal.setDate(date)
          tr(
            td(date.toLocaleString()),
            for (zman <- zmanim) yield
              td(
                zman.getter(cal).nn.map(_.toLocal().toSQLTime()).getOrElse[String]("")
              )
          )
        }
      ).render
    )
  }
}
