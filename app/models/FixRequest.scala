package models

import pl.lodz.p.edu.dao.MongoDatabase
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future
import scala.util.{ Failure, Success }

/**
 * Created by postrowski on 12/9/17.
 */
case class FixRequest(
  _id: Option[BSONObjectID],
  userEmail: String,
  customerName: String,
  customerLastname: String,
  customerAddress: String,
  customerPhone: String,
  deviceType: String,
  deviceManufacturer: String,
  deviceModel: String,
  description: String,
  repairDate: String,
  repairTime: String,
  requestStatus: String,
  partsUsed: List[String],
  serviceCost: Double,
  partsCost: Double,
  totalCost: Double
)

object FixRequest extends MongoDatabase {

  val requestStatuses: List[String] = List(
    "requeststatus.awaitingconfirmation",
    "requeststatus.confirmed",
    "requeststatus.inprogress",
    "requeststatus.fixed",
    "requeststatus.shipped",
    "requeststatus.canceled",
    "requeststatus.completed"
  )

  def fixRequests: Future[List[FixRequest]] = findAllFixRequests()

  def findUsersRequests(email: String): Future[List[FixRequest]] = findAllFixRequestsForUser(email)

  def findById(id: String): Future[Option[FixRequest]] = BSONObjectID.parse(id) match {
    case Success(bsonId) => findFixRequestById(bsonId)
    case Failure(_) => Future.successful(None)
  }

  def save(fixRequest: FixRequest): Future[Option[FixRequest]] = createFixRequest(fixRequest)

  def update(fixRequest: FixRequest): Future[Option[FixRequest]] = updateFixRequest(fixRequest)
}