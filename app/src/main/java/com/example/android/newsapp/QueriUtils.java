package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueriUtils {

    private static final String LOG_TAG = QueriUtils.class.getSimpleName();

    private QueriUtils(){

    }


    public static List<NewsItem> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<NewsItem> newsItems = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return newsItems;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<NewsItem> extractFeatureFromJson(String newsJSON) {

        if(TextUtils.isEmpty(newsJSON)){
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<NewsItem> news = new ArrayList<>();

        // Try to parse the JSON response. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            // extract response JSONArray
            JSONObject newsArray = baseJsonResponse.getJSONObject("response");
            // Extract “features” JSONArray
            JSONArray features  = newsArray.getJSONArray("results");

            // Loop through each feature in the array
            for(int i=0; i<features.length(); i++){
                // Get newsitem JSONObject at position i
                JSONObject newsitem = features.getJSONObject(i);
                // Extract “webTitle” for title
                String webTitle = newsitem.getString("webTitle");
                String title;
                String author = "";
                if(webTitle.contains("|")){
                    int endOfTitle = webTitle.indexOf("|");
                    title = webTitle.substring(0,endOfTitle);
                    // Extract author
                    author = "by" + webTitle.substring(endOfTitle+1, webTitle.length());
                }
                else {
                    title = webTitle;
                }
                // Extract “sectionName” for section
                String section = newsitem.getString("sectionName");
                // Extract "webPublicationDate" for publication date
                String time = newsitem.getString("webPublicationDate");
                int endOfDate = time.indexOf("T");
                String publicationDate = time.substring(0,endOfDate);
                // Extract the value for the url
                String url = newsitem.getString("webUrl");
                // Create News java object
                NewsItem newsItem = new NewsItem(title,section,publicationDate,author,url);
                // Add newsitem to list of news
                news.add(newsItem);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of news
        return news;
    }
}
