package com.example.wilsonsu.nytimessearch;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wilsonsu on 2/10/16.
 */
public class NetworkClient {
//        - API-KEY = 45fcda6fe28f92056a19a87c939b477a:5:68404311
//          - http://api.nytimes.com/svc/search/v2/articlesearch?q=yahoo&api-key=45fcda6fe28f92056a19a87c939b477a:5:68404311

        private final String API_BASE_URL = "http://api.nytimes.com/svc/search/v2/";
        private final String API_KEY = "45fcda6fe28f92056a19a87c939b477a:5:68404311";
        private AsyncHttpClient client;
        private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

        public NetworkClient() {
            this.client = new AsyncHttpClient();
        }

        private String getApiUrl(String relativeUrl) {
            return API_BASE_URL + relativeUrl;
        }

        public void requestForSearchArticles(String query, JsonHttpResponseHandler handler) {
            requestForSearchArticles(query, true, new Date(), "", 0, handler);
        }

        public void requestForSearchArticles(String query, boolean isNewestOrder, Date date, String newDesk, int page,  JsonHttpResponseHandler handler) {
            String url = getApiUrl("articlesearch.json");
            RequestParams params = new RequestParams();
            params.put("api-key", API_KEY);
            params.put("q", query);
            params.put("sort", isNewestOrder ? "newest" : "oldest");
            params.put("page",page);

            if (!TextUtils.isEmpty(newDesk)) {
                List<String> newDesks = new ArrayList<String>();
                if (newDesk.contains("Sports")) {
                    newDesks.add("Sports");
                }
                if (newDesk.contains("Fashion") || newDesk.contains("Style")) {
                    newDesks.add("Fashion & Style");
                }
                if (newDesk.contains("Arts")) {
                    newDesks.add("Arts");
                }
                params.put("fq", "news_desk:(\"" + TextUtils.join("\" \"", newDesks) + "\")");
            }

            if (date!=null) {
                params.put("begin_date", dateFormatter.format(date));
            }
            client.get(url, params, handler);
        }


}
