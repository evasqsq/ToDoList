package com.example.to_dolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.text.DateFormat;
import java.util.Calendar;

public class EditNoteActivity extends AppCompatActivity {

    private Database database = Database.getInstance();

    private EditText editTextNoteEditedTitle;
    private EditText editTextNoteEditedDescription;
    private EditText editTextAddEditedDate;
    private EditText editTextEditedDeadLine;
    private RadioButton radioButtonEditedLow;
    private RadioButton radioButtonEditedMedium;
    private RadioButton radioButtonEditedHigh;
    private Button buttonSaveEditedNote;
    private Button buttonDeleteNote;

    int idOfSentNote;

    private EditNoteViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        initViews();
        viewModel = new ViewModelProvider(this).get(EditNoteViewModel.class);
        viewModel.getNoteIsEdited().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean noteIsEdited) {
                if (noteIsEdited){
                    finish();
                }
            }
        });

        viewModel.getNoteIsDeleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean noteIsDeleted) {
                if (noteIsDeleted){
                    finish();
                }
            }
        });

        Intent intent = getIntent();
        editTextNoteEditedTitle.setText(intent.getStringExtra("NOTE_TITLE"));
        editTextNoteEditedDescription.setText(intent.getStringExtra("NOTE_DESCRIPTION"));
        editTextAddEditedDate.setText(intent.getStringExtra("NOTE_DATE"));
        editTextEditedDeadLine.setText(intent.getStringExtra("NOTE_DEADLINE"));
        idOfSentNote = intent.getIntExtra("NOTE_ID",0);
        buttonSaveEditedNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEditedNote();
            }
        });

        buttonDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletedNote();
            }
        });

    }

    private void initViews(){

        editTextNoteEditedTitle = findViewById(R.id.editTextNoteEditedTitle);
        editTextNoteEditedDescription = findViewById(R.id.editTextNoteEditedDescription);
        editTextAddEditedDate = findViewById(R.id.editTextAddEditedDate);
        editTextEditedDeadLine = findViewById(R.id.editTextEditedDeadLine);
        radioButtonEditedLow = findViewById(R.id.radioButtonEditedLow);
        radioButtonEditedMedium = findViewById(R.id.radioButtonEditedMedium);
        radioButtonEditedHigh = findViewById(R.id.radioButtonEditedHigh);
        buttonSaveEditedNote =findViewById(R.id.buttonSaveEditedNote);
        buttonDeleteNote = findViewById(R.id.buttonDeleteNote);


    }

    private void saveEditedNote(){
            String editedTitle = editTextNoteEditedTitle.getText().toString().trim();
            String editedDescription = editTextNoteEditedDescription.getText().toString().trim();
            Calendar calendar = Calendar.getInstance();
            String editedAddDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
            String editedDeadLine =editTextEditedDeadLine.getText().toString().trim();
            int priority = getPriority();
            viewModel.saveEditedNote(idOfSentNote,editedTitle,editedDescription,editedAddDate,editedDeadLine,priority);


    }

    private void deletedNote(){
        viewModel.remove(idOfSentNote);
    }


    private int getPriority(){
        int priority;
        if (radioButtonEditedLow.isChecked()){
            priority = 0;
        } else if (radioButtonEditedMedium.isChecked()) {
            priority = 1;
        }
        else{
            priority = 2;
        }
        return priority;
    }

}