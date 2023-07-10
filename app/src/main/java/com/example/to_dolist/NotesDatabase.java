package com.example.to_dolist;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Note.class},version = 5,exportSchema = true)
public abstract class NotesDatabase extends RoomDatabase {

    private static NotesDatabase instance = null;
    private static final String DB_NAME = "notes.db";

    public static NotesDatabase getInstance(Application application){
        if (instance == null){
            instance = Room.databaseBuilder(
                    application,
                    NotesDatabase.class,
                    DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract NotesDao notesDao();
}
