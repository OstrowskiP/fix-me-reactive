package controllers

import javax.inject.{ Inject, Singleton }

import com.mohiva.play.silhouette.api.Silhouette
import models._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints.maxLength
import play.api.i18n.{ Lang, Messages, MessagesApi }
import reactivemongo.bson.BSONObjectID
import utils.silhouette._
import views.html.makeARequest

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

@Singleton
class Application @Inject() (val silhouette: Silhouette[MyEnv], val messagesApi: MessagesApi) extends AuthController {

  val makeARequestForm = Form(
    mapping(
      "_id" -> ignored(None: Option[BSONObjectID]),
      "userEmail" -> email.verifying(maxLength(250)),
      "customerName" -> nonEmptyText,
      "customerLastname" -> nonEmptyText,
      "customerAddress" -> nonEmptyText,
      "customerPhone" -> text(9, 9).verifying("Only digits", number => number.matches("[0-9]{9}")),
      "deviceType" -> nonEmptyText,
      "deviceManufacturer" -> nonEmptyText,
      "deviceModel" -> nonEmptyText,
      "description" -> nonEmptyText,
      "repairDate" -> nonEmptyText,
      "repairTime" -> nonEmptyText,
      "requestStatus" -> ignored(AwaitingConfirmation(): RequestStatus),
      "partsUsed" -> ignored(List[Part]()),
      "serviceCost" -> ignored(0.0),
      "partsCost" -> ignored(0.0),
      "totalCost" -> ignored(0.0)
    )(FixRequest.apply)(FixRequest.unapply)
  )

  val editRequestForm = Form(
    mapping(
      "_id" -> ignored(None: Option[BSONObjectID]),
      "userEmail" -> ignored(""),
      "customerName" -> nonEmptyText,
      "customerLastname" -> nonEmptyText,
      "customerAddress" -> nonEmptyText,
      "customerPhone" -> text(9, 9).verifying("Only digits", number => number.matches("[0-9]{9}")),
      "deviceType" -> nonEmptyText,
      "deviceManufacturer" -> nonEmptyText,
      "deviceModel" -> nonEmptyText,
      "description" -> nonEmptyText,
      "repairDate" -> nonEmptyText,
      "repairTime" -> nonEmptyText,
      "requestStatus" -> ignored(AwaitingConfirmation(): RequestStatus),
      "partsUsed" -> ignored(List[Part]()),
      "serviceCost" -> ignored(0.0),
      "partsCost" -> ignored(0.0),
      "totalCost" -> ignored(0.0)
    )(FixRequest.apply)(FixRequest.unapply)
  )

  val trackRequestForm = Form(
    single(
      "requestId" -> text(24, 24)
    )
  )

