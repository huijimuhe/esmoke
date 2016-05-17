package com.huijimuhe.esmoke.ui.Article;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.adapter.ArticlesAdapter;
import com.huijimuhe.esmoke.adapter.base.AbBaseAdapter;
import com.huijimuhe.esmoke.api.WebApi;
import com.huijimuhe.esmoke.bean.ArticleBean;
import com.huijimuhe.esmoke.bean.ArticleResponseBean;
import com.huijimuhe.esmoke.core.AppContext;
import com.huijimuhe.esmoke.ui.base.AbBaseActivity;
import com.huijimuhe.esmoke.ui.base.AbBaseListActivity;
import com.huijimuhe.esmoke.ui.main.WebActivity;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ArticleListActivity extends AbBaseListActivity {

    private ArrayList<ArticleBean> mDataset=new ArrayList<>();

    public static Intent newIntent() {
        Intent intent = new Intent(AppContext.getInstance(),
                ArticleListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set adapter
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AbBaseAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                onItemNormalClick(view, postion);
            }
        });
        //get data
        setSwipeRefreshLoading();
        loadNewData();
    }

    @Override
    public ArticlesAdapter getAdapter() {
        ArticlesAdapter adapter = new ArticlesAdapter(mDataset, this);
        return adapter;
    }

    @Override
    public void onItemNormalClick(View view, int postion) {
        startActivity(WebActivity.newIntent(mDataset.get(postion).getUrl(), mDataset.get(postion).getTitle()));
    }

    @Override
    public void loadNewData() {
        mCurrentPage = 0;
        WebApi.getArticlesList(mCurrentPage, new TextHttpResponseHandlerEx() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                ArticleResponseBean response = gson.fromJson(responseString, ArticleResponseBean.class);
                //refresh datasource
                if (response != null && response.getItems() != null) {
                    if (response.getItems().size() != 0) {
                        mDataset.clear();
                        mDataset.addAll(response.getItems());
                        mAdapter.notifyDataSetChanged();
                        mCurrentPage++;
                    }
                }
            }
        });
    }

    @Override
    public void loadOldData() {
        WebApi.getArticlesList(mCurrentPage, new TextHttpResponseHandlerEx() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                ArticleResponseBean response = gson.fromJson(responseString, ArticleResponseBean.class);
                //refresh datasource
                if (response != null && response.getItems() != null) {
                    if (response.getItems().size() != 0) {
                        mDataset.addAll(mDataset.size(), response.getItems());
                        mAdapter.notifyDataSetChanged();
                        mCurrentPage++;
                    }
                }
            }
        });
    }
}
