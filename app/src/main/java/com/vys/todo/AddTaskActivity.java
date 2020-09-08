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

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

public class AddTaskActivity extends AppCompatActivity {

    private final String[] CATEGORIES_LIST = {"Default","Home","Work","Personal","Fitness","Medication"};

    private final String TAG = "AddTaskActivity";
    private EditText taskNameEt, taskDateEt;
    private LinearLayout holder;
    private TextView tv_error_date, tv_error_name;
    private int selectedCategory = 0;

    Spinner categoriesSpinner;

    private Calendar calendar = Calendar.getInstance();
    private Date dueDate;

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
        ImageView iv_calendar = findViewById(R.id.add_task_calendar);
        holder = findViewById(R.id.holder_add_task);
        tv_error_date = findViewById(R.id.add_task_error_date);
        tv_error_name = findViewById(R.id.add_task_error_name);
        categoriesSpinner = findViewById(R.id.add_task_category_spinner);

        categoriesSpinner.setAdapter(new ArrayAdapter<String>(AddTaskActivity.this,
                R.layout.spinner_dropdown_item, R.id.spinner_item_tv, CATEGORIES_LIST));

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

        iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepicker();
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
        new SingleDateAndTimePickerDialog.Builder(AddTaskActivity.this)
                .curved()
                .minutesStep(1)
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        //retrieve the SingleDateAndTimePicker
                    }
                })
                .title("Task Date & Time")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        taskDateEt.setText(date.toLocaleString());
                        tv_error_date.setVisibility(View.INVISIBLE);
                        dueDate = date;
                        holder.requestFocus();
                    }
                }).display();
    }

    private void validateData() {
        Date todaysDate = calendar.getTime();
        if(dueDate == null){
            tv_error_date.setText(getString(R.string.date_not_set));
            tv_error_date.setVisibility(View.VISIBLE);
        }
        else if(todaysDate.compareTo(dueDate) > 0){
            tv_error_date.setText(getString(R.string.cannot_set_a_reminder_for_past));
            tv_error_date.setVisibility(View.VISIBLE);
        } else {
            tv_error_date.setVisibility(View.INVISIBLE);
            addDataToDb();
        }

    }

    private void addDataToDb() {
        Database db = new Database(AddTaskActivity.this);
        int id = new Random().nextInt(10000000);
        if (db.insertTask(id, taskNameEt.getText().toString(), dueDate.toString(),
                calendar.getTime().toString(), "false", "#FFFFFF", CATEGORIES_LIST[selectedCategory])) {
            Toast.makeText(AddTaskActivity.this, "Task Added", Toast.LENGTH_LONG).show();
            finish();
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
            validateData();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}