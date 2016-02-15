package com.example.wilsonsu.nytimessearch.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wilsonsu.nytimessearch.DataModel.Article;
import com.example.wilsonsu.nytimessearch.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wilsonsu on 2/10/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ARTICLE_TYPE_IMAGE = 0, ARTICLE_TYPE_TEXT = 1;
    private ArrayList<Article> articles;

    public ArticleAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder viewHolder;
        // Inflate the custom layout
        if (viewType ==  ARTICLE_TYPE_IMAGE) {
            view = inflater.inflate(R.layout.item_article_result, parent, false);
            viewHolder = new ImageViewHolder(context, view);
        } else {
            view = inflater.inflate(R.layout.item_article_result2, parent, false);
            viewHolder = new TextViewHolder(context, view);
        }
        // Return a new holder instance
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = articles.get(position);
        if (holder.getItemViewType() == ARTICLE_TYPE_IMAGE) {
            ImageViewHolder viewHolder = (ImageViewHolder) holder;
            viewHolder.setArticle(article);
        }
        else {
            TextViewHolder viewHolder = (TextViewHolder) holder;
            viewHolder.setArticle(article);
        }
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        Article article = articles.get(position);
        if (article.multimedia.size()>0) {
            return ARTICLE_TYPE_IMAGE;
        }
        return ARTICLE_TYPE_TEXT;
    }



    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        static ArrayList<Article> articles;
        @Bind(R.id.ivImage) ImageView ivImage;
        @Bind(R.id.tvTitle) TextView tvTitle;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ImageViewHolder(Context context, final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ImageViewHolder instance.
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
                    Glide.with(context).load(imageURL).override(image.width, image.height).fitCenter().placeholder(R.drawable.ic_pics).into(ivImage);
                }
            }
        }
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder{
        static ArrayList<Article> articles;

        @Bind(R.id.tvTextTitle) TextView tvTitle;
        @Bind(R.id.tvTextSubTitle) TextView tvSubTitle;

        public TextViewHolder(Context context, final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setArticle(Article article) {
            tvTitle.setText(article.headline.main);
            tvSubTitle.setText(article.source);
        }
    }
}
