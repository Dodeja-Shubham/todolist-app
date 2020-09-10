package com.vys.todo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vys.todo.APIModels.LoginResponse;
import com.vys.todo.Class.ApiRequestClass;
import com.vys.todo.Data.SharedPrefs;
import com.vys.todo.R;

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

public class SplashActivity extends AppCompatActivity {
    
    ProgressBar progressBar;

    public static String TOKEN = "";

    private final String TAG = "SplashActivity";
    
    String username,password;

    OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build();
    Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiRequestClass.BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiRequestClass retrofitCall = retrofit.create(ApiRequestClass.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.splash_progress);


        SharedPrefs prefs = new SharedPrefs(this);
        if(prefs.getIsLoggedIn()){
            progressBar.setVisibility(View.VISIBLE);
            System.out.println("username = " + prefs.getUsername());
            System.out.println("password = " + prefs.getPassword());
            username = prefs.getUsername();
            password = prefs.getPassword();
            RequestBody name = RequestBody.create(MediaType.parse("text/parse"), username);
            RequestBody pass = RequestBody.create(MediaType.parse("text/parse"), password);
            Call<LoginResponse> call = retrofitCall.loginUser(name, pass);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        try{
                            SharedPrefs prefs = new SharedPrefs(SplashActivity.this);
                            prefs.setIsLoggedIn(true);
                            prefs.setUsername(username);
                            prefs.setPassword(password);
                            prefs.setToken(response.body().getKey());
                            TOKEN = "Token " + response.body().getKey();
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));
                            finish();
                        }catch (Exception e){
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(SplashActivity.this,"Something wen't wrong",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                            finish();
                        }
                    }else {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        finish();
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
        }else{
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            finish();
        }
    }
}