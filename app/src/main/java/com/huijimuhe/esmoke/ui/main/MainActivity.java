package com.huijimuhe.esmoke.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.bean.UserBean;
import com.huijimuhe.esmoke.core.AppContext;
import com.huijimuhe.esmoke.ui.Article.ArticleListActivity;
import com.huijimuhe.esmoke.ui.base.AbBaseActivity;
import com.huijimuhe.esmoke.utils.MarketUtils;

public class MainActivity extends AbBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Intent newIntent() {
        Intent intent = new Intent(AppContext.getInstance(),
                MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // setupBannerAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUI();
    }

    public void toggleDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private void initUI() {
        //fragment container
        if (getSupportFragmentManager().findFragmentByTag(
                SmokeFragment.class.getName()) == null) {
            SmokeFragment fragment = SmokeFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment,
                            SmokeFragment.class.getName())
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }

        //抽屉菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //菜单头
        UserBean owner = AppContext.getInstance().getUser();

        ImageView ivAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_avatar);
        AppContext.getInstance().loadImg(ivAvatar, owner.getAvatar());

        TextView tvName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        tvName.setText(owner.getName());

        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "head clicked");
                startActivity(RankActivity.newIntent());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_why:
                startActivity(ArticleListActivity.newIntent());
                break;
            case R.id.nav_about:
                //startActivity(AboutActivity.newIntent());
                break;
            case R.id.nav_share:
                MarketUtils.launchAppDetail("com.huijimuhe.esmoke", "");
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected long mExitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按退出",
                        Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
