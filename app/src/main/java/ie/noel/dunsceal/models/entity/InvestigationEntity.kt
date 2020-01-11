package ie.noel.dunsceal.models.entity

import androidx.room.*
import ie.noel.dunsceal.models.Investigation
import ie.noel.dunsceal.persistence.room.DunTypeConverters
import java.util.*

@Entity(tableName = "investigations", foreignKeys = [ForeignKey(entity = DunEntity::class, parentColumns = ["id"], childColumns = ["dunId"], onDelete = ForeignKey.CASCADE)], indices = [Index(value = ["dunId"])])
@TypeConverters(DunTypeConverters::class)
class InvestigationEntity : Investigation {
  @PrimaryKey(autoGenerate = true)
  override var id = 0L
  override var fbId: String = ""
  override var dunId = 0L
  override var image: String = ""
  override var text: String? = null
  @ColumnInfo(defaultValue = "('Created at' || CURRENT_TIMESTAMP)")
  override var postedAt: Date? = null

  constructor() {} // needed

  @Ignore
  constructor(id: Long, fbId: String, dunId: Long, image: String, text: String?, postedAt: Date?) {
    this.id = id
    this.fbId = fbId
    this.dunId = dunId
    this.image = image
    this.text = text
    this.postedAt = postedAt
  }
}