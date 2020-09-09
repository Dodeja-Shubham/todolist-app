package com.vys.todo.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "TODO";
    public static String TASKS_TABLE = "TASKS";
    public static String COLUMN_ID = "ID";
    public static String COLUMN_NAME = "NAME";
    public static String COLUMN_DUE_DATE = "DATE";
    public static String COLUMN_CREATED_AT = "CREATED";
    public static String COLUMN_IS_COMPLETED = "COMPLETED";
    public static String COLUMN_COLOR = "COLOR";
    public static String COLUMN_CATEGORY = "CATEGORY";

    public static String FINISHED_TABLE = "FINISHED";
    public static String MISSED_TABLE = "MISSED";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TASKS_TABLE + " (ID NUMBER PRIMARY KEY, NAME TEXT,DATE TEXT,CREATED TEXT, COMPLETED TEXT,COLOR TEXT,CATEGORY TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE " + FINISHED_TABLE + " (ID NUMBER PRIMARY KEY, NAME TEXT,DATE TEXT,CREATED TEXT, COMPLETED TEXT,COLOR TEXT,CATEGORY TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE " + MISSED_TABLE + " (ID NUMBER PRIMARY KEY, NAME TEXT,DATE TEXT,CREATED TEXT, COMPLETED TEXT,COLOR TEXT,CATEGORY TEXT);");
    }

    public boolean insertTask(int id, String name, String date, String created, String completed, String color, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DUE_DATE, date);
        contentValues.put(COLUMN_CREATED_AT, created);
        contentValues.put(COLUMN_IS_COMPLETED, completed);
        contentValues.put(COLUMN_COLOR, color);
        contentValues.put(COLUMN_CATEGORY, category);
        db.insert(TASKS_TABLE, null, contentValues);
        db.close();
        return true;
    }
    public boolean insertFinished(int id, String name, String date, String created, String completed, String color, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DUE_DATE, date);
        contentValues.put(COLUMN_CREATED_AT, created);
        contentValues.put(COLUMN_IS_COMPLETED, completed);
        contentValues.put(COLUMN_COLOR, color);
        contentValues.put(COLUMN_CATEGORY, category);
        db.insert(FINISHED_TABLE, null, contentValues);
        db.close();
        return true;
    }
    public boolean insertMissed(int id, String name, String date, String created, String completed, String color, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DUE_DATE, date);
        contentValues.put(COLUMN_CREATED_AT, created);
        contentValues.put(COLUMN_IS_COMPLETED, completed);
        contentValues.put(COLUMN_COLOR, color);
        contentValues.put(COLUMN_CATEGORY, category);
        db.insert(MISSED_TABLE, null, contentValues);
        db.close();
        return true;
    }

    public boolean updateTask(int id, String name, String date, String created, String completed, String color, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DUE_DATE, date);
        contentValues.put(COLUMN_CREATED_AT, created);
        contentValues.put(COLUMN_IS_COMPLETED, completed);
        contentValues.put(COLUMN_COLOR, color);
        contentValues.put(COLUMN_CATEGORY, category);
        db.update(TASKS_TABLE, contentValues, "ID = ? ", new String[]{String.valueOf(id)});
        db.close();
        return true;
    }

    public Integer deleteTask(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TASKS_TABLE,
                "ID = ? ",
                new String[]{Integer.toString(id)});
    }
    public Integer deleteFinished(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FINISHED_TABLE,
                "ID = ? ",
                new String[]{Integer.toString(id)});
    }
    public Integer deleteMissed(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MISSED_TABLE,
                "ID = ? ",
                new String[]{Integer.toString(id)});
    }

    public List<TaskDataModel> getAllTasks() {
        List<TaskDataModel> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TASKS_TABLE, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            int ID = res.getInt(res.getColumnIndex(COLUMN_ID));
            String TITLE = res.getString(res.getColumnIndex(COLUMN_NAME));
            String DATE = res.getString(res.getColumnIndex(COLUMN_DUE_DATE));
            String CREATED = res.getString(res.getColumnIndex(COLUMN_CREATED_AT));
            String COMPLETED = res.getString(res.getColumnIndex(COLUMN_IS_COMPLETED));
            String COLOUR = res.getString(res.getColumnIndex(COLUMN_COLOR));
            String CATEGORY = res.getString(res.getColumnIndex(COLUMN_CATEGORY));
            TaskDataModel data = new TaskDataModel(ID, TITLE,CATEGORY,DATE,COLOUR,COMPLETED.equals("true"),CREATED);
            array_list.add(data);
            res.moveToNext();
        }
        res.close();
        db.close();
        return array_list;
    }
    public List<TaskDataModel> getAllFinished() {
        List<TaskDataModel> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + FINISHED_TABLE, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            int ID = res.getInt(res.getColumnIndex(COLUMN_ID));
            String TITLE = res.getString(res.getColumnIndex(COLUMN_NAME));
            String DATE = res.getString(res.getColumnIndex(COLUMN_DUE_DATE));
            String CREATED = res.getString(res.getColumnIndex(COLUMN_CREATED_AT));
            String COMPLETED = res.getString(res.getColumnIndex(COLUMN_IS_COMPLETED));
            String COLOUR = res.getString(res.getColumnIndex(COLUMN_COLOR));
            String CATEGORY = res.getString(res.getColumnIndex(COLUMN_CATEGORY));
            TaskDataModel data = new TaskDataModel(ID, TITLE,CATEGORY,DATE,COLOUR, true,CREATED);
            array_list.add(data);
            res.moveToNext();
        }
        res.close();
        db.close();
        return array_list;
    }
    public List<TaskDataModel> getAllMissed() {
        List<TaskDataModel> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + MISSED_TABLE, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            int ID = res.getInt(res.getColumnIndex(COLUMN_ID));
            String TITLE = res.getString(res.getColumnIndex(COLUMN_NAME));
            String DATE = res.getString(res.getColumnIndex(COLUMN_DUE_DATE));
            String CREATED = res.getString(res.getColumnIndex(COLUMN_CREATED_AT));
            String COMPLETED = res.getString(res.getColumnIndex(COLUMN_IS_COMPLETED));
            String COLOUR = res.getString(res.getColumnIndex(COLUMN_COLOR));
            String CATEGORY = res.getString(res.getColumnIndex(COLUMN_CATEGORY));
            TaskDataModel data = new TaskDataModel(ID, TITLE,CATEGORY,DATE,COLOUR, false,CREATED);
            array_list.add(data);
            res.moveToNext();
        }
        res.close();
        db.close();
        return array_list;
    }

    public void deleteAllTablesData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TASKS_TABLE + ";");
        db.execSQL("DELETE FROM " + FINISHED_TABLE + ";");
        db.execSQL("DELETE FROM " + MISSED_TABLE + ";");
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FINISHED_TABLE + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MISSED_TABLE + ";");
    }
}
