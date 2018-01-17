package com.example.heavon.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.smssdk.SMSSDK;

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSSDK.initSDK(this, "19accee5f0490", "96545bc0dfb6f782602717bb863a9464");
        initWindowStatusBar();
    }

    //初始化顶部工具栏
    public void initToolBar(@Nullable String title){
        System.out.println("startInitToolbar");
        /**开始初始化**/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        if(toolbar == null){
            return;
        }
        setSupportActionBar(toolbar);
        //设置标题栏
        setTitle("");
        if(tvTitle == null){
            if(title != null){
                setTitle(title);
            }
        }else{
            if(title != null){
                tvTitle.setText(title);
            }
        }
        toolbar.setNavigationIcon(R.mipmap.back_blue);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicActivity.this.finish();
            }
        });
        /**结束初始化**/
        System.out.println("endInitToolbar");
    }

    //初始化沉浸式系统状态栏
    public void initWindowStatusBar() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //修改当前 Activity 的显示模式，hideStatusBarBackground :true 全屏模式，false 着色模式
    protected void setStatusBar(boolean hideStatusBarBackground) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (hideStatusBarBackground) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }

            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                if (hideStatusBarBackground) {
                    mChildView.setPadding(
                            mChildView.getPaddingLeft(),
                            0,
                            mChildView.getPaddingRight(),
                            mChildView.getPaddingBottom()
                    );
                } else {
                    int statusHeight = getStatusBarHeight(this);
                    mChildView.setPadding(
                            mChildView.getPaddingLeft(),
                            statusHeight,
                            mChildView.getPaddingRight(),
                            mChildView.getPaddingBottom()
                    );
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (hideStatusBarBackground) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(Color.TRANSPARENT);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.white));
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    //获取系统状态栏高度
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    //跳转到主页面
    public void gotoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    //跳转到登录页面
    public void gotoLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    //进入到搜索页面
    public void enterSearch(){
        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivity(intent);
    }

    //进入到添加节目页
    public void enterAddShow(){
        Intent intent = new Intent(this, AddShowActivity.class);
        this.startActivity(intent);
    }

    //进入到管理节目页
    public void enterManageShow(){
        Intent intent = new Intent(this, ManageShowActivity.class);
        this.startActivity(intent);
    }

    //进入到反馈页
    public void enterFeedback(){
        Intent intent = new Intent(this, FeedbackActivity.class);
        this.startActivity(intent);
    }

    //进入到关于页
    public void enterAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        this.startActivity(intent);
    }

    //进入到协议页
    public void enterProtocol() {
        Intent intent = new Intent(this, ProtocolActivity.class);
        this.startActivity(intent);
    }
}
