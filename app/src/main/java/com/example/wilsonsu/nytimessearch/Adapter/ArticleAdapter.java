package com.example.wilsonsu.nytimessearch.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wilsonsu.nytimessearch.Activities.ArticleActivity;
import com.example.wilsonsu.nytimessearch.DataModel.Article;
import com.example.wilsonsu.nytimessearch.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wilsonsu on 2/10/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private ArrayList<Article> articles;

    public ArticleAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data item
        Article article = articles.get(position);
        // check to see if existing view being reused
        holder.setArticle(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        static ArrayList<Article> articles;
        @Bind(R.id.ivImage) ImageView ivImage;
        @Bind(R.id.tvTitle) TextView tvTitle;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setArticle(Article article) {
            // find the image view
            // clear out recycled image from convertedView from last time
            ivImage.setImageResource(0);
            tvTitle.setText(article.headline.main);
            if (article.multimedia.size()>0) {
                Article.MultimediaEntity image = article.multimedia.get(0);
                String imageURL = "http://www.nytimes.com/"+image.url;
                if (!TextUtils.isEmpty(image.url)) {
                    Context context = ivImage.getContext();
                    Glide.with(context).load(imageURL).override(image.width, image.height).fitCenter().into(ivImage);
                }
            }
        }
    }
}
