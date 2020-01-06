package ie.noel.dunsceal.models

import java.util.*

interface Note {
  val id: Int
  val dunId: Int
  val image: String
  val text: String?
  val postedAt: Date?
}