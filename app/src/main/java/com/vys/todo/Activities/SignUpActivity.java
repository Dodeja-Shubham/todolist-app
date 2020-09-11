package com.vys.todo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.vys.todo.APIModels.SignUpResponse;
import com.vys.todo.Class.ApiRequestClass;
import com.vys.todo.Data.SharedPrefs;
import com.vys.todo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vys.todo.Activities.SplashActivity.TOKEN;

public class SignUpActivity extends AppCompatActivity {

    private static String TAG = "SignUpActivity";

    private EditText usernameEt, passwordEt, emailEt;
    private TextView usernameError, passwordError, emailError;
    Button submitBtn;

    LinearLayout progressBar;

    private String username, email, password;

    OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build();
    Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiRequestClass.BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiRequestClass retrofitCall = retrofit.create(ApiRequestClass.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Create Account");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        usernameEt = findViewById(R.id.signup_name_et);
        usernameError = findViewById(R.id.signup_error_name);
        passwordEt = findViewById(R.id.signup_pass_et);
        passwordError = findViewById(R.id.signup_error_pass);
        emailEt = findViewById(R.id.signup_email_et);
        emailError = findViewById(R.id.signup_error_email);
        submitBtn = findViewById(R.id.signup_btn_submit);
        progressBar = findViewById(R.id.signup_progress);

        usernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                usernameError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        emailError.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        submitBtn.setOnClickListener(it -> {
            signUp();
        });
    }

    private boolean validateData() {
        if(usernameEt.getText().toString().trim().isEmpty()){
            usernameError.setText(getString(R.string.field_empty));
            usernameError.setVisibility(View.VISIBLE);
            return false;
        }
        if(passwordEt.getText().toString().trim().isEmpty()){
            passwordError.setText(getString(R.string.field_empty));
            passwordError.setVisibility(View.VISIBLE);
            return false;
        }
        if(emailEt.getText().toString().trim().isEmpty()){
            emailError.setText(getString(R.string.field_empty));
            emailError.setVisibility(View.VISIBLE);
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

    private void signUp() {
        if (validateData()) {
            progressBar.setVisibility(View.VISIBLE);
            enableDisableTouch(false);
            username = usernameEt.getText().toString().trim();
            password = passwordEt.getText().toString();
            email = emailEt.getText().toString().trim();
            RequestBody name = RequestBody.create(MediaType.parse("text/parse"), username);
            RequestBody pass = RequestBody.create(MediaType.parse("text/parse"), password);
            RequestBody em = RequestBody.create(MediaType.parse("text/parse"), email);
            Call<SignUpResponse> call = retrofitCall.registerUser(name, em, pass, pass);
            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    if(response.isSuccessful()){
                        SharedPrefs prefs = new SharedPrefs(SignUpActivity.this);
                        prefs.setIsLoggedIn(true);
                        prefs.setUsername(username);
                        prefs.setPassword(password);
                        prefs.setToken(response.body().getToken());
                        TOKEN = "Token " + response.body().getToken();
                        progressBar.setVisibility(View.GONE);
                        enableDisableTouch(true);
                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                        finish();
                    }else{
                        if(response.code() == 400){
                            try {
                                String data = response.errorBody().string();
                                JSONObject json = new JSONObject(data);
                                json = json.getJSONObject("error");
                                usernameError.setText(json.get("username").toString());
                                usernameError.setVisibility(View.VISIBLE);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Log.e(TAG,response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {

                }
            });
        }
    }
}