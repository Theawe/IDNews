package id.java.newsproject.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import id.java.newsproject.R;
import id.java.newsproject.adapter.MainArticleAdapter;
import id.java.newsproject.model.Article;
import id.java.newsproject.model.NewsViewModel;
import id.java.newsproject.model.ResponseModel;
import id.java.newsproject.rests.ApiClient;
import id.java.newsproject.rests.APIInterface;
import id.java.newsproject.utils.OnRecyclerViewItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener, AdapterView.OnItemSelectedListener {

    private NewsViewModel newsViewModel;
    private MainArticleAdapter mainArticleAdapter;
    private Spinner spinnerSearch;
    private TextView textView;
    private RelativeLayout relativeSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("IDNews");
        getSupportActionBar().setIcon(R.drawable.logo_app);
        setContentView(R.layout.activity_main);
        relativeSpinner = findViewById(R.id.relative_spinner);
        textView = findViewById(R.id.textView);
        mainArticleAdapter = new MainArticleAdapter();
        mainArticleAdapter.setOnRecyclerViewItemClickListener(MainActivity.this);
        newsViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(NewsViewModel.class);
        newsViewModel.init();
        newsViewModel.getResponseModelLiveData().observe(this, new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                if(responseModel != null){
                    mainArticleAdapter.setResults(responseModel.getArticles());
                }
            }
        });

        RecyclerView mainRecycler = findViewById(R.id.activity_main_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainRecycler.setLayoutManager(linearLayoutManager);

        mainRecycler.setAdapter(mainArticleAdapter);


        newsViewModel.topHeadlines("id");

        spinnerSearch = findViewById(R.id.spinner_country);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(this,R.array.country,android.R.layout.simple_spinner_item);
        spinnerSearch.setAdapter(spinnerAdapter);
        spinnerSearch.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemClick(int position, View view) {
        switch (view.getId()) {
            case R.id.relative_layout:
                Article article = (Article) view.getTag();
                if (!TextUtils.isEmpty(article.getUrl())) {
                    Log.e("clicked url", article.getUrl());
                    Intent detailActivity = new Intent(this, DetailActivity.class);
                    detailActivity.putExtra("url", article.getUrl());
                    detailActivity.putExtra("title", article.getTitle());
                    detailActivity.putExtra("logo",article.getUrlToImage());
                    detailActivity.putExtra("publisher",article.getSource().getName());
                    detailActivity.putExtra("waktu",article.getPublishedAt());
                    detailActivity.putExtra("deskripsi",article.getDescription());
                    detailActivity.putExtra("author",article.getAuthor());
                    startActivity(detailActivity);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(i){
            case 0:
                newsViewModel.topHeadlines("id");
                break;
            case 1:
                newsViewModel.topHeadlines("jp");
                break;
            case 2:
                newsViewModel.topHeadlines("kr");
                break;
            case 3:
                newsViewModel.topHeadlines("us");
                break;
            case 4:
                newsViewModel.topHeadlines("sg");
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu. findItem(R.id.search_news).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.search_news);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Cari Berita");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2){
                    textView.setText("Latest News");
                    spinnerSearch.setVisibility(View.GONE);
                    newsViewModel.getNewsSearch(query);
                    relativeSpinner.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textView.setText("Latest News");
                spinnerSearch.setVisibility(View.GONE);
                relativeSpinner.setVisibility(View.GONE);
                newsViewModel.getNewsSearch(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                textView.setText("Top Headlines");
                spinnerSearch.setVisibility(View.VISIBLE);
                relativeSpinner.setVisibility(View.VISIBLE);
                int i = spinnerSearch.getSelectedItemPosition();
                switch(i){
                    case 0:
                        newsViewModel.topHeadlines("id");
                        break;
                    case 1:
                        newsViewModel.topHeadlines("jp");
                        break;
                    case 2:
                        newsViewModel.topHeadlines("kr");
                        break;
                    case 3:
                        newsViewModel.topHeadlines("us");
                        break;
                    case 4:
                        newsViewModel.topHeadlines("sg");
                        break;
                }
                        return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);
        return true;
    }


}