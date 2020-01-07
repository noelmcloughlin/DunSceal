package ie.noel.dunsceal.models

import ie.noel.dunsceal.models.entity.LocationEntity

interface Dun {
  var id: Long
  var name: String
  var description: String
  var votes: Int
  var fbId: String
  var visited: Int
  var image: String
  var location: LocationEntity
  var email: String?
}