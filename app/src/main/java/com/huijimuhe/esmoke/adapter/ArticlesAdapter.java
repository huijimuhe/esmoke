package com.huijimuhe.esmoke.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.adapter.base.AbBaseAdapter;
import com.huijimuhe.esmoke.bean.ArticleBean;
import com.huijimuhe.esmoke.bean.RoomBean;
import com.huijimuhe.esmoke.core.AppContext;

import java.util.ArrayList;

/**
 * Created by Huijimuhe on 2016/3/18.
 * This is a copy of Esmoke
 * belongs to com.huijimuhe.esmoke.adapter
 * please enjoy the day and night when you work hard on this.
 */
public class ArticlesAdapter extends AbBaseAdapter<ArticlesAdapter.ViewHolder> {
    private ArrayList<ArticleBean> mDataset;
    private Activity mActivity;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleView;
        public ImageView mThumbView;

        public ViewHolder(View v) {
            super(v);
            mTitleView = (TextView) v.findViewById(R.id.tv_title);
            mThumbView = (ImageView) v.findViewById(R.id.iv_thumb);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, getLayoutPosition());
                }
            });
        }
    }

    public ArticlesAdapter(ArrayList<ArticleBean> beans, Activity activity) {
        mDataset = beans;
        mActivity=activity;
    }

    @Override
    public ArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_article, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArticleBean model=mDataset.get(position);
        holder.mTitleView.setText(model.getTitle());
        AppContext.getInstance().loadImg(holder.mThumbView,model.getThumb());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
