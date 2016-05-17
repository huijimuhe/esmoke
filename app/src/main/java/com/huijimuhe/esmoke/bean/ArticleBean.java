package com.huijimuhe.esmoke.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Huijimuhe on 2016/3/30.
 * This is a part of Esmoke
 * enjoy
 */
public class ArticleBean implements Parcelable {
    /**
     * id : 2
     * url : 4444.com/adfdas/dwer234234
     * title : 123
     * thumb : http://7xsfio.com1.z0.glb.clouddn.com/
     * short : <p>adsfadsfczxvwerwer</p>
     * created_at : 2016-03-30 12:06:47
     */

    private int id;
    private String url;
    private String title;
    private String thumb;
    @SerializedName("short")
    private String shortX;
    private String created_at;

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setShortX(String shortX) {
        this.shortX = shortX;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getThumb() {
        return thumb;
    }

    public String getShortX() {
        return shortX;
    }

    public String getCreated_at() {
        return created_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.thumb);
        dest.writeString(this.shortX);
        dest.writeString(this.created_at);
    }

    public ArticleBean() {
    }

    private ArticleBean(Parcel in) {
        this.id = in.readInt();
        this.url = in.readString();
        this.title = in.readString();
        this.thumb = in.readString();
        this.shortX = in.readString();
        this.created_at = in.readString();
    }

    public static final Parcelable.Creator<ArticleBean> CREATOR = new Parcelable.Creator<ArticleBean>() {
        public ArticleBean createFromParcel(Parcel source) {
            return new ArticleBean(source);
        }

        public ArticleBean[] newArray(int size) {
            return new ArticleBean[size];
        }
    };
}
