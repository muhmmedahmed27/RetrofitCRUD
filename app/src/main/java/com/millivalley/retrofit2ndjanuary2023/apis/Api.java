package com.millivalley.retrofit2ndjanuary2023.apis;

import com.millivalley.retrofit2ndjanuary2023.models.DeleteModel;
import com.millivalley.retrofit2ndjanuary2023.models.Hero;
import com.millivalley.retrofit2ndjanuary2023.models.Insert;
import com.millivalley.retrofit2ndjanuary2023.models.Root;
import com.millivalley.retrofit2ndjanuary2023.models.update;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
//    String BASE_URL = "https://simplifiedcoding.net/demos/";
    String BASE_URL = "http://192.168.100.165/brilliance28Dec/";

//    @GET("marvel")
//    Call<List<Hero>> getHeroes();
    @GET("select.php")
    Call<List<Root>> getCustomer();
    @POST("insert.php")
    @FormUrlEncoded
    Call<Insert> AddCustomer(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone
            );
    @POST("update.php")
    @FormUrlEncoded
    Call<update> UpdateCustomer(
            @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone
    );
    @POST("delete.php")
    @FormUrlEncoded
    Call<DeleteModel> DeleteCustomer(
            @Field("id") String id
    );
}
