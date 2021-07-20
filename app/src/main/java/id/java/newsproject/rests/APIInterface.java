package id.java.newsproject.rests;

import id.java.newsproject.model.ResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {



    @GET("everything")
    Call<ResponseModel> getNewsSearch(@Query("q")String keyword,
                                      @Query("language") String language,
                                      @Query("sortBy")String sortBy,
                                      @Query("apiKey")String apiKey);


    @GET("top-headlines")
    Call<ResponseModel> getLatestNews(@Query("country") String source, @Query("apiKey") String apiKey);

    @GET("top-headlines")
    Call<ResponseModel> searchQueryCountry(@Query("q")String search,@Query("country")String country,@Query("apiKey")String apiKey);

    @GET("top-headlines")
    Call<ResponseModel> searchQuery(@Query("q")String search,@Query("apiKey")String apiKey);



}
