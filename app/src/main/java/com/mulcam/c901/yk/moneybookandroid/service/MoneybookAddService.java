package com.mulcam.c901.yk.moneybookandroid.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by student on 2017-06-22.
 */

public interface MoneybookAddService {
    @GET("moneybookAdd.do")
    Call<Integer> moneybookAddService(
            @Query("id_index") int id_index,
            @Query("date")String date,
            @Query("category")String category,
            @Query("content")String content,
            @Query("price")int price
    );

}
