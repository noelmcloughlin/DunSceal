package ie.noel.dunsceal.models

import ie.noel.dunsceal.models.entity.LocationEntity
import java.util.*

interface Dun {
  var id: Long
  var name: String
  var description: String
  var votes: Int
  var fbId: String
  var visited: Int
  var visitDate: Date?
  var image: String
  var location: LocationEntity
  var email: String?
}