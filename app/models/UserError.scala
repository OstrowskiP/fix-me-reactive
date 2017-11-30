package models

/**
 * Created by postrowski on 10/25/17.
 */
case class UserError(message: String = null, cause: Throwable = null) extends RuntimeException(MyException.defaultMessage(message, cause), cause)

object MyException {
  def defaultMessage(message: String, cause: Throwable) =
    if (message != null) message
    else if (cause != null) cause.toString()
    else null
}