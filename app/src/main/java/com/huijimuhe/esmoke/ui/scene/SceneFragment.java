package com.huijimuhe.esmoke.ui.scene;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Huijimuhe on 2016/3/22.
 * This is a part of Esmoke
 * enjoy
 */
public class SceneFragment extends Fragment {

    final static String LAYOUT_ID = "layoutid";

    public static SceneFragment newInstance(int layoutId) {
        SceneFragment pane = new SceneFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID, layoutId);
        pane.setArguments(args);
        return pane;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(getArguments().getInt(LAYOUT_ID, -1), container, false);
        return rootView;
    }
}
