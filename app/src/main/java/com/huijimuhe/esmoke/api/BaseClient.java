package com.huijimuhe.esmoke.api;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.core.AppContext;
import com.huijimuhe.esmoke.utils.ToastUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BaseClient {

    public static final String URL_DNS = "http://esmoke.huijimuhe.com/";
    public static final String URL_BASE = URL_DNS + "api/";

    public static final String URL_GET_OSS = "token";
    public static final String URL_GET_ARTICLES = "articles";
    public static final String URL_POST_OPEN = "open";
    public static final String URL_POST_SIGN_OUT = "signout";
    public static final String URL_USER_AGREEMENT = URL_BASE + "agreement";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (!AppContext.getInstance().isConnected()) {
            ToastUtils.show(AppContext.getInstance(), AppContext.getInstance().getString(R.string.error_no_network));
            return;
        }
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (!AppContext.getInstance().isConnected()) {
            ToastUtils.show(AppContext.getInstance(), AppContext.getInstance().getString(R.string.error_no_network));
            return;
        }
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return URL_BASE + relativeUrl;
    }
}
