package com.example.to_dolist;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class AddNoteViewModel extends AndroidViewModel {
    private NotesDatabase notesDatabase;
    private MutableLiveData<Boolean> noteIsSaved = new MutableLiveData<>();


    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public LiveData<Boolean> getNoteIsSaved() {
        return noteIsSaved;
    }


    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        notesDatabase = NotesDatabase.getInstance(application);
    }

    @SuppressLint("CheckResult")
    public void saveNote(Note note){
        Disposable disposable = notesDatabase.notesDao().add(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        noteIsSaved.setValue(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("AddNoteViewModel","Error while adding Note");
                    }
                });
        compositeDisposable.add(disposable);

    }



    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
