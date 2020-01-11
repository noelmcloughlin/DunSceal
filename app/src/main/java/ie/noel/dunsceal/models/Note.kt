package ie.noel.dunsceal.models

import java.util.*

interface Note {
  var id: Long
  var dunId: Long
  var image: String
  var text: String?
  var postedAt: Date?
}