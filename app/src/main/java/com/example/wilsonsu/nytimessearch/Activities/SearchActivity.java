package com.example.wilsonsu.nytimessearch.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.GridView;
import android.widget.Toast;

import com.example.wilsonsu.nytimessearch.DataModel.Article;
import com.example.wilsonsu.nytimessearch.Adapter.ArticleArrayAdapter;
import com.example.wilsonsu.nytimessearch.DataModel.ArticleDataModel;
import com.example.wilsonsu.nytimessearch.FilterDialog;
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

public class SearchActivity extends AppCompatActivity {
    @Bind(R.id.gvResults) GridView gvResults;
    private NetworkClient client = new NetworkClient();
    private ArrayList<Article> articles;
    private ArticleArrayAdapter articleArrayAdapter;

    private String settingsNewsDesk;
    private Date settingsDates;
    private boolean orderTheLatest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupView();
    }

    private void setupView() {
        ButterKnife.bind(this);
        articles = new ArrayList<>();
        articleArrayAdapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(articleArrayAdapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchAritcle(query);
                Toast.makeText(getApplicationContext(), "Search For " + query, Toast.LENGTH_LONG).show();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.set_dates:



                return true;
            case R.id.set_order:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }



        private void fetchAritcle(String query) {
        client.requestForSearchArticles(query, defaultHandler());
    }

    private void showFilterFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialog filterDialog = FilterDialog.newInstance(settingsNewsDesk, settingsDates);
        filterDialog.show(fm, "fragment_edit_name");


    }

    private JsonHttpResponseHandler defaultHandler() {
        return new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                ArticleDataModel articleEntity = gson.fromJson(response.toString(), ArticleDataModel.class);
                articleArrayAdapter.addAll(articleEntity.response.articles);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };
    }
}
