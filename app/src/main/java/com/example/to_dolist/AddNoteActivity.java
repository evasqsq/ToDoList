package com.example.to_dolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.AlarmManagerCompat;

public class AddNoteActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTextNoteTitle;
    private EditText editTextNoteDescription;
    private TextView editTextDeadLine;
    private RadioButton radioButtonLow;
    private RadioButton radioButtonMedium;
    private RadioButton radioButtonHigh;
    private Button buttonSaveNote;

    private Button buttonChooseDeadLine;


    private AddNoteViewModel viewModel;

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;


    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initViews();
        viewModel = new ViewModelProvider(this).get(AddNoteViewModel.class);
        viewModel.getNoteIsSaved().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean noteIsSaved) {
                if (noteIsSaved){
                    scheduleNotification();
                    finish();
                }
            }
        });
        buttonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        buttonChooseDeadLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Создание интента для AlarmReceiver
        Intent intent = new Intent(this, AlarmReciever.class);
        intent.setAction("MY_NOTIFICATION_ACTION");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,i);
        c.set(Calendar.MONTH,i1);
        c.set(Calendar.DAY_OF_MONTH,i2);
        String currentDataString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        editTextDeadLine.setText(currentDataString);
    }

    private void initViews(){

        editTextNoteTitle = findViewById(R.id.editTextNoteEditedTitle);
        editTextNoteDescription = findViewById(R.id.editTextNoteEditedDescription);
        radioButtonLow = findViewById(R.id.radioButtonEditedLow);
        radioButtonMedium = findViewById(R.id.radioButtonEditedMedium);
        radioButtonHigh = findViewById(R.id.radioButtonEditedHigh);
        buttonSaveNote = findViewById(R.id.buttonSaveEditedNote);
        buttonChooseDeadLine = findViewById(R.id.buttonChooseDeadLine);
        editTextDeadLine = findViewById(R.id.editTextDeadLine);

    }

    private void saveNote(){
        String title = editTextNoteTitle.getText().toString().trim();
        String description = editTextNoteDescription.getText().toString().trim();
        Calendar calendar = Calendar.getInstance();
        String addDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        String deadLine = editTextDeadLine.getText().toString();
        int priority = getPriority();
        Note note = new Note(title,description,priority,addDate,deadLine);
        viewModel.saveNote(note);
    }

    private int getPriority(){
        int priority;
        if (radioButtonLow.isChecked()){
            priority = 0;
        } else if (radioButtonMedium.isChecked()) {
            priority = 1;
        }
        else{
            priority = 2;
        }
        return priority;
    }
    private void scheduleNotification() {
        String deadline = editTextDeadLine.getText().toString();
        if (!deadline.isEmpty()) {
            Calendar deadlineCalendar = Calendar.getInstance();
            try {
                deadlineCalendar.setTime(DateFormat.getDateInstance(DateFormat.FULL).parse(deadline));
            } catch (Exception e) {
                e.printStackTrace();
            }

            long deadlineInMillis = deadlineCalendar.getTimeInMillis();

            // Создание интента для AlarmReceiver с информацией о заметке
            Intent alarmIntent = new Intent(this, AlarmReciever.class);
            alarmIntent.setAction("MY_NOTIFICATION_ACTION");
            alarmIntent.putExtra("extra_note_title", editTextNoteTitle.getText().toString());
            alarmIntent.putExtra("extra_note_description", editTextNoteDescription.getText().toString());

            PendingIntent pendingIntent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                // Используйте флаг FLAG_IMMUTABLE для версии S+ (Android 31 и выше)
                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            } else {
                // Используйте только флаг FLAG_UPDATE_CURRENT для более старых версий Android
                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
            }

            // Настройка AlarmManager для планирования уведомления
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, deadlineInMillis, pendingIntent);

            Toast.makeText(this, "Уведомление запланировано", Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent newIntent(Context context){
        return new Intent(context,AddNoteActivity.class);
    }
}