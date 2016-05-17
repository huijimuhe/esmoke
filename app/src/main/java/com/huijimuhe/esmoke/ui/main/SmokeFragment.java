package com.huijimuhe.esmoke.ui.main;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.domain.PrefService;
import com.huijimuhe.esmoke.widget.CigaretteView;
import com.huijimuhe.esmoke.widget.SmokeRingView;

/**
 * Created by Huijimuhe on 2016/3/30.
 * This is a part of Esmoke
 * enjoy
 */
public class SmokeFragment extends AbSmokeBaseFragment implements CigaretteView.onSuckListener {

    public static SmokeFragment newInstance() {
        SmokeFragment fragment = new SmokeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initUI(View v) {

        //cigartte
        mCigaretteView = (CigaretteView) v.findViewById(R.id.smoke);
        mCigaretteView.setOnSuckListener(this);

        //ring
        mRingView = (SmokeRingView) v.findViewById(R.id.smoke_ring);

        //background
        mBackground = v.findViewById(R.id.layout_background);
        mBackground.setOnTouchListener(suckListener);

        //rotate
        v.findViewById(R.id.btn_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showAd();
                mCigaretteView.reset();
                PrefService.getInstance(getActivity()).increatCiga();
                mIsFinished = false;
            }
        });

        //drawer
        v.findViewById(R.id.btn_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ( (MainActivity) getActivity()).toggleDrawer();
            }
        });
//
        //sound
        v.findViewById(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show(view);
//                if (mIsSoundOpen) {
//                    mIsSoundOpen = false;
//                    ImageButton iv=(ImageButton)view;
//                    iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_off));
//                } else {
//                    mIsSoundOpen = true;
//                    ImageButton iv=(ImageButton)view;
//                    iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_on));
//                }
            }
        });
    }

    /**
     * 抽完烟的时候
     */
    @Override
    public void onFinish() {
        //结束动画
        suckOut();
        mIsFinished = true;
    }

    private View.OnTouchListener suckListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (!mIsFinished) {
                    suckIn();
                    mCigaretteView.start();
                }
                hideKeyboard();
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (!mIsFinished) {
                    suckOut();
                }
            }
            return true;
        }
    };
}
