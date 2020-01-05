
package ie.noel.dunsceal.models.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import ie.noel.dunsceal.models.Dun
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var county: String = "",
    var zoom: Float = 0f
) : Parcelable

@IgnoreExtraProperties
@Parcelize
@Entity(tableName = "duns")
data class DunEntity(
    @PrimaryKey(autoGenerate = true) override var id: Int = 0,
    override var name: String = "",
    override var description: String = "",
    override var votes: Int = 0,
    override var fbId: String = "",
    override var visited : Boolean = false,
    override var image: String = "",
    //@Embedded override var images: ArrayList<String> = arrayListOf("dummy1", "dummy2", "dummy3", "dummy4"),
    @Embedded override var location: Location = Location(),
    override var message: String = "a message",
    override var email: String = "joe@bloggs.com"
) : Parcelable, Dun {
  @Exclude
  fun toMap(): Map<String, Any?> {
    return mapOf(
        "id" to id,
        "name" to name,
        "description" to description,
        "votes" to votes,
        "fbId" to fbId,
        "visited" to visited,
        "image" to image,
       // "images" to images,
        "location" to location,
        "message" to message,
        "image" to image,
        "email" to email
    )
  }
}

@IgnoreExtraProperties
@Parcelize
data class User(
    var uid: String? = "",
    var paymenttype: String = "N/A",
    var upVote: Int = 0,
    var message: String = "a message",
    var votes: Int = 0,
    var image: String = "",
    var email: String? = "joe@bloggs.com"
) : Parcelable {
  @Exclude
  fun toMap(): Map<String, Any?> {
    return mapOf(
        "uid" to uid,
        "paymenttype" to paymenttype,
        "upVote" to upVote,
        "message" to message,
        "votes" to votes,
        "image" to image,
        "email" to email
    )
  }
}