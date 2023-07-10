package com.example.to_dolist;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<Note> notes = new ArrayList<>();
    private OnNoteClick onNoteClick;
    private List<Note> originalNotes = new ArrayList<>();

    public void setNotes(List<Note> notes) {
        this.notes.clear(); // Очистить текущий список заметок
        this.notes.addAll(notes); // Добавить новые заметки
        this.originalNotes = new ArrayList<>(notes);
        notifyDataSetChanged();

    }

    public void setOnNoteClick(OnNoteClick onNoteClick) {
        this.onNoteClick = onNoteClick;
    }

    public List<Note> getNotes() {
        return new ArrayList<>(notes);
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate
                        (R.layout.note_item,
                                parent,
                                false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder viewHolder, int position) {
        Note note = notes.get(position);
        viewHolder.titleOutput.setText(note.getTitle());
        viewHolder.descriptionOutput.setText(note.getDescription());
        viewHolder.timeOutput.setText(note.getAddDate());

        int colorResId;
        switch (note.getPriority()){
            case 0:
                colorResId = R.drawable.rounded_corner_low_priotity;
                break;
            case 1:
                colorResId = R.drawable.rounded_corner_medium_priotity;
                break;
            default:
                colorResId = R.drawable.rounded_corner_high_priotity;
        }
        Drawable color = ContextCompat.getDrawable(viewHolder.itemView.getContext(),colorResId);
         viewHolder.note_layout.setBackground(color);
         viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (onNoteClick != null){
                     onNoteClick.onNoteClick(note);
                 }
             }
         });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public void filterList(List<Note> filteredList) {
        notes.clear();
        if (filteredList.isEmpty()) {
            notes.addAll(originalNotes);
        } else {
            notes.addAll(filteredList);
        }

        notifyDataSetChanged();
    }

   class NotesViewHolder extends RecyclerView.ViewHolder{
//       private  TextView textViewNote;
       private TextView titleOutput;
       private TextView descriptionOutput;
       private TextView timeOutput;

       private RelativeLayout note_layout;

       public NotesViewHolder(@NonNull View itemView) {
           super(itemView);
//           textViewNote = itemView.findViewById(R.id.textViewNote);
           titleOutput = itemView.findViewById(R.id.titleOutput);
           descriptionOutput = itemView.findViewById(R.id.descriptionOutput);
           timeOutput = itemView.findViewById(R.id.timeOutput);
           note_layout = itemView.findViewById(R.id.note_layout);
       }
   }

   interface OnNoteClick {
        void onNoteClick(Note note);
   }
}
