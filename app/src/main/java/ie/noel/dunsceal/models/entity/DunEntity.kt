
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
    override var price: Int = 0,
    override var fbId: String = "",
    override var visited: Int = 0,
    override var upVotes: Int = 0,
    override var image: String = "",
    //@Embedded override var images: ArrayList<String> = arrayListOf("dummy1", "dummy2", "dummy3", "dummy4"),
    @Embedded override var location: Location = Location(),
    var paymenttype: String = "N/A",
    override var upVote: Int? = 0,
    override var message: String? = "a message",
    override var profilepic: String? = "",
    override var email: String? = "joe@bloggs.com"
) : Parcelable, Dun {
  @Exclude
  fun toMap(): Map<String, Any?> {
    return mapOf(
        "id" to id,
        "name" to name,
        "description" to description,
        "price" to price,
        "fbId" to fbId,
        "visited" to visited,
        "upVotes" to upVotes,
        "image" to image,
       // "images" to images,
        "location" to location,
        "paymenttype" to paymenttype,
        "upVote" to upVote,
        "message" to message,
        "profilepic" to profilepic,
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
    var upVotes: Int = 0,
    var profilepic: String = "",
    var email: String? = "joe@bloggs.com"
) : Parcelable {
  @Exclude
  fun toMap(): Map<String, Any?> {
    return mapOf(
        "uid" to uid,
        "paymenttype" to paymenttype,
        "upVote" to upVote,
        "message" to message,
        "upVotes" to upVotes,
        "profilepic" to profilepic,
        "email" to email
    )
  }
}