package ie.noel.dunsceal.models

import ie.noel.dunsceal.models.entity.Location

interface Dun {
  val id: Int
  var name: String
  var description: String
  var votes: Int
  var fbId: String
  var visited: Boolean
  var image: String
  var location: Location
}