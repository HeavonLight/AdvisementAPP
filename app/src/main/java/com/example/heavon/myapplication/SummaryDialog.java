package com.example.heavon.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Created by heavon on 2017/3/12.
 */

public class SummaryDialog {

    private PopupWindow mPopWindow;
    private EditText mSummaryView;

    private Context context;
    private SummaryEditListener listener;
    private View mPopupWindowView;
    private Screen mScreen;

    private String summary = "";

    public SummaryDialog(Context context) {
        this.context = context;
    }

    public SummaryDialog(Activity context, String summary, SummaryEditListener listener) {
        this.context = context;
        this.listener = listener;

        onCreate();
        if (summary != null && !TextUtils.isEmpty(summary)) {
            mSummaryView.setText(summary);
            this.summary = summary;
        }
    }

    /**
     * Called when the activity is first created.
     */

    public void onCreate() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupWindowView = inflater.inflate(R.layout.edit_summary_dlg, null, false);
        if (context instanceof Activity) {
            mScreen = new Screen((Activity) context);
            mPopWindow = new PopupWindow(mPopupWindowView, mScreen.getWidth(), mScreen.getHeight() - BasicActivity.getStatusBarHeight((Activity) context), true);
        } else {
            mPopWindow = new PopupWindow(mPopupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        }

        mSummaryView = (EditText) mPopupWindowView.findViewById(R.id.summary);
        mPopupWindowView.findViewById(R.id.link_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                mPopWindow.dismiss();
            }
        });
        mPopupWindowView.findViewById(R.id.link_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存
                summary = mSummaryView.getText().toString().trim();
                listener.confirmSummary(summary);
                mPopWindow.dismiss();
            }
        });
    }

    public String getSummary() {
        return summary;
    }

    public void closeDialog(boolean result) {
        mPopWindow.dismiss();
    }

    public void showDialog() {
        if (context instanceof Activity) {
            mPopWindow.showAtLocation(mPopupWindowView, Gravity.NO_GRAVITY, 0, BasicActivity.getStatusBarHeight((Activity) context));
            mPopWindow.showAsDropDown(mPopupWindowView);
        }
    }

    public class Screen {

        private int width;
        private int height;

        public Screen(Activity context) {
            DisplayMetrics metrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }

        /**
         * @return 屏幕宽度 in pixel
         */
        public int getWidth() {
            return width;
        }

        /**
         * @return 屏幕高度 in pixel
         */
        public int getHeight() {
            return height;
        }

        @Override
        public String toString() {
            return "Screen{" +
                    "width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

    public interface SummaryEditListener {
        public void confirmSummary(String summary);
    }
}
