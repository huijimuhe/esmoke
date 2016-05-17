package com.huijimuhe.esmoke.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.api.WebApi;
import com.huijimuhe.esmoke.bean.UserBean;
import com.huijimuhe.esmoke.core.AppContext;
import com.huijimuhe.esmoke.domain.PrefService;
import com.huijimuhe.esmoke.ui.auth.SignInActivity;
import com.huijimuhe.esmoke.utils.ToastUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class RankActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    public static Intent newIntent() {
        Intent intent = new Intent(AppContext.getInstance(),
                RankActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rank, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }else if(id==R.id.action_signout){
            WebApi.signOut(new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    finish();
                    startActivity(SignInActivity.newIntent());
                    ToastUtils.show(RankActivity.this,"您已经退出登录");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    finish();
                    startActivity(SignInActivity.newIntent());
                    ToastUtils.show(RankActivity.this,"您已经退出登录");
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    private void init(){
        //toolbar
        //Set up the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("我的");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //烟数量
        int total=  PrefService.getInstance(this).getCiga();
        TextView tvTotal=(TextView)findViewById(R.id.tv_total);
        tvTotal.setText(String.valueOf(total));

        int pack= total%20;
        TextView tvPack=(TextView)findViewById(R.id.tv_pack);
        tvPack.setText(String.valueOf(pack));

        //个人资料
        UserBean owner=AppContext.getInstance().getUser();
        ImageView ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        AppContext.getInstance().loadImg(ivAvatar,owner.getAvatar());
        TextView tvName = (TextView)findViewById(R.id.tv_name);
        tvName.setText(owner.getName());

    }
}
