package id.java.newsproject.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import id.java.newsproject.rests.APIInterface;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRepository {
    public static final String BASE_URL = "https://newsapi.org/v2/";

    private APIInterface apiInterface;
    private MutableLiveData<ResponseModel> responseModelLiveData;

    public  NewsRepository(){
        responseModelLiveData = new MutableLiveData<>();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        apiInterface = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIInterface.class);
    }

    public void searchNews(String keyword,String country, String apiKey){
        apiInterface.searchQueryCountry(keyword,country,apiKey)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if(response.body() != null){
                            responseModelLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                            responseModelLiveData.postValue(null);
                    }
                });
    }

    public void topHeadlines(String country,String apiKey){
        apiInterface.getLatestNews(country,apiKey)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.body() != null) {
                            responseModelLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        responseModelLiveData.postValue(null);
                    }
                });
    }

    public void getNewsSearch(String keyword,String language, String sortBy,String apiKey){
        apiInterface.getNewsSearch(keyword,language,sortBy,apiKey)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.body() != null){
                            responseModelLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        responseModelLiveData.postValue(null);
                    }
                });
    }



    public LiveData<ResponseModel> getResponseModel(){
        return responseModelLiveData;
    }
}
