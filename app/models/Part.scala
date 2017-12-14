package models

import pl.lodz.p.edu.dao.MongoDatabase
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

/**
 * Created by postrowski on 12/9/17.
 */
case class Part(_id: Option[BSONObjectID], name: String, price: Double)

object Part extends MongoDatabase {
  def parts: Future[List[Part]] = findAllParts()

  def create(part: Part): Future[Option[Part]] = insertPart(part)

  def save(part: Part): Future[Option[Part]] = updatePart(part)

  def findPart(id: String): Future[Option[Part]] = BSONObjectID.parse(id) match {
    case Success(bsonId) => findPartById(bsonId)
    case Failure(_) => Future.successful(None)
  }

  def remove(id: String): Future[Option[WriteResult]] = BSONObjectID.parse(id) match {
    case Success(bsonId) => removePart(bsonId).map(Option(_))
    case Failure(_) => Future.successful(None)
  }
}