package models

import models.RequestStatus.RequestStatus
import pl.lodz.p.edu.dao.MongoDatabase

import scala.concurrent.Future

/**
 * Created by postrowski on 12/9/17.
 */
case class FixRequest(
  id: String,
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

object RequestStatus extends Enumeration {
  type RequestStatus = Value
  val AwaitingConfirmation, Confirmed, InProgress, Fixed, Shipped, Canceled, Completed = Value
}

object FixRequest extends MongoDatabase {

  val users = findAllUsers()

  def findByEmail(email: String): Future[Option[User]] = findUserByEmail(email)

  def save(user: User): Future[User] = updateUser(user)

  def remove(email: String): Future[Unit] = removeUser(email)
}