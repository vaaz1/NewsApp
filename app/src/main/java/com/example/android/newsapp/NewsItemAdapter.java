package com.example.android.newsapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsItemAdapter extends ArrayAdapter<NewsItem> {
    public NewsItemAdapter(Activity context, ArrayList<NewsItem> item) {
        super(context, 0, item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        final NewsItem currentNewsItem = getItem(position);

        View textContainer = convertView.findViewById(R.id.text_container);

        textContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = currentNewsItem.getmUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                view.getContext().startActivity(intent);
            }
        });

        TextView sectionTextView = (TextView) convertView.findViewById(R.id.item_section);
        sectionTextView.setText(currentNewsItem.getmSection());

        TextView titleTextView = (TextView) convertView.findViewById(R.id.item_title);
        titleTextView.setText(currentNewsItem.getmTitle());

        TextView dateTextView = (TextView) convertView.findViewById(R.id.item_date);
        dateTextView.setText(currentNewsItem.getmTime());

        TextView authorTextView = (TextView) convertView.findViewById(R.id.item_author);
        authorTextView.setText(currentNewsItem.getmAuthor());
        if (currentNewsItem.getmAuthor().isEmpty()) {
            authorTextView.setVisibility(View.GONE);
        } else {
            authorTextView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