  val partForm = Form(
    mapping(
      "_id" -> ignored(Some(BSONObjectID.generate): Option[BSONObjectID]),
      "name" -> nonEmptyText,
      "price" -> of(doubleFormat)
    )(Part.apply)(Part.unapply)
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
  def parts = SecuredAction(WithService("master")).async { implicit request =>
    Part.parts.map { partsList =>
      Ok(views.html.parts(partsList))
    }
  }

  def addPart = SecuredAction(WithService("master")) { implicit request =>
    Ok(views.html.editPart(partForm))
  }

  def handleAddPart = SecuredAction(WithService("master")).async { implicit request =>
    partForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.editPart(formWithErrors))),
      part => {
        Part.create(part).map {
          case Some(_) => Redirect(routes.Application.parts).flashing("success" -> "Saved")
          case None => BadRequest(views.html.editPart(partForm.withError("name", "Couldn't save part")))
        }
      }
    )
  }

  def handleUpdatePart(partId: String) = SecuredAction(WithService("master")).async { implicit request =>
    partForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.editPart(formWithErrors))),
      part => {
        Part.save(part.copy(_id = BSONObjectID.parse(partId).toOption)).map {
          case Some(_) => Redirect(routes.Application.parts)
          case None => BadRequest(views.html.editPart(partForm.withError("name", "Couldn't save part")))
        }
      }
    )
  }

  def updatePart(partId: String) = SecuredAction(WithService("master")).async { implicit request =>
    Part.findPart(partId).map {
      case Some(part) => Ok(views.html.editPart(partForm.fill(part), part._id.map(_.stringify)))
      case None => Redirect(routes.Application.parts).flashing("error" -> "Couldn't find part")
    }
  }

  def deletePart(partId: String) = SecuredAction(WithService("master")).async { implicit request =>
    Part.remove(partId).map {
      case Some(_) => Redirect(routes.Application.parts).flashing("success" -> "Deleted part")
      case None => Redirect(routes.Application.parts).flashing("error" -> "Couldn't delete part")
    }
  }

  def startMakeARequest = UserAwareAction { implicit request =>
    request.identity match {
      case Some(user) => Ok(views.html.makeARequest(makeARequestForm.fill(FixRequest(
        _id = None,
        userEmail = user.email,
        customerName = user.firstName,
        customerLastname = user.lastName,
        customerAddress = user.address,
        customerPhone = user.phone,
        deviceType = "",
        deviceManufacturer = "",
        deviceModel = "",
        description = "",
        repairDate = "",
        repairTime = "",
        requestStatus = AwaitingConfirmation(),
        partsUsed = List(),
        serviceCost = 0.0,
        partsCost = 0.0,
        totalCost = 0.0
      ))))
      case None => Ok(views.html.makeARequest(makeARequestForm))
    }
  }

  def handleMakeARequest = UserAwareAction.async { implicit request =>
    makeARequestForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(makeARequest(formWithErrors))),
      fixRequest => {
        FixRequest.save(fixRequest).map {
          case Some(savedFixRequest) => Ok(views.html.requestMade(savedFixRequest))
          case None => Ok(views.html.makeARequest(makeARequestForm.fill(fixRequest))).flashing("error" -> "Making request failed")
        }
      }
    )
  }

  def startTrackRequest = UserAwareAction { implicit request =>
    request.identity match {
      case Some(_) => Redirect(routes.Application.index)
      case None => Ok(views.html.trackRequest(trackRequestForm))
    }
  }

  def handleTrackRequest = UserAwareAction.async { implicit request =>
    trackRequestForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.trackRequest(formWithErrors))),
      requestId => {
        FixRequest.findById(requestId).map(fixRequestOpt => Ok(views.html.requestDetails(requestId, fixRequestOpt)))
      }
    )
  }

  def trackRequest(requestId: String) = UserAwareAction.async { implicit request =>
    FixRequest.findById(requestId).map(fixRequestOpt => Ok(views.html.requestDetails(requestId, fixRequestOpt)))
  }

  def myRequests = SecuredAction(WithService("serviceA")).async { implicit request =>
    (if (request.identity.services.contains("master")) FixRequest.fixRequests
    else FixRequest.findUsersRequests(request.identity.email))
      .map { fixRequests =>
        Ok(views.html.myRequests(fixRequests))
      }
  }

  def cancelRequest(requestId: String) = SecuredAction(WithService("serviceA")).async { implicit request =>
    FixRequest.findById(requestId).flatMap {
      case Some(fixRequest) => FixRequest.update(fixRequest.copy(requestStatus = Canceled())).map {
        case Some(_) => Redirect(routes.Application.myRequests).flashing("success" -> "Successfully canceled request")
        case None => Redirect(routes.Application.myRequests).flashing("error" -> "Couldn't cancel request")
      }
      case None => Future.successful(Redirect(routes.Application.myRequests).flashing("error" -> "Couldn't cancel request"))
    }
  }

  def updateRequest(requestId: String) = SecuredAction(WithService("serviceA")).async { implicit request =>
    FixRequest.findById(requestId).map {
      case Some(fixRequest) =>
        if (request.identity.services.contains("master") || fixRequest.userEmail == request.identity.email)
          Ok(views.html.editRequest(editRequestForm.fill(fixRequest), requestId))
        else
          Redirect(routes.Application.myRequests).flashing("error" -> "You don't have any matching requests")
      case None => Redirect(routes.Application.myRequests).flashing("error" -> "Couldn't find request")
    }
  }

  def handleUpdateRequest(requestId: String) = SecuredAction(WithService("serviceA")).async { implicit request =>
    editRequestForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.editRequest(formWithErrors, requestId))),
      updatedFixRequest => {
        FixRequest.findById(requestId).flatMap {
          case Some(fixRequestToUpdate) =>
            if (request.identity.services.contains("master") || fixRequestToUpdate.userEmail == request.identity.email) {
              FixRequest.update(fixRequestToUpdate.copy(
                customerName = updatedFixRequest.customerName,
                customerLastname = updatedFixRequest.customerLastname,
                customerAddress = updatedFixRequest.customerAddress,
                customerPhone = updatedFixRequest.customerPhone,
                deviceType = updatedFixRequest.deviceType,
                deviceManufacturer = updatedFixRequest.deviceManufacturer,
                deviceModel = updatedFixRequest.deviceModel,
                description = updatedFixRequest.description,
                repairDate = updatedFixRequest.repairDate,
                repairTime = updatedFixRequest.repairTime
              )).flatMap {
                case Some(_) => Future.successful(Redirect(routes.Application.myRequests).flashing("success" -> "Request saved successfully"))
                case None => Future.successful(Redirect(routes.Application.myRequests).flashing("error" -> "Couldn't save request"))
              }
            } else Future.successful(Redirect(routes.Application.myRequests).flashing("error" -> "You don't have any matching requests"))
          case None => Future.successful(Redirect(routes.Application.myRequests).flashing("error" -> "Couldn't save request"))
        }
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