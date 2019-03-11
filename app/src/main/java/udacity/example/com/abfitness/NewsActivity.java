package udacity.example.com.abfitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import java.util.ArrayList;

import udacity.example.com.abfitness.adapters.NewsAdapter;
import udacity.example.com.abfitness.interfaces.OnNewsClickHandler;
import udacity.example.com.abfitness.model.Fitness;
import udacity.example.com.abfitness.model.News;
import udacity.example.com.abfitness.utils.JsonUtils;
import udacity.example.com.abfitness.utils.NetworkUtils;

import static udacity.example.com.abfitness.utils.NetworkUtils.THE_JSON;

public class NewsActivity extends BaseActivity implements OnNewsClickHandler {

    private static final String TAG = NewsActivity.class.getSimpleName();
    private static final String HEADER = "header";
    private static final String LOGO_URL = "logo_url";
    private static final String ARTICLE = "article";

    private RecyclerView mRecyclerView;
    private ArrayList<News> newsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_news);

        String jsonString = NetworkUtils.getSharedPreferences().getString(THE_JSON,"");
        Fitness fitness = JsonUtils.getNews(jsonString);
        newsList = fitness.getNews();

        mRecyclerView = (RecyclerView)findViewById(R.id.news_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        NewsAdapter adapter = new NewsAdapter(this);
        adapter.setNewsList(newsList);
        mRecyclerView.setAdapter(adapter);



    }

    @Override
    public void onNewsClick(String header, String picUrl, String article) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra(HEADER, header);
        intent.putExtra(LOGO_URL, picUrl);
        intent.putExtra(ARTICLE, article);

        startActivity(intent);
    }
}
