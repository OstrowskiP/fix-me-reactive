package controllers

import javax.inject.{ Inject, Singleton }

import com.mohiva.play.silhouette.api.{ LoginInfo, Silhouette }
import models._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints.{ maxLength, minLength }
import play.api.i18n.{ Lang, Messages, MessagesApi }
import reactivemongo.bson.BSONObjectID
import utils.silhouette._
import views.html.{ auth, makeARequest }

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

@Singleton
class Application @Inject() (val silhouette: Silhouette[MyEnv], val messagesApi: MessagesApi) extends AuthController {

  val makeARequestForm = Form(
    mapping(
      "_id" -> ignored(Some(BSONObjectID.generate): Option[BSONObjectID]),
      "userEmail" -> ignored(None: Option[String]),
      //      "userEmail" -> email.verifying(maxLength(250)),
      "customerName" -> optional(nonEmptyText),
      "customerLastname" -> optional(nonEmptyText),
      "customerAddress" -> optional(nonEmptyText),
      "customerPhone" -> optional(nonEmptyText),
      "deviceType" -> nonEmptyText,
      "deviceManufacturer" -> nonEmptyText,
      "deviceModel" -> nonEmptyText,
      "description" -> nonEmptyText,
      "repairDate" -> nonEmptyText,
      "repairTime" -> nonEmptyText,
      "requestStatus" -> ignored("AwaitingConfirmation"),
      "partsUsed" -> ignored(List[Part]()),
      "serviceCost" -> ignored(0.0),
      "partsCost" -> ignored(0.0),
      "totalCost" -> ignored(0.0)
    )(FixRequest.apply _)(FixRequest.unapply _)
  )

  def index = UserAwareAction { implicit request =>
    Ok(views.html.index())
  }

  def myAccount = SecuredAction { implicit request =>
    Ok(views.html.myAccount())
  }

  // REQUIRED ROLES: serviceA (or master)
  def serviceA = SecuredAction(WithService("serviceA")) { implicit request =>
    Ok(views.html.serviceA())
  }

  // REQUIRED ROLES: serviceA OR serviceB (or master)
  def serviceAorServiceB = SecuredAction(WithService("serviceA", "serviceB")) { implicit request =>
    Ok(views.html.serviceAorServiceB())
  }

  // REQUIRED ROLES: serviceA AND serviceB (or master)
  def serviceAandServiceB = SecuredAction(WithServices("serviceA", "serviceB")) { implicit request =>
    Ok(views.html.serviceAandServiceB())
  }

  // REQUIRED ROLES: master
  def settings = SecuredAction(WithService("master")).async { implicit request =>
    Part.parts.map { partsList =>
      Ok(views.html.settings(partsList))
    }
  }

  def startMakeARequest = UserAwareAction { implicit request =>
    request.identity match {
      case Some(_) => Redirect(routes.Application.index)
      case None => Ok(views.html.makeARequest(makeARequestForm))
    }
  }

  def handleMakeARequest = UserAwareAction.async { implicit request =>
    makeARequestForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(makeARequest(formWithErrors))),
      fixRequest => {
        FixRequest.save(fixRequest).map {
          case Some(savedFixRequest) => Ok(views.html.requestMade(savedFixRequest))
          case None => BadRequest(makeARequest(makeARequestForm.withError("customerName", "Couldn't complete making request")))
        }
        //        val loginInfo: LoginInfo = user.email
        //        userService.retrieve(loginInfo).flatMap {
        //          case Some(_) => Future.successful(BadRequest(viewsAuth.signUp(signUpForm.withError("email", Messages("auth.user.notunique")))))
        //          case None => {
        //            val token = MailTokenUser(user.email, isSignUp = true)
        //            for {
        //              savedUser <- userService.save(user).map {
        //                case Some(u) => u
        //                case None => throw UserError(s"Can't find user $user")
        //              }
        //              _ <- authInfoRepository.add(loginInfo, passwordHasherRegistry.current.hash(user.password))
        //              _ <- tokenService.create(token)
        //            } yield {
        //              mailer.welcome(savedUser, link = routes.Auth.signUp(token.id).absoluteURL())
        //              Ok(viewsAuth.almostSignedUp(savedUser))
        //            }
        //          }
        //        }
      }
    )
  }

  def selectLang(lang: String) = UserAwareAction { implicit request =>
    Logger.logger.debug("Change user lang to : " + lang)
    request.headers.get(REFERER).map { referer =>
      Redirect(referer).withLang(Lang(lang))
    }.getOrElse {
      Redirect(routes.Application.index).withLang(Lang(lang))
    }
  }

}