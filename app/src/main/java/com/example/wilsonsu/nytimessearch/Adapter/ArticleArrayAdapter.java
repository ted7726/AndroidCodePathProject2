package com.example.wilsonsu.nytimessearch.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wilsonsu.nytimessearch.DataModel.Article;
import com.example.wilsonsu.nytimessearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by wilsonsu on 2/10/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, ArrayList<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item
        Article article = this.getItem(position);
        // check to see if existing view being reused
        // not using a recycled view -> inflate the layout
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }

        // find the image view
        ImageView ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
        // clear out recycled image from convertedView from last time
        ivImage.setImageResource(0);

        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.headline.main);
        String thumbnail = "http://www.nytimes.com/"+article.multimedia.get(0).url;
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(ivImage);
        }

        return convertView;
    }
}
