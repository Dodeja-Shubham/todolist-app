package com.vys.todo.Class;

import com.vys.todo.APIModels.LoginResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiRequestClass {

    public static String BASE_URL = "https://intense-plateau-34888.herokuapp.com/";

//    @Multipart
//    @POST("v1/accounts/registration/")
//    Call<RegistrationModel> RegisterUser(@Part("email") RequestBody email,
//                                         @Part("password1") RequestBody password1,
//                                         @Part("password2") RequestBody password2,
//                                         @Part("phone") RequestBody phone);

    @Multipart
    @POST("/user/login/")
    Call<LoginResponse> loginUser(@Part("username") RequestBody username,
                                  @Part("password") RequestBody password);
}
