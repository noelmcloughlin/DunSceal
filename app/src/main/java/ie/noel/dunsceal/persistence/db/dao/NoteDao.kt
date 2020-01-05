package ie.noel.dunsceal.persistence.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ie.noel.dunsceal.models.entity.NoteEntity

@Dao
interface NoteDao {
  @Query("SELECT * FROM notes where contentId = :contentId")
  fun loadNotes(contentId: Int): LiveData<List<NoteEntity>?>?

  @Query("SELECT * FROM notes where contentId = :contentId")
  fun loadNotesSync(contentId: Int): List<NoteEntity?>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(notes: List<NoteEntity?>?)

  // Room DB integration

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun createDun(note: NoteEntity?)

  @Query("SELECT * FROM notes")
  fun findAll(): List<NoteEntity?>?

  @Query("select * from notes where id = :id")
  fun findById(id: Int): NoteEntity?

  @Update
  fun updateNote(note: NoteEntity?)

  @Delete
  fun deleteNote(note: NoteEntity?)
}