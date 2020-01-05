package ie.noel.dunsceal.models

import ie.noel.dunsceal.models.entity.Location

interface Dun {
  val id: Int
  var name: String
  var description: String
  val votes: Int
  var fbId: String
  val visited: Boolean
  var image: String
  //val images: ArrayList<String>
  var location: Location
  var message: String
  var email: String
}