package id.java.newsproject.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import id.java.newsproject.activity.MainActivity;

public class NewsViewModel extends AndroidViewModel {

    private static final String API_KEY = "b55984b591744a5ba9d8dfcf5dfd0572";

    private NewsRepository newsRepository;
    private LiveData<ResponseModel> responseModelLiveData;


    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        newsRepository = new NewsRepository();
        responseModelLiveData = newsRepository.getResponseModel();
    }

    public void searchNews(String keyword,String country){
        newsRepository.searchNews(keyword,country, API_KEY);
    }

    public void topHeadlines(String country){
        newsRepository.topHeadlines(country,API_KEY);
    }

    public void getNewsSearch(String keyword){
        newsRepository.getNewsSearch(keyword,"en","publishedAt",API_KEY);
    }

    public LiveData<ResponseModel> getResponseModelLiveData(){
        return responseModelLiveData;
    }

}
