package com.huijimuhe.esmoke.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.core.AppContext;

public class AboutActivity extends AppCompatActivity {

    public static Intent newIntent() {
        Intent intent = new Intent(AppContext.getInstance(),
                AboutActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
