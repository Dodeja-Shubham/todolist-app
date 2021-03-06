package com.vys.todo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vys.todo.APIModels.LoginResponse;
import com.vys.todo.Class.ApiRequestClass;
import com.vys.todo.Data.SharedPrefs;
import com.vys.todo.R;

import static com.vys.todo.Activities.SplashActivity.TOKEN;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {



    private final String TAG = "LoginActivity";

    private EditText usernameET, passwordET;
    private TextView usernameError, passwordError,create;
    private Button loginBtn;

    LinearLayout progressBar;

    OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build();
    Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiRequestClass.BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiRequestClass retrofitCall = retrofit.create(ApiRequestClass.class);

    private String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        usernameET = findViewById(R.id.login_name_et);
        passwordET = findViewById(R.id.login_pass_et);
        usernameError = findViewById(R.id.login_error_name);
        passwordError = findViewById(R.id.login_error_pass);
        loginBtn = findViewById(R.id.login_btn_submit);
        create = findViewById(R.id.login_create_account);
        progressBar = findViewById(R.id.login_progress);
        loginBtn.setOnClickListener(view -> {
            if (validateData()) {
                progressBar.setVisibility(View.VISIBLE);
                enableDisableTouch(false);
                username = usernameET.getText().toString().trim();
                password = passwordET.getText().toString();
                RequestBody name = RequestBody.create(MediaType.parse("text/parse"), username);
                RequestBody pass = RequestBody.create(MediaType.parse("text/parse"), password);
                Call<LoginResponse> call = retrofitCall.loginUser(name, pass);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            try{
                                SharedPrefs prefs = new SharedPrefs(LoginActivity.this);
                                prefs.setIsLoggedIn(true);
                                prefs.setUsername(username);
                                prefs.setPassword(password);
                                prefs.setToken(response.body().getKey());
                                TOKEN = "Token " + response.body().getKey();
                                progressBar.setVisibility(View.GONE);
                                enableDisableTouch(true);
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }catch (Exception e){
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(LoginActivity.this,"Something wen't wrong",Toast.LENGTH_LONG).show();
                            }
                        } else if (response.code() == 400) {
                            Toast.makeText(LoginActivity.this, "Unable to log in with provided credentials.", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                Log.e(TAG, response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
            }
        });
        usernameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                usernameError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        create.setOnClickListener(it -> {
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private boolean validateData() {
        if (usernameET.getText().toString().trim().isEmpty() || passwordET.getText().toString().isEmpty()) {

            if (usernameET.getText().toString().trim().isEmpty()) {
                usernameError.setVisibility(View.VISIBLE);
                usernameError.setText("Empty field");
            }
            if (passwordET.getText().toString().isEmpty()) {
                passwordError.setVisibility(View.INVISIBLE);
                passwordError.setText("Empty field");
            }
            return false;
        } else if (usernameET.getText().toString().trim().contains(" ")) {
            usernameError.setVisibility(View.VISIBLE);
            usernameError.setText("Username cannot contain blank spaces");
            return false;
        }


        return true;
    }

    private void enableDisableTouch(boolean type) {
        if (type) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}