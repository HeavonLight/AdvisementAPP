package com.example.heavon.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heavon.constant.Constant;
import com.example.heavon.dao.ShowDao;
import com.example.heavon.dao.UserDao;
import com.example.heavon.views.ShowWebView;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.view.KeyEvent.KEYCODE_BACK;

public class EditShowActivity extends BasicActivity {

    private int mSid = 0;
    private String url = "";

    private ShowDao showDao = new ShowDao();
    // UI preference.
    private ShowWebView mShowBoxWeb;

    private Calendar calendar;
    private DatePickerDialog mDateDlg;

    /**
     * 初始化，用于接收参数
     */
    public void initialize() {
        Bundle bundle = this.getIntent().getExtras();
        int sid = bundle.getInt("sid");
        if (sid > 0) {
            mSid = sid;
            url = showDao.getEditShowUrlById(mSid);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_show);

        //初始化
        initialize();
        initUI();
    }

    /**
     * 初始化UI
     */
    public void initUI() {
        mShowBoxWeb = (ShowWebView) findViewById(R.id.show_box_web);
        //加载URL
        if (!url.isEmpty()) {
            Log.e("showEditActivity", url);
            mShowBoxWeb.loadUrl(url);
        }

        initToolBar(null);
    }

    /**
     * 拦截返回按钮事件，如果webview有页面可返回则返回webview上一页面
     *
     * @param keyCode 按钮
     * @param event   事件
     * @return 是否响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK && mShowBoxWeb.canGoBack()) {
            mShowBoxWeb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_show, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit: {
                /**----------wait to modify-----------**/
//                Toast.makeText(this, "edit save", Toast.LENGTH_SHORT).show();
                mShowBoxWeb.loadUrl("javascript:editPost()");
                /**----------wait to modify-----------**/
            }
            break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //上传图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Constant.FILECHOOSER_RESULTCODE) {
            if (requestCode == Constant.FILECHOOSER_RESULTCODE) {
                //调用系统图库
                mShowBoxWeb.uploadImgFromSysPhotos(requestCode, resultCode, intent);
            }
        }
    }
}
