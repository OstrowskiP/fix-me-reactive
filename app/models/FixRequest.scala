package models

import java.time.LocalTime

import pl.lodz.p.edu.dao.MongoDatabase
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

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
  requestStatus: String,
  partsUsed: List[Part],
  serviceCost: Double,
  partsCost: Double,
  totalCost: Double
)

//sealed trait RequestStatus
//case object AwaitingConfirmation extends RequestStatus
//case object Confirmed extends RequestStatus
//case object InProgress extends RequestStatus
//case object Fixed extends RequestStatus
//case object Shipped extends RequestStatus
//case object Canceled extends RequestStatus
//case object Completed extends RequestStatus

object FixRequest extends MongoDatabase {

  val fixRequests: Future[List[FixRequest]] = findAllFixRequests()

  def findById(id: String): Future[Option[FixRequest]] = findFixRequestById(BSONObjectID(id))

  def save(fixRequest: FixRequest): Future[Option[FixRequest]] = createFixRequest(fixRequest)
  //    updateFixRequest(fixRequest)
}