package pl.lodz.p.edu.dao

import models.{ User, UserError }
import reactivemongo.api.MongoConnection.ParsedURI
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{ Cursor, DefaultDB, MongoConnection, MongoDriver }
import reactivemongo.bson.{ BSONDocumentReader, BSONDocumentWriter, Macros, document }

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
import scala.util.Try

/**
 * Created by postrowski on 10/25/17.
 */
trait MongoDatabase {
  val mongoUri = "mongodb://localhost:27017/mydb?authMode=scram-sha1"

  import scala.concurrent.ExecutionContext.Implicits.global

  val driver = MongoDriver()
  val parsedUri: Try[ParsedURI] = MongoConnection.parseURI(mongoUri)
  val connection: Try[MongoConnection] = parsedUri.map(driver.connection)

  val futureConnection: Future[MongoConnection] = Future.fromTry(connection)
  def db: Future[DefaultDB] = futureConnection.flatMap(_.database("fixMeDB"))
  def usersCollection: Future[BSONCollection] = db.map(_.collection("users2"))

  implicit def userWriter: BSONDocumentWriter[User] = Macros.writer[User]

  def createUser(user: User): Future[Unit] =
    usersCollection.flatMap(_.insert(user).map(_ => {}))

  def updateUser(user: User): Future[User] = {
    val selector = document(
      "email" -> user.email
    )
    val res = usersCollection.flatMap(_.update(selector, user, upsert = true))
    Await.ready(res, Duration(1, "minute"))
    Future.successful(Await.result(findUserByEmail(user.email), Duration(1, "minute")).getOrElse(throw UserError(s"Can't retrieve user: $user")))
  }

  def removeUser(email: String): Future[Unit] = usersCollection.flatMap(_.remove(document("email" -> email))).map(_ => ())

  implicit def userReader: BSONDocumentReader[User] = Macros.reader[User]

  def findUserByEmail(email: String): Future[Option[User]] =
    usersCollection.flatMap(_.find(document("email" -> email)).one[User])

  def findAll(): Future[List[User]] =
    usersCollection.flatMap(_.find(document("" -> "")).cursor[User]().collect[List](25))
}
