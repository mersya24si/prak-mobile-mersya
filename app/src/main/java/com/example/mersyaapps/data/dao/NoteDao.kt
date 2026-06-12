package com.example.mersyaapps.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mersyaapps.data.entity.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    suspend fun getAll(): List<NoteEntity>

    @Insert
    suspend fun insert(note: NoteEntity)

    @Update
    suspend fun update (note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)
}