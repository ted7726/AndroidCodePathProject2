package com.example.wilsonsu.nytimessearch.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wilsonsu on 2/10/16.
 */

public class Article implements Parcelable {

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



    public static class HeadlineEntity implements Parcelable {
        public String main;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.main);
        }

        public HeadlineEntity() {
        }

        protected HeadlineEntity(Parcel in) {
            this.main = in.readString();
        }

        public static final Creator<HeadlineEntity> CREATOR = new Creator<HeadlineEntity>() {
            public HeadlineEntity createFromParcel(Parcel source) {
                return new HeadlineEntity(source);
            }

            public HeadlineEntity[] newArray(int size) {
                return new HeadlineEntity[size];
            }
        };
    }

    public static class MultimediaEntity implements Parcelable {
        public int width;
        public String url;
        public int height;
        public String type;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.width);
            dest.writeString(this.url);
            dest.writeInt(this.height);
            dest.writeString(this.type);
        }

        public MultimediaEntity() {
        }

        protected MultimediaEntity(Parcel in) {
            this.width = in.readInt();
            this.url = in.readString();
            this.height = in.readInt();
            this.type = in.readString();
        }

        public static final Creator<MultimediaEntity> CREATOR = new Creator<MultimediaEntity>() {
            public MultimediaEntity createFromParcel(Parcel source) {
                return new MultimediaEntity(source);
            }

            public MultimediaEntity[] newArray(int size) {
                return new MultimediaEntity[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeParcelable(this.headline, 0);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeTypedList(multimedia);
    }

    public Article() {
    }

    protected Article(Parcel in) {
        this.url = in.readString();
        this.headline = in.readParcelable(HeadlineEntity.class.getClassLoader());
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.multimedia = in.createTypedArrayList(MultimediaEntity.CREATOR);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}


