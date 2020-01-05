package ie.noel.dunsceal.models.entity

import androidx.room.Entity
import androidx.room.Fts4

@Entity(tableName = "contentFts")
@Fts4(contentEntity = DunEntity::class)
class ContentFtsEntity(val name: String, val description: String)