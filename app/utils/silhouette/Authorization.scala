package utils.silhouette

import com.mohiva.play.silhouette.api.Authorization
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.User
import play.api.mvc.Request

import scala.concurrent.Future

/**
 * Only allows those users that have at least a service of the selected.
 * Master service is always allowed.
 * Ex: WithService("customer", "serviceB") => only users with services "customer" OR "serviceB" (or "administrator") are allowed.
 */
case class WithService(anyOf: String*) extends Authorization[User, CookieAuthenticator] {
  def isAuthorized[A](user: User, authenticator: CookieAuthenticator)(implicit r: Request[A]) = Future.successful {
    WithService.isAuthorized(user, anyOf: _*)
  }
}

object WithService {
  def isAuthorized(user: User, anyOf: String*): Boolean =
    anyOf.intersect(user.services).size > 0 || user.services.contains("administrator")
}

/**
 * Only allows those users that have every of the selected services.
 * Master service is always allowed.
 * Ex: Restrict("customer", "serviceB") => only users with services "customer" AND "serviceB" (or "administrator") are allowed.
 */
case class WithServices(allOf: String*) extends Authorization[User, CookieAuthenticator] {
  def isAuthorized[A](user: User, authenticator: CookieAuthenticator)(implicit r: Request[A]) = Future.successful {
    WithServices.isAuthorized(user, allOf: _*)
  }
}

object WithServices {
  def isAuthorized(user: User, allOf: String*): Boolean =
    allOf.intersect(user.services).size == allOf.size || user.services.contains("administrator")
}