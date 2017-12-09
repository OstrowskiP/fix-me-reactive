package models

import pl.lodz.p.edu.dao.MongoDatabase

import scala.concurrent.Future

/**
 * Created by postrowski on 12/9/17.
 */
case class Part(name: String, price: Double)

object Part extends MongoDatabase {
  val parts: Future[List[Part]] = findAllParts()

  def save(part: Part): Future[Option[Part]] = updatePart(part)
}