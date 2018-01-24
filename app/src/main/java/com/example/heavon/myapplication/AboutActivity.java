package com.example.heavon.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //初始化顶部导航栏
        initToolBar(getString(R.string.about_title));
    }
}
