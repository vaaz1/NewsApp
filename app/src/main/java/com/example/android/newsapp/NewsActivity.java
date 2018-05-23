package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsItem>> {

    public static final int NEWS_LOADER_ID = 1;
    private static final String REQUEST_URL = "http://content.guardianapis.com/search?";
    private static final String PAGE_SIZE = "25";
    private static final String API_KEY = "test";
    ProgressBar progressBar;
    boolean isConnected;
    private TextView mEmptyStateTextView;
    private NewsItemAdapter mNewsItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        progressBar = findViewById(R.id.progress_bar);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String newestDate = sharedPrefs.getString(
                getString(R.string.settings_newest_date_key),
                getString(R.string.settings_newest_date_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String searchKeyword = sharedPrefs.getString(
                getString(R.string.settings_search_keyword_key),
                getString(R.string.settings_search_keyword_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`

        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", PAGE_SIZE);
        uriBuilder.appendQueryParameter("from-date", newestDate);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("q",searchKeyword);
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        Log.v("NewsActivity", "URL: " + uriBuilder.toString());
        // Return the completed uri http://content.guardianapis.com/search?order-by=newest&page-size=25&q=GOOGLE&api-key=431fa36e-151d-4260-afcc-0009afa0e57b
        return new NewsLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        mNewsItemAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mNewsItemAdapter.addAll(data);
        }
        if (isConnected) {
            mEmptyStateTextView.setText(R.string.empty_view);
        } else {
            mEmptyStateTextView.setText(R.string.no_connection);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        mNewsItemAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sttings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
