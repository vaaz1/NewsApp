package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {
    private String mUrl;

    public NewsLoader(Context context, String url){
        super (context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        List<NewsItem> result = QueriUtils.fetchNewsData(mUrl);
        return result;
    }
}
