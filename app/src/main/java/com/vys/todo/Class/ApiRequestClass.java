package com.vys.todo.Class;

import com.android.volley.toolbox.StringRequest;
import com.vys.todo.APIModels.LoginResponse;
import com.vys.todo.APIModels.SignUpResponse;
import com.vys.todo.APIModels.TaskResponse;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiRequestClass {

    public static String BASE_URL = "https://intense-plateau-34888.herokuapp.com/";

    @Multipart
    @POST("/user/login/")
    Call<LoginResponse> loginUser(@Part("username") RequestBody username,
                                  @Part("password") RequestBody password);

    @Multipart
    @POST("/user/login/")
    Call<SignUpResponse> registerUser(@Part("username") RequestBody username,
                                      @Part("email") RequestBody email,
                                      @Part("password") RequestBody password,
                                      @Part("password2") RequestBody password2);

    @GET("/todo/")
    Call<List<TaskResponse>> getTasks(@Header("Authorization") String authorization);

    @POST("/todo/")
    Call<TaskResponse> setTasks(@Header("Authorization") String authorization,
                                      @Body TaskResponse data);

    @PATCH("/todo/{id}")
    Call<TaskResponse> updateTask(@Header("Authorization") String authorization,
                                  @Path("id") int id,
                                  @Body JSONObject data);
}
