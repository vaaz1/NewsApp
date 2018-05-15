package com.example.android.newsapp;

public class NewsItem {

    private String mTitle;
    private String mSection;
    private String mTime;
    private String mAuthor;
    private String mUrl;

    public NewsItem(String title, String section, String time, String author, String url) {
        mTitle = title;
        mSection = section;
        mTime = time;
        mAuthor = author;
        mUrl = url;
    }

    public NewsItem(String title, String section, String time, String url) {
        mTitle = title;
        mSection = section;
        mTime = time;
        mUrl = url;
    }

    public NewsItem(String title, String section, String url) {
        mTitle = title;
        mSection = section;
        mUrl = url;
    }


    public String getmTitle() {
        return mTitle;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmUrl() {
        return mUrl;
    }
}
