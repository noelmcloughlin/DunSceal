package ie.noel.dunsceal.models

// PoJo interface declaration
interface Location {
  val latitude: Double
  val longitude: Double
  val county: String?
  val zoom: Float
}