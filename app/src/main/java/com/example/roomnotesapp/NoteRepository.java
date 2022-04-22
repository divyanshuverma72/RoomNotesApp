package com.example.roomnotesapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteRepository {
    private final NoteDao noteDao;
    private final LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) {
        insertNote(note);
    }

    public void update(Note note) {
        updateNote(note);
    }

    public void delete(Note note) {
        deleteNote(note);
    }

    public void deleteAllNotes() {
        deleteAllNote();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    private void insertNote(Note note) {
        Observable.fromCallable(() -> {
            noteDao.insert(note);
            // RxJava does not accept null return value. Null will be treated as a failure.
            // So just make it return true.
            return true;
        }) // Execute in IO thread, i.e. background thread.
                .subscribeOn(Schedulers.io())
                // report or post the result to main thread.
                .observeOn(AndroidSchedulers.mainThread())
                // execute this RxJava
                .subscribe();
    }

    private void updateNote(Note note) {
        Observable.fromCallable(() -> {
            noteDao.update(note);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private void deleteNote(Note note) {
        Observable.fromCallable(() -> {
            noteDao.delete(note);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private void deleteAllNote() {
        Observable.fromCallable(() -> {
            noteDao.deleteAllNotes();
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
