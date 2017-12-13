package pl.lodz.p.edu.dao

import models._
import reactivemongo.api.MongoConnection.ParsedURI
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
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

  private def usersCollection: Future[BSONCollection] = db.map(_.collection("users"))

  private def fixRequestsCollection: Future[BSONCollection] = db.map(_.collection("fixRequests"))

  private def partsCollection: Future[BSONCollection] = db.map(_.collection("parts"))

  implicit def userWriter: BSONDocumentWriter[User] = Macros.writer[User]

  implicit def userReader: BSONDocumentReader[User] = Macros.reader[User]

  implicit def requestStatusWriter: BSONDocumentWriter[RequestStatus] = derived.encoder[RequestStatus]

  implicit def requestStatusReader: BSONDocumentReader[RequestStatus] = derived.decoder[RequestStatus]

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

  protected def removeUser(email: String): Future[WriteResult] = usersCollection.flatMap(_.remove(document("email" -> email)))

  protected def findUserByEmail(email: String): Future[Option[User]] =
    usersCollection.flatMap(_.find(document("email" -> email)).one[User])

  protected def findAllUsers(): Future[List[User]] =
    usersCollection.flatMap(_.find(document()).cursor[User]().collect[List](25))

  /* Fix requests DAO */

  protected def createFixRequest(fixRequest: FixRequest): Future[Option[FixRequest]] = {
    val newFixRequest: FixRequest = fixRequest.copy(_id = Some(BSONObjectID.generate()))
    val res = fixRequestsCollection.flatMap(_.insert(newFixRequest))
    Await.ready(res, Duration(1, "minute"))
    findFixRequestById(newFixRequest._id.get)
  }

  protected def updateFixRequest(fixRequest: FixRequest): Future[Option[FixRequest]] = {
    val selector = document(
      "_id" -> fixRequest._id
    )
    val res = fixRequestsCollection.flatMap(_.update(selector, fixRequest))
    Await.ready(res, Duration(1, "minute"))
    findFixRequestById(fixRequest._id.get)
  }

  protected def findAllFixRequests(): Future[List[FixRequest]] =
    fixRequestsCollection.flatMap(_.find(document()).cursor[FixRequest]().collect[List](25))

  protected def findAllFixRequestsForUser(email: String) =
    fixRequestsCollection.flatMap(_.find(document("userEmail" -> email)).cursor[FixRequest]().collect[List](25))

  protected def findFixRequestById(id: BSONObjectID): Future[Option[FixRequest]] =
    fixRequestsCollection.flatMap(_.find(document("_id" -> id)).one[FixRequest])

  /* Parts DAO */

  protected def findAllParts(): Future[List[Part]] =
    partsCollection.flatMap(_.find(document()).cursor[Part]().collect[List](25))

  protected def findPartById(id: BSONObjectID): Future[Option[Part]] =
    partsCollection.flatMap(_.find(document("_id" -> id)).one[Part])

  protected def removePart(id: BSONObjectID): Future[WriteResult] =
    partsCollection.flatMap(_.remove(document("_id" -> id)))

  protected def updatePart(part: Part): Future[Option[Part]] = {
    val selector = document(
      "_id" -> part._id
    )
    val res = partsCollection.flatMap(_.update(selector, part, upsert = true))
    Await.ready(res, Duration(1, "minute"))
    findPartById(part._id.get)
  }

  protected def insertPart(part: Part): Future[Option[Part]] = {
    val newPart: Part = part.copy(_id = Some(BSONObjectID.generate()))
    val res = partsCollection.flatMap(_.insert(newPart))
    Await.ready(res, Duration(1, "minute"))
    findPartById(newPart._id.get)
  }
}
