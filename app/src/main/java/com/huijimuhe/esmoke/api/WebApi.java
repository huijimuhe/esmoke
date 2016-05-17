package com.huijimuhe.esmoke.api;

import com.huijimuhe.esmoke.core.AppContext;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class WebApi {

    public static void getArticlesList(int page, TextHttpResponseHandler responseHandler) {
        String url = BaseClient.URL_GET_ARTICLES+"/"+String.valueOf(page);
        RequestParams params = new RequestParams();
        BaseClient.get(url, params, responseHandler);
    }

    public static void open(String name, String openid, String gender, String avatar, String token, String type, TextHttpResponseHandler responseHandler) {
        String url = BaseClient.URL_POST_OPEN;
        RequestParams params = new RequestParams();
        params.put("open_id", openid);
        params.put("token", token);
        params.put("type", type);
        params.put("name", name);
        params.put("gender", gender);
        params.put("avatar", avatar);
        BaseClient.post(url, params, responseHandler);
    }

    public static void getOssToken(TextHttpResponseHandler responseHandler) {
        String token = AppContext.getInstance().getToken();
        String url = BaseClient.URL_GET_OSS;
        RequestParams params = new RequestParams();
        params.put("token", token);
        BaseClient.get(url, params, responseHandler);
    }


    public static void signOut(TextHttpResponseHandler responseHandler) {
        String url = BaseClient.URL_POST_SIGN_OUT;
        String token = AppContext.getInstance().getToken();
        RequestParams params = new RequestParams();
        params.put("token", token);
        BaseClient.post(url, params, responseHandler);
    }

}
