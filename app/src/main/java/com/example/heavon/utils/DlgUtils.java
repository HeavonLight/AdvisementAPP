package com.example.heavon.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.heavon.myapplication.R;

/**
 * Created by heavon on 2017/1/16.
 */

public class DlgUtils {

    private Activity mActivity;
    private Dialog mDlg;

    private int mDlgStyle;
    private int mDlgLayout;

    //
    public DlgUtils(Activity activity) {
        this.mActivity = activity;
    }

    //设置窗口风格
    public void setStyle(int style) {
        this.mDlgStyle = style;
    }

    //设置窗口布局
    public void setLayout(int layout) {
        this.mDlgLayout = layout;
    }

    //初始化正在登录等待窗口
    public void initDlg(int style, int layout) {
        if (mDlg != null) {
            mDlg = null;
        }
        mDlg = new Dialog(mActivity, style);
        mDlg.setContentView(layout);
        mDlgStyle = style;
        mDlgLayout =layout;
        Window window = mDlg.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 获取和mLoginingDlg关联的当前窗口的属性，从而设置它在屏幕中显示的位置
        if(mDlgLayout == R.layout.loading_dlg){

        } else {
            // 获取屏幕的高宽
            DisplayMetrics dm = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int cxScreen = dm.widthPixels;
            int cyScreen = dm.heightPixels;
            int height = (int) mActivity.getResources().getDimension(R.dimen.loginingdlg_height);// 高42dp
            int lrMargin = (int) mActivity.getResources().getDimension(R.dimen.loginingdlg_lr_margin); // 左右边沿10dp
            int topMargin = (int) mActivity.getResources().getDimension(R.dimen.loginingdlg_top_margin); // 上沿20dp
            params.y = (-(cyScreen - height) / 2) + topMargin; // -199
        /* 对话框默认位置在屏幕中心,所以x,y表示此控件到"屏幕中心"的偏移量 */
            params.width = cxScreen;
            params.height = height;// width,height表示mLoginingDlg的实际大小
        }

        mDlg.setCanceledOnTouchOutside(true); // 设置点击Dialog外部任意区域关闭Dialog
    }

    //显示正在登录窗口
    public void showDlg() {
        if (mDlg != null) {
            if (mDlgLayout == R.layout.loading_dlg) {
                Animation animation1 = AnimationUtils.loadAnimation(mActivity, R.anim.amin_loading1);
                Animation animation2 = AnimationUtils.loadAnimation(mActivity, R.anim.amin_loading2);
                Animation animation3 = AnimationUtils.loadAnimation(mActivity, R.anim.amin_loading3);
                Animation animation4 = AnimationUtils.loadAnimation(mActivity, R.anim.amin_loading4);
                Animation animation_box = AnimationUtils.loadAnimation(mActivity, R.anim.amin_loading_box);
                mDlg.findViewById(R.id.circle1).startAnimation(animation1);
                mDlg.findViewById(R.id.circle2).startAnimation(animation2);
                mDlg.findViewById(R.id.circle3).startAnimation(animation3);
                mDlg.findViewById(R.id.circle4).startAnimation(animation4);
                mDlg.findViewById(R.id.circle_box).startAnimation(animation_box);
                Log.e("anim", "staart");
            }
            mDlg.show();
        }
    }

    //关闭正在登录窗口
    public void closeDlg() {
        if (mDlg != null && mDlg.isShowing()) {
            mDlg.dismiss();
        }
    }
}
