package ie.noel.dunsceal.models

import ie.noel.dunsceal.models.entity.Location

interface Dun {
  val id: Int
  val name: String
  val description: String
  val price: Int?
  val fbId: String?
  val visited: Int?
  val upVotes: Int?
  val image: String?
  //val images: ArrayList<String>
  val location: Location?
  var upVote: Int?
  var message: String?
  var profilepic: String?
  var email: String?
}