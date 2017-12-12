package models

import java.time.LocalTime

import pl.lodz.p.edu.dao.MongoDatabase
import play.api.i18n.Messages
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future
import scala.util.{ Failure, Success }

/**
 * Created by postrowski on 12/9/17.
 */
case class FixRequest(
  _id: Option[BSONObjectID],
  userEmail: Option[String],
  customerName: Option[String],
  customerLastname: Option[String],
  customerAddress: Option[String],
  customerPhone: Option[String],
  deviceType: String,
  deviceManufacturer: String,
  deviceModel: String,
  description: String,
  repairDate: String,
  repairTime: String,
  requestStatus: RequestStatus,
  partsUsed: List[Part],
  serviceCost: Double,
  partsCost: Double,
  totalCost: Double
)

sealed trait RequestStatus {
  val name: String
}
case class AwaitingConfirmation(name: String = "requeststatus.awaitingconfirmation") extends RequestStatus
case class Confirmed(name: String = "requeststatus.confirmed") extends RequestStatus
case class InProgress(name: String = "requeststatus.inprogress") extends RequestStatus
case class Fixed(name: String = "requeststatus.fixed") extends RequestStatus
case class Shipped(name: String = "requeststatus.shipped") extends RequestStatus
case class Canceled(name: String = "requeststatus.canceled") extends RequestStatus
case class Completed(name: String = "requeststatus.completed") extends RequestStatus

object FixRequest extends MongoDatabase {

  def fixRequests: Future[List[FixRequest]] = findAllFixRequests()

  def findById(id: String): Future[Option[FixRequest]] = BSONObjectID.parse(id) match {
    case Success(bsonId) => findFixRequestById(bsonId)
    case Failure(_) => Future.successful(None)
  }

  def save(fixRequest: FixRequest): Future[Option[FixRequest]] = createFixRequest(fixRequest)
  //    updateFixRequest(fixRequest)
}