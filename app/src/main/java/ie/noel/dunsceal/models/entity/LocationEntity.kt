
package ie.noel.dunsceal.models.entity

import androidx.room.Ignore

class LocationEntity {
  var latitude = 0.0
  var longitude = 0.0
  var county: String? = null
  var zoom = 0f

  @Ignore
  constructor(latitude: Double, longitude: Double, county: String?, zoom: Float) {
    this.latitude = latitude
    this.longitude = longitude
    this.county = county
    this.zoom = zoom
  }
  constructor()  //needed
}