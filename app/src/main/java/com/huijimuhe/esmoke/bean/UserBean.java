package com.huijimuhe.esmoke.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2015/9/17.
 */
public class UserBean implements Parcelable {

    private String id;
    private String name;
    private String gender;
    private String avatar;
    private String open_id;
    private String app_token;

    public UserBean() {

    }

    @Override
    public String toString() {
        Gson gson=new Gson();
        String temp=   gson.toJson(this);
        return  gson.toJson(this);//super.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getApp_token() {
        return app_token;
    }

    public void setApp_token(String app_token) {
        this.app_token = app_token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.gender);
        dest.writeString(this.avatar);
        dest.writeString(this.open_id);
        dest.writeString(this.app_token);
    }

    private UserBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.gender = in.readString();
        this.avatar = in.readString();
        this.open_id = in.readString();
        this.app_token = in.readString();
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
