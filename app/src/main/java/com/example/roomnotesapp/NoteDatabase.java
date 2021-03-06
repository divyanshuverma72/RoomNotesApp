package com.example.roomnotesapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            Log.d("TAG", "getInstance: ");
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database").
                    fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }
        return instance;
    }

    public static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("TAG", "populateDb first: ");
        }
    };
}
