package com.example.wilsonsu.nytimessearch.DataModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by wilsonsu on 2/10/16.
 */

public class Article implements Serializable {

    @SerializedName("web_url")
    public String url;
    /**
     * main : Marissa Mayer Defends Yahoo Strategy
     * sub : Yahoo CEO: Coming from position of strength
     */

    public HeadlineEntity headline;
    @SerializedName("pub_date")
    public Date date;
    /**
     * width : 190
     * url : images/2016/02/03/business/cnbc-yahoo/cnbc-yahoo-thumbWide.png
     * height : 126
     * subtype : wide
     * legacy : {"wide":"images/2016/02/03/business/cnbc-yahoo/cnbc-yahoo-thumbWide.png","wideheight":"126","widewidth":"190"}
     * type : image
     */

    public List<MultimediaEntity> multimedia;

    public static class HeadlineEntity implements Serializable {
        public String main;
    }

    public static class MultimediaEntity implements Serializable {
        public int width;
        public String url;
        public int height;
        public String type;
    }
}


