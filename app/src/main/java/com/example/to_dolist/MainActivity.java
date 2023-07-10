package com.example.to_dolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private Button buttonAddNewNote;

    private NotesDatabase notesDatabase;

    private NotesAdapter notesAdapter;

    private MainViewModel viewModel;

    private SearchView searchViewNotes;

    private List<Note> originalNotes = new ArrayList<>();

    NotificationManagerCompat notificationManagerCompat;
    Notification notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        notesAdapter = new NotesAdapter();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        recyclerViewNotes.setAdapter(notesAdapter);
        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesAdapter.setNotes(notes);
                originalNotes = new ArrayList<>(notes);
            }
        });

        notesAdapter.setOnNoteClick(new NotesAdapter.OnNoteClick() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra("NOTE_TITLE", note.getTitle());
                intent.putExtra("NOTE_DESCRIPTION", note.getDescription());
                intent.putExtra("NOTE_DATE", note.getAddDate());
                intent.putExtra("NOTE_DEADLINE", note.getDeadLine());
                intent.putExtra("NOTE_ID", note.getId());
                startActivity(intent);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = notesAdapter.getNotes().get(position);
                viewModel.remove(note);
            }
        });

        buttonAddNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddNoteActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);
        searchViewNotes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                List<Note> filteredNotes = new ArrayList<>();
                for (Note note : originalNotes) {
                    if (note.getTitle().toLowerCase().contains(userInput) ||
                            note.getDescription().toLowerCase().contains(userInput)) {
                        filteredNotes.add(note);
                    }
                }
                if (userInput.isEmpty()) {
                    notesAdapter.filterList(originalNotes);
                } else {
                    notesAdapter.filterList(filteredNotes);
                }
                return true;
            }
        });
    }

    private void initViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAddNewNote = findViewById(R.id.buttonAddNewNote);
        searchViewNotes = findViewById(R.id.searchViewNotes);
    }
}