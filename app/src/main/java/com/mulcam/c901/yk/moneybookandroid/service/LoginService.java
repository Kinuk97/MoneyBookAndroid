package com.mulcam.c901.yk.moneybookandroid.service;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by student on 2017-06-14.
 */

public interface LoginService {
    @POST("login.do")
    Call<HashMap<String, Object>> login(
            @Query("id")String id,
            @Query("pwd")String pwd);
}
