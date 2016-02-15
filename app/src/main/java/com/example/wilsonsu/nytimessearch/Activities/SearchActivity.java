package com.example.wilsonsu.nytimessearch.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.wilsonsu.nytimessearch.Adapter.ArticleAdapter;
import com.example.wilsonsu.nytimessearch.DataModel.Article;
import com.example.wilsonsu.nytimessearch.DataModel.ArticleDataModel;
import com.example.wilsonsu.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.wilsonsu.nytimessearch.FilterDialog;
import com.example.wilsonsu.nytimessearch.ItemClickSupport;
import com.example.wilsonsu.nytimessearch.NetworkClient;
import com.example.wilsonsu.nytimessearch.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
//import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchActivity extends AppCompatActivity implements FilterDialog.FilterDialogListener {
    @Bind(R.id.rvResults2) RecyclerView rvResults2;
    @Bind(R.id.rvResults) RecyclerView rvResults;
    @Bind(R.id.search_view) SearchView searchView;
    @Bind(R.id.btn_newest) ImageView btnOrder;
    @Bind(R.id.btn_columns) ImageButton resizeButton;
    @Bind(R.id.search_card_view) View searchCardView;
    private NetworkClient client = new NetworkClient();
    private ArrayList<Article> articles;
    private ArticleAdapter articleAdapter;

    private String settingsNewsDesk;
    private Date settingsDates;
    private boolean orderTheLatest = true;
    private boolean onTwoColumn = true;
    private boolean showSearchBar = true;
    private String queryString;
    private ScaleGestureDetector SGD;
    private float scale = 1f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        setupView();
    }

    private void init() {
        ButterKnife.bind(this);
        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(articles);
    }

    private void setupView() {
        setupRecyclerView(rvResults, 3);
        setupRecyclerView(rvResults2, 2);
        rvResults2.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                SGD.onTouchEvent(e);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryString = query;
                refetchAritcle();
                Toast.makeText(getBaseContext(), "search for \"" + query + "\"", Toast.LENGTH_SHORT).show();
                setShowSearchBar(false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        SGD = new ScaleGestureDetector(this,new ScaleListener());
    }

    private void setupRecyclerView(RecyclerView rvView, int columnNum) {

        rvView.setAdapter(articleAdapter);
        final StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(columnNum, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);
        rvView.setLayoutManager(gridLayoutManager);
        rvView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchAritcle(queryString, page);
            }

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                setShowSearchBar((dy < 0));


                super.onScrolled(view, dx, dy);
            }

        });

//        final SlideInUpAnimator slideInUpAnimator = new SlideInUpAnimator();
//        rvView.setItemAnimator(slideInUpAnimator);
        ItemClickSupport.addTo(rvView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });
    }

    public void setShowSearchBar(boolean showSearchBar) {
        if (this.showSearchBar!=showSearchBar) {
            alphaAnimationCreator(searchCardView, showSearchBar);
            this.showSearchBar = showSearchBar;
        }
    }

    private void refetchAritcle() {

        articles.clear();
        articleAdapter.notifyDataSetChanged();
        fetchAritcle(queryString, 0);
    }

    private void fetchAritcle(String query, int page) {
        if (!isNetworkAvailable()) {
            Toast.makeText(getBaseContext(), "There is no network available", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!client.isOnline()) {
            Toast.makeText(getBaseContext(), "Network is not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        client.requestForSearchArticles(query, orderTheLatest, settingsDates, settingsNewsDesk, page, defaultHandler());
    }

    private void showFilterFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialog filterDialog = FilterDialog.newInstance(settingsNewsDesk, settingsDates);
        filterDialog.show(fm, "settings_fragment");
    }

    @Override
    public void onFinishFilterDialog(String newsDesk, Date date) {
        settingsNewsDesk= newsDesk;
        settingsDates = date;
        refetchAritcle();
    }

    private JsonHttpResponseHandler defaultHandler() {
        return new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                ArticleDataModel articleEntity = gson.fromJson(response.toString(), ArticleDataModel.class);
                articles.addAll(articleEntity.response.articles);
                articleAdapter.notifyDataSetChanged();
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };
    }

    public void onSettingsButton(View view) {
        showFilterFragment();
    }

    public void onOrderButton(View view) {
        orderTheLatest = !orderTheLatest;
        btnOrder.setImageResource(orderTheLatest ? R.drawable.ic_order_newest : R.drawable.ic_order_oldest);
        refetchAritcle();
    }


    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SGD.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void onSettingsColumns(View view) {
        onTwoColumn = !onTwoColumn;
        alphaAnimationCreator(rvResults2, onTwoColumn);
        if(onTwoColumn) {
            resizeButton.setImageResource(R.drawable.ic_resize_decrease);
        } else {
            resizeButton.setImageResource(R.drawable.ic_resize_increase);
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5.0f));
            return true;
        }
    }

    private void alphaAnimationCreator(final View view, final boolean isfadeIn) {
        final float alpha = isfadeIn?0.0f:1.0f;

        view.setVisibility(View.VISIBLE);
        AlphaAnimation fade = new AlphaAnimation(alpha, 1-alpha);
        fade.setAnimationListener(
            new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(isfadeIn?View.VISIBLE:View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            }
        );
        fade.setDuration(1000);
        view.startAnimation(fade);
    }
}
