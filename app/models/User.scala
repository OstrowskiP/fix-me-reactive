package models

import utils.silhouette.IdentitySilhouette
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import pl.lodz.p.edu.dao.MongoDatabase

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

case class User(
    id: Option[Long],
    email: String,
    emailConfirmed: Boolean,
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

  val users = findAll()

  //  val users = scala.collection.mutable.HashMap[Long, User](
  //    1L -> User(Some(1L), "master@myweb.com", true, (new BCryptPasswordHasher()).hash("123123").password, "Eddy", "Eddard", "Stark", List("master")),
  //    2L -> User(Some(2L), "a@myweb.com", true, (new BCryptPasswordHasher()).hash("123123").password, "Maggy", "Margaery", "Tyrell", List("serviceA")),
  //    3L -> User(Some(3L), "b@myweb.com", true, (new BCryptPasswordHasher()).hash("123123").password, "Petyr", "Petyr", "Baelish", List("serviceB")),
  //    4L -> User(Some(4L), "a_b@myweb.com", true, (new BCryptPasswordHasher()).hash("123123").password, "Tyry", "Tyrion", "Lannister", List("serviceA", "serviceB"))
  //  )

  def findByEmail(email: String): Future[Option[User]] = findUserByEmail(email)
  //    Future.successful(users.find(_._2.email == email).map(_._2))

  def save(user: User): Future[User] = updateUser(user)
  //  {
  //    // A rudimentary auto-increment feature...
  //    def nextId: Long = users.maxBy(_._1)._1 + 1
  //
  //    val theUser = if (user.id.isDefined) user else user.copy(id = Some(nextId))
  //    users += (theUser.id.get -> theUser)
  //    Future.successful(theUser)
  //  }

  def remove(email: String): Future[Unit] = removeUser(email)
}
