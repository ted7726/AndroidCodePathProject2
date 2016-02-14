package com.example.wilsonsu.nytimessearch.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchActivity extends AppCompatActivity implements FilterDialog.FilterDialogListener {
//    @Bind(R.id.gvResults) GridView gvResults;
    @Bind(R.id.rvResults) RecyclerView rvResults;
    @Bind(R.id.search_view) SearchView searchView;
    @Bind(R.id.btn_settings) ImageButton btnSettings;
    @Bind(R.id.btn_order) ImageButton btnOrder;
    private NetworkClient client = new NetworkClient();
    private ArrayList<Article> articles;
    private ArticleAdapter articleAdapter;

    private String settingsNewsDesk;
    private Date settingsDates;
    private boolean orderTheLatest = true;
    private String queryString;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupView();
    }

    private void setupView() {
        ButterKnife.bind(this);
        currentPage = 0;
        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(articles);
        rvResults.setAdapter(articleAdapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);
        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreAritcle();
            }
        });
        rvResults.setItemAnimator(new SlideInUpAnimator());
        ItemClickSupport.addTo(rvResults).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryString = query;
                fetchAritcle(query);
                Toast.makeText(getBaseContext(), query,
                        Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
    private void fetchAritcle() {
        articles.clear();
        articleAdapter.notifyDataSetChanged();
        currentPage = 0;
        fetchAritcle(queryString);
    }

    private void loadMoreAritcle() {
        ++currentPage;
        client.requestForSearchArticles(queryString, orderTheLatest, settingsDates, settingsNewsDesk, currentPage, loadMoreHandler()
        );
    }

    private void fetchAritcle(String query) {
        client.requestForSearchArticles(query, orderTheLatest, settingsDates, settingsNewsDesk, currentPage, defaultHandler());
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
        fetchAritcle();
    }

    private JsonHttpResponseHandler defaultHandler() {
        return new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                parsingHelper(response);
                articleAdapter.notifyDataSetChanged();
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };
    }

    private JsonHttpResponseHandler loadMoreHandler() {
        return new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ArticleDataModel articleDataModel = parsingHelper(response);
                int curSize = articleAdapter.getItemCount();
                articleAdapter.notifyItemRangeInserted(curSize, articleDataModel.response.articles.size() - 1);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };
    }

    private ArticleDataModel parsingHelper(JSONObject response) {
        Gson gson = new Gson();
        ArticleDataModel articleEntity = gson.fromJson(response.toString(), ArticleDataModel.class);
        articles.addAll(articleEntity.response.articles);
        return articleEntity;
    }

    public void onSettingsButton(View view) {
        showFilterFragment();
    }

    public void onOrderButton(View view) {
        orderTheLatest = !orderTheLatest;
        btnOrder.setAlpha(orderTheLatest?0.4f:1.0f);
        fetchAritcle();
    }

}
