package com.peehu.taskdemoyestitlabs;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface API_Interface {
    @Headers("Accept: application/json")
    @GET("/json/airline-tickets.php")
    Call<List<ResponseModel>> GetJetLists(@Query("from") String from,
                                          @Query("to") String to);

    @Headers("Accept: application/json")
    @GET("/json/airline-tickets-price.php")
    Call<Pojo_Details> GetDetails(@Query("flight_number") String flight_number,
                                   @Query("from") String from,
                                   @Query("to") String to);
}
