package com.vys.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private final String TAG = "AddTaskActivity";
    private EditText taskNameEt, taskDateEt, taskTimeEt;
    private ImageView iv_calendar, iv_clock;
    private LinearLayout holder;
    private TextView tv_error_date, tv_error_time, tv_error_name;

    private Calendar calendar = Calendar.getInstance();
    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private int mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private int mMinute = Calendar.getInstance().get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        toolbar = findViewById(R.id.toolbar);
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
        iv_calendar = findViewById(R.id.add_task_calendar);
        iv_clock = findViewById(R.id.add_task_time);
        holder = findViewById(R.id.holder_add_task);
        tv_error_date = findViewById(R.id.add_task_error_date);
        tv_error_time = findViewById(R.id.add_task_error_time);
        tv_error_name = findViewById(R.id.add_task_error_name);

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
    }

    private void showDatepicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                        int currentMonth = calendar.get(Calendar.MONTH) + 1;
                        int currentYear = calendar.get(Calendar.YEAR);

                        mMonth = monthOfYear + 1;
                        mYear = year;
                        mDay = dayOfMonth;
                        taskDateEt.setText(mDay + "-" + mMonth + "-" + mYear);
                        holder.requestFocus();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimepicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {
                        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = calendar.get(Calendar.MINUTE);

                        mHour = pHour;
                        mMinute = pMinute;
                        taskTimeEt.setText(mHour + ":" + mMinute);
                        holder.requestFocus();
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
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
            Log.e(TAG, "Submit Pressed");
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}