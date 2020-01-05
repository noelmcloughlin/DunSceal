package ie.noel.dunsceal.models

import ie.noel.dunsceal.models.entity.Location

interface Content {
  val id: Int
  var fbId: String?
  var name: String
  var description: String
  val votes: Int?
  var image: String?
  var location: Location?
  var message: String?
  var email: String?
}