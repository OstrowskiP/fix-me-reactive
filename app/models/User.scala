package models

import pl.lodz.p.edu.dao.MongoDatabase
import reactivemongo.api.commands.WriteResult
import utils.silhouette.IdentitySilhouette

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class User(
    id: Option[Long],
    email: String,
    emailConfirmed: Boolean,
    address: String,
    phone: String,
    password: String,
    nick: String,
    firstName: String,
    lastName: String,
    services: List[String]
) extends IdentitySilhouette {
  def key = email

  def fullName: String = firstName + " " + lastName
}

object User extends MongoDatabase {

  val roles = Seq("serviceA", "master")

  def users: Future[List[User]] = findAllUsers()

  def findByEmail(email: String): Future[Option[User]] = findUserByEmail(email)

  def save(user: User): Future[Option[User]] = updateUser(user)

  def remove(email: String): Future[Option[WriteResult]] = removeUser(email).map(Option(_))
}
