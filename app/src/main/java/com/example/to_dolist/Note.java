package com.example.to_dolist;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notes")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int priority;
    private String addDate;
    private String deadLine;

    public Note(int id, String title, String description, int priority, String addDate, String deadLine) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.addDate = addDate;
        this.deadLine = deadLine;
    }

    @Ignore
    public Note( String title, String description, int priority, String addDate, String deadLine) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.addDate = addDate;
        this.deadLine = deadLine;
    }


    public Note(int tmpId, String tmpTitle, int tmpPriority, String valueOf, String tmpDeadLine) {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public String getAddDate() {
        return addDate;
    }

    public String getDeadLine() {
        return deadLine;
    }



    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public boolean containsQuery(String query) {
        return title.contains(query) || description.contains(query);
    }
}
