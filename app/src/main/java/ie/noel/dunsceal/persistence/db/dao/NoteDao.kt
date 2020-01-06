package ie.noel.dunsceal.persistence.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ie.noel.dunsceal.models.entity.Note

@Dao
interface NoteDao {
  @Query("SELECT * FROM notes where dunId = :dunId")
  fun load(dunId: Int): LiveData<List<Note>?>?

  @Query("SELECT * FROM notes where dunId = :dunId")
  fun loadSync(dunId: Int): List<Note?>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(notes: List<Note?>?)

  // Room DB integration

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(note: Note?)

  @Query("SELECT * FROM notes")
  fun findAll(): List<Note?>?

  @Query("select * from notes where id = :id")
  fun findById(id: Long): Note?

  @Update
  fun update(note: Note?)

  @Delete
  fun delete(note: Note?)
}