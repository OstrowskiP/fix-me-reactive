package utils.silhouette

import com.mohiva.play.silhouette.api.actions._
import com.mohiva.play.silhouette.api.{ Environment, Silhouette }
import models.User
import play.api.i18n.I18nSupport
import play.api.mvc.Controller

trait AuthController extends Controller with I18nSupport {
  def silhouette: Silhouette[MyEnv]

  def env: Environment[MyEnv] = silhouette.env

  def SecuredAction = silhouette.SecuredAction

  def UnsecuredAction = silhouette.UnsecuredAction

  def UserAwareAction = silhouette.UserAwareAction

  implicit def securedRequest2User[A](implicit request: SecuredRequest[MyEnv, A]): User = request.identity

  implicit def userAwareRequest2UserOpt[A](implicit request: UserAwareRequest[MyEnv, A]): Option[User] = request.identity
}