package models

import utils.silhouette.IdentitySilhouette
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import pl.lodz.p.edu.dao.MongoDatabase
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

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
    /*
	* A user can register some accounts from third-party services, then it will have access to different parts of the webpage. The 'master' privilege has full access.
	* Ex: ("master") -> full access to every point of the webpage.
	* Ex: ("serviceA") -> have access only to general and serviceA areas.
	* Ex: ("serviceA", "serviceB") -> have access only to general, serviceA and serviceB areas.
	*/
    services: List[String]
) extends IdentitySilhouette {
  def key = email
  def fullName: String = firstName + " " + lastName
}

object User extends MongoDatabase {

  val roles = Seq("serviceA", "serviceB", "master")

  def users: Future[List[User]] = findAllUsers()

  def findByEmail(email: String): Future[Option[User]] = findUserByEmail(email)

  def save(user: User): Future[Option[User]] = updateUser(user)

  def remove(email: String): Future[Option[WriteResult]] = removeUser(email).map(Option(_))
}
