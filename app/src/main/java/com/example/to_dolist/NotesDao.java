package com.example.to_dolist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable add(Note note);

    @Query("DELETE FROM notes WHERE id = :id")
     Completable  remove(int id);

    @Query("UPDATE notes SET title = :title, description =:description, addDate =:addDate,deadLine =:deadLine,priority = :priority WHERE id =:id")
    Completable replace(int id, String title, String description, String addDate, String deadLine, int priority);

}
