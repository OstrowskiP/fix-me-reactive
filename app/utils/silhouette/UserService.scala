package utils.silhouette

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.User
import utils.silhouette.Implicits._

import scala.concurrent.Future

class UserService extends IdentityService[User] {
  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = User.findByEmail(loginInfo)

  def save(user: User): Future[Option[User]] = User.save(user)
}