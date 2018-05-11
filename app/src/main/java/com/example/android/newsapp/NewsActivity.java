package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsItem>>{

    private TextView mEmptyStateTextView;
    ProgressBar progressBar;
    boolean isConnected;
    private static final String REQUEST_URL = "http://content.guardianapis.com/search?order-by=newest&page-size=25&q=google&api-key=431fa36e-151d-4260-afcc-0009afa0e57b";
    private NewsItemAdapter mNewsItemAdapter;

    public static final int NEWS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        progressBar = findViewById(R.id.progress_bar);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        ListView newsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        mNewsItemAdapter = new NewsItemAdapter(this, new ArrayList<NewsItem>());
        newsListView.setAdapter(mNewsItemAdapter);

    }


    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        mNewsItemAdapter.clear();
        if(data != null && !data.isEmpty()){
            mNewsItemAdapter.addAll(data);
        }
        if(isConnected){
            mEmptyStateTextView.setText(R.string.empty_view);
        }
        else {
            mEmptyStateTextView.setText(R.string.no_connection);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        mNewsItemAdapter.clear();
    }
}
