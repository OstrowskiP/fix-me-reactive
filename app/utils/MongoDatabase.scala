package pl.lodz.p.edu.dao

import java.time.LocalTime

import models._
import reactivemongo.api.MongoConnection.ParsedURI
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{ DefaultDB, MongoConnection, MongoDriver }
import reactivemongo.bson.{ BSONDocumentReader, BSONDocumentWriter, BSONObjectID, Macros, derived, document }

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
import scala.util.Try

/**
 * Created by postrowski on 10/25/17.
 */
trait MongoDatabase {
  private val mongoUri = "mongodb://localhost:27017/mydb?authMode=scram-sha1"

  import scala.concurrent.ExecutionContext.Implicits.global

  private val driver = MongoDriver()
  private val parsedUri: Try[ParsedURI] = MongoConnection.parseURI(mongoUri)
  private val connection: Try[MongoConnection] = parsedUri.map(driver.connection)

  private val futureConnection: Future[MongoConnection] = Future.fromTry(connection)

  private def db: Future[DefaultDB] = futureConnection.flatMap(_.database("fixMeDB"))

  private def usersCollection: Future[BSONCollection] = db.map(_.collection("users2"))

  private def fixRequestsCollection: Future[BSONCollection] = db.map(_.collection("fixRequests"))

  private def partsCollection: Future[BSONCollection] = db.map(_.collection("parts"))

  implicit def userWriter: BSONDocumentWriter[User] = Macros.writer[User]

  implicit def userReader: BSONDocumentReader[User] = Macros.reader[User]

  implicit def fixRequestWriter: BSONDocumentWriter[FixRequest] = Macros.writer[FixRequest]

  implicit def fixRequestReader: BSONDocumentReader[FixRequest] = Macros.reader[FixRequest]

  implicit def partWriter: BSONDocumentWriter[Part] = Macros.writer[Part]

  implicit def partReader: BSONDocumentReader[Part] = Macros.reader[Part]

  /* Users DAO */

  protected def createUser(user: User): Future[Unit] =
    usersCollection.flatMap(_.insert(user).map(_ => {}))

  protected def updateUser(user: User): Future[Option[User]] = {
    val selector = document(
      "email" -> user.email
    )
    val res = usersCollection.flatMap(_.update(selector, user, upsert = true))
    Await.ready(res, Duration(1, "minute"))
    findUserByEmail(user.email)
  }

  protected def removeUser(email: String): Future[Unit] = usersCollection.flatMap(_.remove(document("email" -> email))).map(_ => ())

  protected def findUserByEmail(email: String): Future[Option[User]] =
    usersCollection.flatMap(_.find(document("email" -> email)).one[User])

  protected def findAllUsers(): Future[List[User]] =
    usersCollection.flatMap(_.find(document()).cursor[User]().collect[List](25))

  /* Fix requests DAO */

  protected def createFixRequest(fixRequest: FixRequest): Future[Option[FixRequest]] = {
    val res = fixRequestsCollection.flatMap(_.insert(fixRequest))
    Await.ready(res, Duration(1, "minute"))
    findFixRequestById(fixRequest._id.get)
  }
  //    fixRequestsCollection.flatMap(_.insert(fixRequest).map(_ => {}))

  protected def updateFixRequest(fixRequest: FixRequest): Future[Option[FixRequest]] = {
    val selector = document(
      "_id" -> fixRequest._id
    )
    val res = fixRequestsCollection.flatMap(_.update(selector, fixRequest, upsert = true))
    Await.ready(res, Duration(1, "minute"))
    findFixRequestById(fixRequest._id.get)
  }

  protected def findAllFixRequests(): Future[List[FixRequest]] =
    fixRequestsCollection.flatMap(_.find(document()).cursor[FixRequest]().collect[List](25))

  protected def findFixRequestById(id: BSONObjectID): Future[Option[FixRequest]] =
    fixRequestsCollection.flatMap(_.find(document("_id" -> id)).one[FixRequest])

  /* Parts DAO */

  protected def findAllParts(): Future[List[Part]] =
    partsCollection.flatMap(_.find(document()).cursor[Part]().collect[List](25))

  protected def findPartByName(name: String): Future[Option[Part]] =
    partsCollection.flatMap(_.find(document("name" -> name)).one[Part])

  protected def updatePart(part: Part): Future[Option[Part]] = {
    val selector = document(
      "name" -> part.name
    )
    val res = partsCollection.flatMap(_.update(selector, part, upsert = true))
    Await.ready(res, Duration(1, "minute"))
    findPartByName(part.name)
  }
}
