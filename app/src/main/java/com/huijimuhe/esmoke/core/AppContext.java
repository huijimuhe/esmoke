package com.huijimuhe.esmoke.core;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.bean.UserBean;
import com.huijimuhe.esmoke.domain.PrefService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.tencent.bugly.crashreport.CrashReport;

public class AppContext extends Application {
    public static final String RING_BROADCAST = "com.huijimuhe.esmoke.ring";
    public static final String DANMAKU_BROADCAST = "com.huijimuhe.esmoke.danmaku";
    IntentFilter ringFilter = new IntentFilter(RING_BROADCAST);
    IntentFilter danmakuFilter = new IntentFilter(DANMAKU_BROADCAST);

    // singleton
    private static AppContext AppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = this;
        CrashReport.initCrashReport(getApplicationContext(), "[YOURS]", false);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public static AppContext getInstance() {
        return AppContext;
    }

    public void loadImg(ImageView v, String url) {
        String absoluteUrl = url;
        Picasso.with(this)
                .load(absoluteUrl)
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .transform(new CropSquareTransformation())
                .into(v);
    }

    public class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "monolog";
        }
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)
                this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public String getToken() {
        return PrefService.getInstance(this).getToken();
    }

    public UserBean getUser() {
        return PrefService.getInstance(this).getUser();
    }

    public void registChatRoomReceiver(BroadcastReceiver receiver) {
        registerReceiver(receiver, ringFilter);
        registerReceiver(receiver, danmakuFilter);
    }

    public void unregistChatRoomReceiver(BroadcastReceiver receiver) {
        unregisterReceiver(receiver);
    }
}
