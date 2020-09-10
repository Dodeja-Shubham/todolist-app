package com.vys.todo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.vys.todo.APIModels.TaskResponse;
import com.vys.todo.Class.ApiRequestClass;
import com.vys.todo.R;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vys.todo.Activities.SplashActivity.TOKEN;

public class AddTaskActivity extends AppCompatActivity {

    private final String[] CATEGORIES_LIST = {"Default","Home","Work","Personal","Fitness","Medication"};

    private final String TAG = "AddTaskActivity";
    private EditText taskNameEt, taskDateEt, taskDescEt;
    private LinearLayout holder;
    private TextView tv_error_date, tv_error_name,tv_error_desc;
    private int selectedCategory = 0;
    Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiRequestClass.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiRequestClass retrofitCall = retrofit.create(ApiRequestClass.class);
    Spinner categoriesSpinner;
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
        taskDescEt = findViewById(R.id.add_task_desc_et);
        ImageView iv_calendar = findViewById(R.id.add_task_calendar);
        holder = findViewById(R.id.holder_add_task);
        tv_error_date = findViewById(R.id.add_task_error_date);
        tv_error_name = findViewById(R.id.add_task_error_name);
        tv_error_desc = findViewById(R.id.add_task_error_desc);
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

        taskDateEt.setOnFocusChangeListener((view, b) -> {
            if (b) {
                showDatepicker();
            }
        });

        iv_calendar.setOnClickListener(view -> showDatepicker());

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

        taskDescEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_error_desc.setVisibility(View.GONE);
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
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .displayYears(true)
                .displayDaysOfMonth(true)
                .displayListener(picker -> {
                    //retrieve the SingleDateAndTimePicker
                })
                .title("Task Date & Time")
                .listener(date -> {
                    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                    taskDateEt.setText(format.format(date));
                    tv_error_date.setVisibility(View.INVISIBLE);
                    dueDate = date;
                    holder.requestFocus();
                }).display();
    }

    private void validateData() {
        if(dueDate == null){
            tv_error_date.setText(getString(R.string.date_not_set));
            tv_error_date.setVisibility(View.VISIBLE);
        } else {
            tv_error_date.setVisibility(View.INVISIBLE);
            addData();
        }

    }

    private void addData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TaskResponse obj = new TaskResponse();
        obj.setTitle(taskNameEt.getText().toString());
        obj.setCategory(CATEGORIES_LIST[selectedCategory]);
        obj.setDesc(taskDescEt.getText().toString());
        obj.setDueDate(dateFormat.format(dueDate));
        obj.setColour(colorRandom());
        obj.setIsCompleted(false);
        obj.setCreatedAt(dateFormat.format(Calendar.getInstance().getTime()));
        Call<TaskResponse> call = retrofitCall.setTasks(TOKEN,obj);
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(AddTaskActivity.this,"Task Added",Toast.LENGTH_LONG).show();
                    taskNameEt.setText("");
                    taskDateEt.setText("");
                    finish();
                }else{
                    Toast.makeText(AddTaskActivity.this,"Something wen't wrong",Toast.LENGTH_LONG).show();
                    try {
                        Log.e(TAG,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Toast.makeText(AddTaskActivity.this,"Something wen't wrong",Toast.LENGTH_LONG).show();
                Log.e(TAG,t.getMessage());
            }
        });
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
            if (taskNameEt.getText().toString().trim().isEmpty() || taskDescEt.getText().toString().trim().isEmpty()) {
                if(taskNameEt.getText().toString().trim().isEmpty())
                    tv_error_name.setVisibility(View.VISIBLE);
                if(taskDescEt.getText().toString().trim().isEmpty())
                    tv_error_desc.setVisibility(View.VISIBLE);
            } else {
                tv_error_name.setVisibility(View.INVISIBLE);
                tv_error_desc.setVisibility(View.INVISIBLE);
                validateData();
            }
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private String colorRandom(){
        Random random = new Random();
        int nextInt = random.nextInt(0xffffff + 1);
        return String.format("#%06x", nextInt);
    }
}