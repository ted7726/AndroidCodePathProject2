package com.example.wilsonsu.nytimessearch.DataModel;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by wilsonsu on 2/10/16.
 */
public class ArticleDataModel {


    public ResponseEntity response;

    public static class ResponseEntity {
        @SerializedName("docs")
        public List<Article> articles;
    }
}
