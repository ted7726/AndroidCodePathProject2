package com.example.wilsonsu.nytimessearch.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toolbar;

import com.example.wilsonsu.nytimessearch.DataModel.Article;
import com.example.wilsonsu.nytimessearch.R;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Article article= (Article) getIntent().getSerializableExtra("article");
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
        wvArticle.loadUrl(article.url);
    }
}
