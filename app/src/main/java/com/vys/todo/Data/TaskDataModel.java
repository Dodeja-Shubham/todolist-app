package com.vys.todo.Data;

public class TaskDataModel {

    private String title, category, due_date, due_time, colour,  created_at;
    private int id;
    private boolean is_completed;

    public TaskDataModel(){}

    public TaskDataModel(int ID, String TITLE, String CATEGORY, String DUE_DATE, String DUE_TIME, String COLOUR,
                         boolean IS_COMPLETED, String CREATED_AT) {
        this.id = ID;
        this.title = TITLE;
        this.category = CATEGORY;
        this.due_date = DUE_DATE;
        this.colour = COLOUR;
        this.due_time = DUE_TIME;
        this.is_completed = IS_COMPLETED;
        this.created_at = CREATED_AT;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_completed() {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }
}
