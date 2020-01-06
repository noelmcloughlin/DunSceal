package ie.noel.dunsceal.models.entity

import androidx.room.*
import ie.noel.dunsceal.models.Investigation
import ie.noel.dunsceal.models.Note
import java.util.*

@Entity(tableName = "notes", foreignKeys = [ForeignKey(entity = DunEntity::class, parentColumns = ["id"], childColumns = ["dunId"], onDelete = ForeignKey.CASCADE)], indices = [Index(value = ["dunId"])])
class NoteEntity : Note {
  @PrimaryKey(autoGenerate = true)
  override var id = 0
  override var dunId = 0
  override var image: String = ""
  override var text: String? = null
  override var postedAt: Date? = null

  constructor() {} // needed

  @Ignore
  constructor(id: Int, dunId: Int, image: String, text: String?, postedAt: Date?) {
    this.id = id
    this.dunId = dunId
    this.image = image
    this.text = text
    this.postedAt = postedAt
  }
}