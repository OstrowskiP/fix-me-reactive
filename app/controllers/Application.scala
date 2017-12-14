package controllers

import javax.inject.{ Inject, Singleton }

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import models._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints.maxLength
import play.api.i18n.{ Lang, MessagesApi }
import play.api.mvc.AnyContent
import reactivemongo.bson.BSONObjectID
import utils.silhouette._
import views.html.makeARequest

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

@Singleton
class Application @Inject() (val silhouette: Silhouette[MyEnv], val messagesApi: MessagesApi) extends AuthController {

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // UTILITIES

  def validateTime(time: String): Boolean = time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")

  def validateDate(date: String): Boolean = date.matches("((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})")

  def validatePhoneNumber(phoneNumber: String): Boolean = phoneNumber.matches("([0-9]{3})-([0-9]{3})-([0-9]{3})")

  def isAdmin(implicit request: SecuredRequest[MyEnv, AnyContent]): Boolean = request.identity.services.contains("master")

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // FORMS

  val makeARequestForm = Form(
    mapping(
      "_id" -> ignored(None: Option[BSONObjectID]),
      "userEmail" -> email.verifying(maxLength(50)),
      "customerName" -> nonEmptyText.verifying(maxLength(20)),
      "customerLastname" -> nonEmptyText.verifying(maxLength(40)),
      "customerAddress" -> nonEmptyText.verifying(maxLength(80)),
      "customerPhone" -> text(9, 9).verifying("Only digits", validatePhoneNumber(_)),
      "deviceType" -> nonEmptyText.verifying(maxLength(50)),
      "deviceManufacturer" -> nonEmptyText.verifying(maxLength(50)),
      "deviceModel" -> nonEmptyText.verifying(maxLength(20)),
      "description" -> nonEmptyText.verifying(maxLength(250)),
      "repairDate" -> nonEmptyText.verifying(maxLength(10)).verifying("Wrong date format", validateDate(_)),
      "repairTime" -> nonEmptyText.verifying(maxLength(5)).verifying("Wrong time format", validateTime(_)),
      "requestStatus" -> ignored("requeststatus.awaitingconfirmation": String),
      "partsUsed" -> ignored(List[String]()),
      "serviceCost" -> ignored(0.0),
      "partsCost" -> ignored(0.0),
      "totalCost" -> ignored(0.0)
    )(FixRequest.apply)(FixRequest.unapply)
  )

  val editRequestForm = Form(
    mapping(
      "_id" -> ignored(None: Option[BSONObjectID]),
      "userEmail" -> ignored(""),
      "customerName" -> nonEmptyText.verifying(maxLength(20)),
      "customerLastname" -> nonEmptyText.verifying(maxLength(40)),
      "customerAddress" -> nonEmptyText.verifying(maxLength(80)),
      "customerPhone" -> text(9, 9).verifying("Only digits", validatePhoneNumber(_)),
      "deviceType" -> nonEmptyText.verifying(maxLength(50)),
      "deviceManufacturer" -> nonEmptyText.verifying(maxLength(50)),
      "deviceModel" -> nonEmptyText.verifying(maxLength(20)),
      "description" -> nonEmptyText.verifying(maxLength(250)),
      "repairDate" -> nonEmptyText.verifying(maxLength(10)).verifying("Wrong date format", validateDate(_)),
      "repairTime" -> nonEmptyText.verifying(maxLength(5)).verifying("Wrong time format", validateTime(_)),
      "requestStatus" -> ignored("requeststatus.awaitingconfirmation": String),
      "partsUsed" -> ignored(List[String]()),
      "serviceCost" -> ignored(0.0),
      "partsCost" -> ignored(0.0),
      "totalCost" -> ignored(0.0)
    )(FixRequest.apply)(FixRequest.unapply)
  )

