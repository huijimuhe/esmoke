package com.huijimuhe.esmoke.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Administrator on 2016/2/26.
 */
public class PreferenceUtils {
    private SharedPreferences sp;
    private Context context;

    private static final String STRING_TOKEN="token";
    private static final String STRING_USER="user";
    private static final String STRING_FIRST_INSTALL="first_install";
    public static PreferenceUtils instance;

    private PreferenceUtils(){}
    private PreferenceUtils(Context context){
        this.context=context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtils getInstance(Context context){
        if(instance==null){
            instance=new PreferenceUtils(context);
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
            edit.putInt("STRING_FIRST_INSTALL",1);
            edit.commit();
    }
}
