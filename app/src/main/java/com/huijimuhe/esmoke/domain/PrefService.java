package com.huijimuhe.esmoke.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.huijimuhe.esmoke.bean.UserBean;

/**
 * Created by Administrator on 2016/2/26.
 */
public class PrefService {
    private SharedPreferences sp;
    private Context context;

    private static final String STRING_TOKEN="token";
    private static final String STRING_USER="user";
    private static final String STRING_FIRST_INSTALL="first_install";
    private static final String INT_CIGA_COUNT="ciga_count";
    public static PrefService instance;

    private PrefService(){}

    private PrefService(Context context){
        this.context=context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PrefService getInstance(Context context){
        if(instance==null){
            instance=new PrefService(context);
        }
        return instance;
    }


    /**
     * 设置token
     * @param token
     */
    public void setToken(String token){
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(STRING_TOKEN, token);
        edit.commit();
    }

    /**
     * 获取token
     * @return
     */
    public String getToken() {
        return sp.getString(STRING_TOKEN, null);
    }

    /**
     * 保存用户
     * @param user
     */
    public void setUser(UserBean user) {
        SharedPreferences.Editor edit = sp.edit();
        String request=user.toString();
        edit.putString(STRING_USER,user.toString());
        edit.commit();
    }

    /**
     * 获取用户
     * @return
     */
    public UserBean getUser(){
      String json = sp.getString(STRING_USER, null);
      return   new Gson().fromJson(json, UserBean.class);
    }

    /**
     * 用户退出
     */
    public void cleanUser() {
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(STRING_USER);
        edit.remove(STRING_TOKEN);
        edit.commit();
    }

    /**
     * 是否是第一次安装登录
     * @return
     */
    public boolean isInstalled() {
        return sp.getInt("STRING_FIRST_INSTALL", 0)==0;
    }

    public void setInstalled() {
            SharedPreferences.Editor edit = sp.edit();
            edit.putInt("STRING_FIRST_INSTALL", 1);
            edit.commit();
    }

    /**
     * 未读计数
     * @param
     */
    public void increatCiga(){
        int ciga=sp.getInt(INT_CIGA_COUNT, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(INT_CIGA_COUNT, ciga+1);
        edit.commit();
    }

    public void setCiga(){
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(INT_CIGA_COUNT, 17);
        edit.commit();
    }

    /**
     * 获取未读计数
     * @return
     */
    public int getCiga() {
        return sp.getInt(INT_CIGA_COUNT, 0);
    }
}
