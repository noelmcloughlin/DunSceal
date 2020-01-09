package ie.noel.dunsceal.models

// PoJo interface declaration
interface Location {
  var id: Long
  var dunId: Long
  val latitude: Double
  val longitude: Double
  val zoom: Float
}