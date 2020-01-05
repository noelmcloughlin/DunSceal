package ie.noel.dunsceal.models.entity

import androidx.room.*
import ie.noel.dunsceal.models.Investigation
import ie.noel.dunsceal.models.Note
import java.util.*

@Entity(tableName = "notes", foreignKeys = [ForeignKey(entity = DunEntity::class, parentColumns = ["id"], childColumns = ["contentId"], onDelete = ForeignKey.CASCADE)], indices = [Index(value = ["contentId"])])
class NoteEntity : Note {
  @PrimaryKey(autoGenerate = true)
  override var id = 0
  override var contentId = 0
  override var image: String = ""
  override var text: String? = null
  override var postedAt: Date? = null

  constructor() {} // needed

  @Ignore
  constructor(id: Int, contentId: Int, text: String?, postedAt: Date?) {
    this.id = id
    this.contentId = contentId
    this.image = image
    this.text = text
    this.postedAt = postedAt
  }
}