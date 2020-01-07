
package ie.noel.dunsceal.models.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import ie.noel.dunsceal.models.Dun
import ie.noel.dunsceal.models.entity.DunEntity
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
@Entity(tableName = "duns")
data class DunEntity(
    @PrimaryKey(autoGenerate = true) override var id: Long = 0,
    override var name: String = "",
    override var description: String = "",
    override var votes: Int = 0,
    override var fbId: String = "",
    override var visited : Int = 0,
    override var image: String = "",
    //@Embedded override var images: ArrayList<String> = arrayListOf("dummy1", "dummy2", "dummy3", "dummy4"),
    @Embedded override var location: LocationEntity = LocationEntity(),
    override var email: String? = "john@doe.com"
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
        "location" to location,
        "image" to image,
        "email" to email
    )
  }
}