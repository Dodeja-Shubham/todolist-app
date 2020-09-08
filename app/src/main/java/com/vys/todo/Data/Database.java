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
    public static String COLUMN_DUE_TIME = "TIME";
    public static String COLUMN_CREATED_AT = "CREATED";
    public static String COLUMN_IS_COMPLETED = "COMPLETED";
    public static String COLUMN_COLOR = "COLOR";
    public static String COLUMN_CATEGORY = "CATEGORY";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TASKS_TABLE + " (ID NUMBER PRIMARY KEY, NAME TEXT,DATE TEXT,TIME TEXT,CREATED TEXT, COMPLETED TEXT,COLOR TEXT,CATEGORY TEXT);");
    }

    public boolean insertTask(int id, String name, String date, String time, String created, String completed, String color, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DUE_DATE, date);
        contentValues.put(COLUMN_DUE_TIME, time);
        contentValues.put(COLUMN_CREATED_AT, created);
        contentValues.put(COLUMN_IS_COMPLETED, completed);
        contentValues.put(COLUMN_COLOR, color);
        contentValues.put(COLUMN_CATEGORY, category);
        db.insert(TASKS_TABLE, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TASKS_TABLE);
    }

    public boolean updateContact(int id, String name, String date, String time, String created, String completed, String color, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DUE_DATE, date);
        contentValues.put(COLUMN_DUE_TIME, time);
        contentValues.put(COLUMN_CREATED_AT, created);
        contentValues.put(COLUMN_IS_COMPLETED, completed);
        contentValues.put(COLUMN_COLOR, color);
        contentValues.put(COLUMN_CATEGORY, category);
        db.update(TASKS_TABLE, contentValues, "ID = ? ", new String[]{String.valueOf(id)});
        return true;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public List<TaskDataModel> getAllContacts() {
        List<TaskDataModel> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TASKS_TABLE, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            int ID = res.getInt(res.getColumnIndex(COLUMN_ID));
            String TITLE = res.getString(res.getColumnIndex(COLUMN_NAME));
            String DATE = res.getString(res.getColumnIndex(COLUMN_NAME));
            String TIME = res.getString(res.getColumnIndex(COLUMN_NAME));
            String CREATED = res.getString(res.getColumnIndex(COLUMN_NAME));
            String COMPLETED = res.getString(res.getColumnIndex(COLUMN_NAME));
            String COLOUR = res.getString(res.getColumnIndex(COLUMN_NAME));
            String CATEGORY = res.getString(res.getColumnIndex(COLUMN_CATEGORY));
            TaskDataModel data = new TaskDataModel(ID, TITLE,CATEGORY,DATE,TIME,COLOUR,Boolean.getBoolean(COMPLETED),CREATED);
            array_list.add(data);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE + ";");
    }
}