  val editRequestFormAdmin = Form(
    mapping(
      "_id" -> ignored(None: Option[BSONObjectID]),
      "userEmail" -> ignored(""),
      "customerName" -> nonEmptyText.verifying(maxLength(20)),
      "customerLastname" -> nonEmptyText.verifying(maxLength(40)),
      "customerAddress" -> nonEmptyText.verifying(maxLength(80)),
      "customerPhone" -> text(9, 9).verifying("Only digits", validatePhoneNumber(_)),
      "deviceType" -> nonEmptyText.verifying(maxLength(50)),
      "deviceManufacturer" -> nonEmptyText.verifying(maxLength(50)),
      "deviceModel" -> nonEmptyText.verifying(maxLength(20)),
      "description" -> nonEmptyText.verifying(maxLength(250)),
      "repairDate" -> nonEmptyText.verifying(maxLength(10)).verifying("Wrong date format", validateDate(_)),
      "repairTime" -> nonEmptyText.verifying(maxLength(5)).verifying("Wrong time format", validateTime(_)),
      "requestStatus" -> nonEmptyText,
      "partsUsed" -> list(nonEmptyText),
      "serviceCost" -> of(doubleFormat),
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

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // TOPBAR ACTIONS

  def index = UserAwareAction { implicit request =>
    Ok(views.html.index())
  }

  def myAccount = SecuredAction { implicit request =>
    Ok(views.html.myAccount())
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // PARTS MANAGEMENT

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

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // FIX REQUESTS MANAGEMENT

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
        requestStatus = "requeststatus.awaitingconfirmation",
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
      case Some(fixRequest) => FixRequest.update(fixRequest.copy(requestStatus = "requeststatus.canceled")).map {
        case Some(_) => Redirect(routes.Application.myRequests).flashing("success" -> "Successfully canceled request")
        case None => Redirect(routes.Application.myRequests).flashing("error" -> "Couldn't cancel request")
      }
      case None => Future.successful(Redirect(routes.Application.myRequests).flashing("error" -> "Couldn't cancel request"))
    }
  }

  def updateRequest(requestId: String) = SecuredAction(WithService("serviceA")).async { implicit request =>
    FixRequest.findById(requestId).flatMap {
      case Some(fixRequest) =>
        if (request.identity.services.contains("master")) {
          Part.parts.map(partsList => Ok(views.html.editRequest(editRequestFormAdmin.fill(fixRequest), requestId, partsList)))
        } else if (fixRequest.userEmail == request.identity.email)
          Future.successful(Ok(views.html.editRequest(editRequestForm.fill(fixRequest), requestId)))
        else
          Future.successful(Redirect(routes.Application.myRequests).flashing("error" -> "You don't have any matching requests"))
      case None => Future.successful(Redirect(routes.Application.myRequests).flashing("error" -> "Couldn't find request"))
    }
  }

  def handleUpdateRequest(requestId: String) = SecuredAction(WithService("serviceA")).async { implicit request =>
    (
      if (request.identity.services.contains("master")) editRequestFormAdmin
      else editRequestForm
    ).bindFromRequest.fold(
        formWithErrors => {
          if (isAdmin)
            Part.parts.map(partsList => BadRequest(views.html.editRequest(formWithErrors, requestId, partsList)))
          else
            Future.successful(BadRequest(views.html.editRequest(formWithErrors, requestId)))
        },
        updatedFixRequest => {
          FixRequest.findById(requestId).flatMap {
            case Some(fixRequestToUpdate) =>
              if (request.identity.services.contains("master") || fixRequestToUpdate.userEmail == request.identity.email) {

                val fixRequestToSave: Future[FixRequest] = {
                  if (isAdmin) {
                    Part.parts.map { partsList =>
                      val partsCostValue = updatedFixRequest.partsUsed.map(partName => partsList.find(_.name == partName).get.price).sum

                      fixRequestToUpdate.copy(
                        customerName = updatedFixRequest.customerName,
                        customerLastname = updatedFixRequest.customerLastname,
                        customerAddress = updatedFixRequest.customerAddress,
                        customerPhone = updatedFixRequest.customerPhone,
                        deviceType = updatedFixRequest.deviceType,
                        deviceManufacturer = updatedFixRequest.deviceManufacturer,
                        deviceModel = updatedFixRequest.deviceModel,
                        description = updatedFixRequest.description,
                        repairDate = updatedFixRequest.repairDate,
                        repairTime = updatedFixRequest.repairTime,
                        requestStatus = updatedFixRequest.requestStatus,
                        partsUsed = updatedFixRequest.partsUsed,
                        partsCost = partsCostValue,
                        serviceCost = updatedFixRequest.serviceCost,
                        totalCost = (partsCostValue + updatedFixRequest.serviceCost)
                      )
                    }
                  } else {
                    Future.successful(fixRequestToUpdate.copy(
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
                    ))
                  }
                }

                fixRequestToSave.flatMap { fixRequestToSaveValue =>
                  FixRequest.update(fixRequestToSaveValue).flatMap {
                    case Some(_) => Future.successful(Redirect(routes.Application.myRequests).flashing("success" -> "Request saved successfully"))
                    case None => Future.successful(Redirect(routes.Application.myRequests).flashing("error" -> "Couldn't save request"))
                  }
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