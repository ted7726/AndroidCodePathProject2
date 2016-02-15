package com.example.wilsonsu.nytimessearch.Activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.wilsonsu.nytimessearch.DataModel.Article;
import com.example.wilsonsu.nytimessearch.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {
    @Bind(R.id.wvArticle) WebView wvArticle;
    private Article article;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);

        Article article= (Article) getIntent().getParcelableExtra("article");
        this.article = article;
        WebView wvArticle = (WebView) findViewById(R.id.wvArticle);
        // Configure related browser settings
        wvArticle.getSettings().setLoadsImagesAutomatically(true);
        wvArticle.getSettings().setJavaScriptEnabled(true);
        wvArticle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        wvArticle.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // Load the initial URL
        if (!TextUtils.isEmpty(article.url)) {
            wvArticle.loadUrl(article.url);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        ShareActionProvider actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());

        Intent shareEmailIntent = new Intent(Intent.ACTION_SENDTO);
        shareEmailIntent.setType("text/plain");
        shareEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"codepath@gmail.com" });
        shareEmailIntent.putExtra(Intent.EXTRA_SUBJECT, article.headline.main);
        shareEmailIntent.putExtra(Intent.EXTRA_TEXT, article.url);
        actionProvider.setShareIntent(shareIntent);
//        actionProvider.setShareIntent(shareEmailIntent);

        return super.onCreateOptionsMenu(menu);
    }
}
