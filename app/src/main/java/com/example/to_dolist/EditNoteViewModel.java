package com.example.to_dolist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class EditNoteViewModel extends AndroidViewModel {

    private NotesDatabase notesDatabase;
    private MutableLiveData<Boolean> noteIsEdited = new MutableLiveData<>();
    private MutableLiveData<Boolean> noteIsDeleted = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<Boolean> getNoteIsEdited() {
        return noteIsEdited;
    }

    public LiveData<Boolean> getNoteIsDeleted() {
        return noteIsDeleted;
    }

    public EditNoteViewModel(@NonNull Application application) {
        super(application);
        notesDatabase = NotesDatabase.getInstance(application);
    }

    public void saveEditedNote(int idOfSentNote, String editedTitle, String editedDescription, String editedAddDate, String editedDeadLine, int priority) {
        Disposable disposable = notesDatabase.notesDao().replace(idOfSentNote,editedTitle,editedDescription,editedAddDate,editedDeadLine,priority)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        noteIsEdited.setValue(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("EditNoteViewModel","Error while editing note");
                    }

                });
        compositeDisposable.add(disposable);

    }

    public void remove(int idOfSentNote){
        Disposable disposable = notesDatabase.notesDao().remove(idOfSentNote)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                            noteIsDeleted.setValue(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("MainViewModel","Error while removeing note");
                    }
                });
        compositeDisposable.add(disposable);

    }

    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
