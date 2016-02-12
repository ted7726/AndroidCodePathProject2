package com.example.wilsonsu.nytimessearch;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by wilsonsu on 2/10/16.
 */
public class NetworkClient {
//        - API-KEY = 45fcda6fe28f92056a19a87c939b477a:5:68404311
//          - http://api.nytimes.com/svc/search/v2/articlesearch?q=yahoo&api-key=45fcda6fe28f92056a19a87c939b477a:5:68404311

        private final String API_BASE_URL = "http://api.nytimes.com/svc/search/v2/";
        private final String API_KEY = "45fcda6fe28f92056a19a87c939b477a:5:68404311";
        private AsyncHttpClient client;

        public NetworkClient() {
            this.client = new AsyncHttpClient();
        }

        private String getApiUrl(String relativeUrl) {
            return API_BASE_URL + relativeUrl;
        }

        public void requestForSearchArticles(String query, JsonHttpResponseHandler handler) {
            String url = getApiUrl("articlesearch.json");
            RequestParams params = new RequestParams();
            params.put("api-key", API_KEY);
            params.put("page", 0);
            params.put("q", query);
            client.get(url, params, handler);
        }


}
