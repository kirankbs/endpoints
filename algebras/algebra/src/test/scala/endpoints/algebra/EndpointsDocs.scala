package endpoints.algebra

import scala.util.Try

trait EndpointsDocs extends Endpoints {

  locally {
    //#construction
    // An endpoint whose requests use the HTTP verb “GET” and the URL
    // path “/some-resource”, and whose responses have an entity of
    // type “text/plain”
    val someResource: Endpoint[Unit, String] =
      endpoint(get(path / "some-resource"), textResponse())
    //#construction
  }

  //#with-docs
  endpoint(
    get(path / "some-resource"),
    textResponse(),
    description = Some("The contents of some resource")
  )
  //#with-docs

  //#request-construction
  // A request that uses the verb “GET”, the URL path “/foo”,
  // no entity and no headers
  request(Get, path / "foo", emptyRequest, emptyHeaders)
  //#request-construction

  //#convenient-get
  get(path / "foo") // Same as above
  //#convenient-get

  //#urls
  path                                            // the root path “/”
  path / "users"                                  // “/users”
  path / "users" / segment[Long]()                // “/users/1234”, “/users/5678”, …
  path / "assets" / remainingSegments()           // “/assets/images/logo.png”
  path / "articles" /? qs[Int]("page")            // “/articles?page=2”, “/articles?page=5”, …
  // Optional parameter
  path / "articles" /? qs[Option[Int]]("page")    // “/articles”, “/articles?page=2”, …
  // Repeated parameter
  path / "articles" /? qs[List[String]]("kinds")  // “/articles?kinds=garden&kinds=woodworking”, …
  // Several parameters
  path /? (qs[String]("q") & qs[String]("lang"))  // “/?q=foo&lang=en”, …
  //#urls

  //#urls-with-docs
  // “/users/{id}”
  path / "users" / segment[Long]("id", docs = Some("A user id"))

  // “/?q=foo&lang=en”, …
  val query = qs[String]("q", docs = Some("Query"))
  val lang = qs[String]("lang", docs = Some("Language"))
  path /? (query & lang)
  //#urls-with-docs

  //#response
  // An HTTP response with status code 200 (Ok) and no entity
  val nothing: Response[Unit] = emptyResponse()
  //#response

  //#response-combinator
  // An HTTP response with status code 404 (Not Found) and no entity,
  // or with status code 200 (Ok) and a text entity.
  val maybeText: Response[Option[String]] = wheneverFound(textResponse())
  //#response-combinator

  // Shared definition used by the documentation of interpreters
  //#endpoint-definition
  val someResource: Endpoint[Int, String] =
    endpoint(get(path / "some-resource" / segment[Int]()), textResponse())
  //#endpoint-definition

  //#documented-endpoint-definition
  val someDocumentedResource: Endpoint[Int, String] =
    endpoint(
      get(path / "some-resource" / segment[Int]("id")),
      textResponse(docs = Some("The content of the resource"))
    )
  //#documented-endpoint-definition

  //#xmap-partial
  import java.time.LocalDate
  implicit def localDateSegment(implicit string: Segment[String]): Segment[LocalDate] =
    string.xmapPartial(s => Try(LocalDate.parse(s)).toOption)(_.toString)
  //#xmap-partial
}
