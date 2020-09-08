package com.vys.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class AddTaskActivity extends AppCompatActivity {

    public static String[] CATEGORIES_LIST = {"Default","Home","Work","Personal","Fitness","Medication"};

    private final String TAG = "AddTaskActivity";
    private EditText taskNameEt, taskDateEt, taskTimeEt;
    private LinearLayout holder;
    private TextView tv_error_date, tv_error_time, tv_error_name;
    private int selectedCategory = 0;

    Spinner categoriesSpinner;

    private Calendar calendar = Calendar.getInstance();
    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;
    private int mHour = -1;
    private int mMinute = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("New Task");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e(TAG, "getSupportActionBar()");
        }

        taskNameEt = findViewById(R.id.add_task_name_et);
        taskDateEt = findViewById(R.id.add_task_date_et);
        taskTimeEt = findViewById(R.id.add_task_time_et);
        ImageView iv_calendar = findViewById(R.id.add_task_calendar);
        ImageView iv_clock = findViewById(R.id.add_task_time);
        holder = findViewById(R.id.holder_add_task);
        tv_error_date = findViewById(R.id.add_task_error_date);
        tv_error_time = findViewById(R.id.add_task_error_time);
        tv_error_name = findViewById(R.id.add_task_error_name);
        categoriesSpinner = findViewById(R.id.add_task_category_spinner);

        categoriesSpinner.setAdapter(new ArrayAdapter<String>(AddTaskActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, CATEGORIES_LIST));

        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        taskDateEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatepicker();
                }
            }
        });

        taskTimeEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showTimepicker();
                }
            }
        });

        iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepicker();
            }
        });

        iv_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimepicker();
            }
        });

        taskNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_error_name.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void showDatepicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mMonth = monthOfYear + 1;
                        mYear = year;
                        mDay = dayOfMonth;
                        String month = "";
                        switch (mMonth) {
                            case 1: {
                                month = "January";
                                break;
                            }
                            case 2: {
                                month = "February";
                                break;
                            }
                            case 3: {
                                month = "March";
                                break;
                            }
                            case 4: {
                                month = "April";
                                break;
                            }
                            case 5: {
                                month = "May";
                                break;
                            }
                            case 6: {
                                month = "June";
                                break;
                            }
                            case 7: {
                                month = "July";
                                break;
                            }
                            case 8: {
                                month = "August";
                                break;
                            }
                            case 9: {
                                month = "September";
                                break;
                            }
                            case 10: {
                                month = "October";
                                break;
                            }
                            case 11: {
                                month = "November";
                                break;
                            }
                            case 12: {
                                month = "December";
                                break;
                            }
                        }
                        taskDateEt.setText(mDay + "-" + month + "-" + mYear);
                        holder.requestFocus();
                        tv_error_date.setVisibility(View.INVISIBLE);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimepicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {
                        mHour = pHour;
                        mMinute = pMinute;
                        taskTimeEt.setText(mHour + ":" + mMinute);
                        holder.requestFocus();
                        tv_error_time.setVisibility(View.INVISIBLE);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void validateData() {
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        Log.e(TAG, "current timestamp: " + currentDay + "/" + currentMonth + "/" + currentYear + " -- " + currentHour + ":" + currentMinute);
        Log.e(TAG, "selected timestamp: " + mDay + "/" + mMonth + "/" + mYear + " -- " + mHour + ":" + mMinute);

        if (currentYear > mYear) {
            Log.e(TAG, "year");
            tv_error_date.setVisibility(View.VISIBLE);
            tv_error_date.setText(getString(R.string.cannot_set_a_reminder_for_past));
            tv_error_time.setVisibility(View.INVISIBLE);
        } else if (currentMonth > mMonth) {
            Log.e(TAG, "month");
            tv_error_date.setVisibility(View.VISIBLE);
            tv_error_date.setText(getString(R.string.cannot_set_a_reminder_for_past));
            tv_error_time.setVisibility(View.INVISIBLE);
        } else if (currentDay > mDay) {
            Log.e(TAG, "day");
            tv_error_date.setVisibility(View.VISIBLE);
            tv_error_date.setText(getString(R.string.cannot_set_a_reminder_for_past));
            tv_error_time.setVisibility(View.INVISIBLE);
        } else if ((currentDay == mDay && currentMonth == mMonth && currentYear == mYear) && currentHour > mHour) {
            tv_error_time.setVisibility(View.VISIBLE);
            tv_error_time.setText(getString(R.string.cannot_set_a_reminder_for_past));
            tv_error_date.setVisibility(View.INVISIBLE);
        } else if ((currentDay == mDay && currentMonth == mMonth && currentYear == mYear) && currentMinute > mMinute) {
            tv_error_time.setVisibility(View.VISIBLE);
            tv_error_time.setText(getString(R.string.cannot_set_a_reminder_for_past));
            tv_error_date.setVisibility(View.INVISIBLE);
        } else {
            tv_error_date.setVisibility(View.INVISIBLE);
            tv_error_time.setVisibility(View.INVISIBLE);
            addDataToDb();
        }
    }

    private void addDataToDb() {
        Database db = new Database(AddTaskActivity.this);
        int id = new Random().nextInt(10000000);
        if (db.insertTask(id, taskNameEt.getText().toString(), taskDateEt.getText().toString(), taskTimeEt.getText().toString(),
                calendar.getTime().toString(), "false", "#FFFFFF", CATEGORIES_LIST[selectedCategory])) {
            Toast.makeText(AddTaskActivity.this, "Task Added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AddTaskActivity.this, "Unable to add task", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_task_menu_check) {
            if (taskNameEt.getText().toString().trim().isEmpty()) {
                tv_error_name.setVisibility(View.VISIBLE);
            } else {
                tv_error_name.setVisibility(View.INVISIBLE);
            }
            if (mYear == -1 || mMonth == -1 || mDay == -1) {
                tv_error_date.setText(getString(R.string.date_not_set));
                tv_error_date.setVisibility(View.VISIBLE);
            }
            if (mHour == -1 || mMinute == -1) {
                tv_error_time.setText(getString(R.string.time_not_set));
                tv_error_time.setVisibility(View.VISIBLE);
            }
            if (mYear != -1 && mMonth != -1 && mDay != -1 && mHour != -1 && mMinute != -1) {
                validateData();
            }
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}