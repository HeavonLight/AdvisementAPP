package com.example.heavon.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.example.heavon.constant.Constant;
import com.example.heavon.dao.ShowDao;
import com.example.heavon.views.ShowWebView;

import java.util.Calendar;

import static android.view.KeyEvent.KEYCODE_BACK;

public class AddShowActivity extends BasicActivity {

    private ShowDao showDao = new ShowDao();
    // UI preference.
    private ShowWebView mShowBoxWeb;

    private Calendar calendar;
    private DatePickerDialog mDateDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_show);

        initUI();
    }

    /**
     * 初始化UI
     */
    public void initUI() {
        mShowBoxWeb = (ShowWebView) findViewById(R.id.show_box_web);
        //加载URL
        ShowDao showDao = new ShowDao();
        mShowBoxWeb.loadUrl(showDao.getAddShowUrl());

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {Log.e("activity", (keyCode == KEYCODE_BACK && mShowBoxWeb.canGoBack())+"");
        if (keyCode == KEYCODE_BACK && mShowBoxWeb.canGoBack()) {
            mShowBoxWeb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_show, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add: {
                /**----------wait to modify-----------**/
//                Toast.makeText(this, "edit put", Toast.LENGTH_SHORT).show();
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
            //调用系统图库
            mShowBoxWeb.uploadImgFromSysPhotos(requestCode, resultCode, intent);
        }
    }
}
