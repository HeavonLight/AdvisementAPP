package com.example.heavon.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.heavon.constant.Constant;
import com.example.heavon.myapplication.R;
import com.example.heavon.myapplication.SummaryDialog;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by heavon on 2017/3/28.
 */

public class ShowWebView extends WebView{

    private Activity activity;
    private DatePickerDialog mDateDlg;
    private Handler handler;
    private ProgressBar mProgressBar;

    private ValueCallback<Uri[]> mValueCallback;
    private ValueCallback<Uri> mUploadMessage;

    private ViewGroup mRootLayout;

    public ShowWebView(Context context) {
        super(context);
        if(context instanceof Activity){
            this.activity = (Activity)(context);
            initialize();
        }
    }

    public ShowWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(context instanceof Activity){
            this.activity = (Activity)(context);
            initialize();
        }
    }

    public ShowWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(context instanceof Activity){
            this.activity = (Activity)(context);
            initialize();
        }
    }

    public ShowWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        if(context instanceof Activity){
            this.activity = (Activity)(context);
            initialize();
        }
    }

    public void initialize(){
        //获取控件
        mRootLayout = ((ViewGroup)activity.getWindow().getDecorView().findViewById(android.R.id.content));
        //设置进度条
        mProgressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5, 0, 0));
        Drawable drawable = activity.getResources().getDrawable(R.drawable.progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);

        WebSettings settings = super.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        handler = new Handler(Looper.getMainLooper());
        //设置js交互
        Object jsObj = new Object() {
            private Date newDate;
            @JavascriptInterface
            public void toast(String msg) {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
            @JavascriptInterface
            public void save() {
                //保存成功
                Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();
                //跳转返回
                activity.finish();
            }
            @JavascriptInterface
            public void editSummary(@Nullable String summary) {
                //进入长文本输入
                if (summary == null) {
                    summary = "";
                }
                SummaryDialog dlg = new SummaryDialog(activity, summary, new SummaryDialog.SummaryEditListener(){
                    @Override
                    public void confirmSummary(final String summary) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ShowWebView.super.loadUrl("javascript:setInputText('summary','"+summary+"')");
                                Toast.makeText(activity, summary, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dlg.showDialog();
            }
            @JavascriptInterface
            public Date editDate(@Nullable String dStr) {
                Date date;
                Calendar calendar = Calendar.getInstance();
                if (dStr == null) {
                    date = new Date();
                } else {
                    date = new Date(dStr);
                }
                calendar.setTime(date);
                //弹出日期选择框
                mDateDlg = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        Log.e("editShowDate", year + "/" + monthOfYear + "/" + dayOfMonth);
                        //设置日期
                        newDate = new Date(year, monthOfYear, dayOfMonth);
//                        mShowBoxWeb.loadUrl("javascript:setInputText('date'");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ShowWebView.super.loadUrl("javascript:setInputText('date','"+newDate+"')");
                                Toast.makeText(activity, newDate.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        mDateDlg.dismiss();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                mDateDlg.show();
                return newDate;
            }
            @JavascriptInterface
            public void editName() {
                //弹出名字名字框
            }
        };
        addJavascriptInterface(jsObj, "editObj");
        setWebChromeClient(new ShowWebChromeClient());
        setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                shouldOverrideUrlLoading(view, request);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    public class ShowWebChromeClient extends WebChromeClient{
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            result.confirm();
            return true;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            mRootLayout.addView(mCustomView);
            mCustomViewCallback = callback;
            ShowWebView.this.setVisibility(View.GONE);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        @Override
        public void onHideCustomView() {
            ShowWebView.this.setVisibility(View.VISIBLE);
            if (mCustomView == null) {
                return;
            }
            mCustomView.setVisibility(View.GONE);
            mRootLayout.removeView(mCustomView);
            mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onHideCustomView();
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            if(mUploadMessage != null){
                return;
            }
            mUploadMessage = uploadMsg;
            String type = TextUtils.isEmpty(acceptType) ? "image/*" : acceptType;
            selectFile(type);
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // For Android  > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooser(uploadMsg, acceptType);
        }

        // For Android 5.0+
        @Override
        @SuppressLint("NewApi")
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback,
                                         android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
            mValueCallback = valueCallback;
            String type = "image/*";
            if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
                    && fileChooserParams.getAcceptTypes().length > 0 && !TextUtils.isEmpty(fileChooserParams.getAcceptTypes()[0])) {
                type = fileChooserParams.getAcceptTypes()[0];
            }
            selectFile(type);
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (mProgressBar.getVisibility() == View.GONE) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        private void selectFile(String type){
            Log.e("webview", "选择文件"+type );
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType(type);

            activity.startActivityForResult(Intent.createChooser(i, "File Chooser"), Constant.FILECHOOSER_RESULTCODE);
        }
    }

    private void fullscreen(){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 上传图片,调用系统图库 与h5 file标签交互
     *
     * @param resultCode
     * @param intent
     */
    public void uploadImgFromSysPhotos(int requestCode, int resultCode, Intent intent) {
        Log.e("webview", "上传图片"+resultCode);
        if (mUploadMessage != null) {//5.0以下
            Uri result = intent == null || resultCode != activity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (mValueCallback != null) {//5.0+
            Uri[] uris = new Uri[1];
            uris[0] = intent == null || resultCode != activity.RESULT_OK ? null : intent.getData();
            if (uris[0]!=null){
                mValueCallback.onReceiveValue(uris);
            } else {
                mValueCallback.onReceiveValue(new Uri[]{});
            }
            mValueCallback = null;
        }
    }

    public void finish(){
        this.clearHistory();
        this.clearCache(true);
        this.freeMemory();
        this.pauseTimers();
        this.destroy();
    }
}
