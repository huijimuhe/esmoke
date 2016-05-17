package com.huijimuhe.esmoke.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Huijimuhe on 2016/3/30.
 * This is a part of Esmoke
 * enjoy
 */
public class ArticleResponseBean implements Parcelable {

    /**
     * items : [{"id":2,"url":"4444.com/adfdas/dwer234234","title":"123","thumb":"http://7xsfio.com1.z0.glb.clouddn.com/","short":"<p>adsfadsfczxvwerwer<\/p>","created_at":"2016-03-30 12:06:47"}]
     * total_number : 1
     */

    private int total_number;
    private List<ArticleBean> items;

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public void setItems(List<ArticleBean> items) {
        this.items = items;
    }

    public int getTotal_number() {
        return total_number;
    }

    public List<ArticleBean> getItems() {
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total_number);
        dest.writeTypedList(items);
    }

    public ArticleResponseBean() {
    }

    private ArticleResponseBean(Parcel in) {
        this.total_number = in.readInt();
        in.readTypedList(items, ArticleBean.CREATOR);
    }

    public static final Parcelable.Creator<ArticleResponseBean> CREATOR = new Parcelable.Creator<ArticleResponseBean>() {
        public ArticleResponseBean createFromParcel(Parcel source) {
            return new ArticleResponseBean(source);
        }

        public ArticleResponseBean[] newArray(int size) {
            return new ArticleResponseBean[size];
        }
    };
}
