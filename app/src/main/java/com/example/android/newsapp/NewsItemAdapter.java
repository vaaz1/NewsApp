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


    private static class ViewHolder{
        private TextView sectionTextView;
        private TextView titleTextView;
        private TextView dateTextView;
        private TextView authorTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.sectionTextView = convertView.findViewById(R.id.item_section);
            holder.titleTextView = convertView.findViewById(R.id.item_title);
            holder.dateTextView = convertView.findViewById(R.id.item_date);
            holder.authorTextView = convertView.findViewById(R.id.item_author);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
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

        holder.sectionTextView.setText(currentNewsItem.getmSection());
        holder.titleTextView.setText(currentNewsItem.getmTitle());
        holder.dateTextView.setText(currentNewsItem.getmTime());
        holder.authorTextView.setText(currentNewsItem.getmAuthor());

        if (currentNewsItem.getmAuthor().isEmpty()) {
            holder.authorTextView.setVisibility(View.GONE);
        } else {
            holder.authorTextView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
