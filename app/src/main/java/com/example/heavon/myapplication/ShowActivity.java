package com.example.heavon.myapplication;

import android.content.Intent;
import android.content.res.ObbInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.heavon.constant.Constant;
import com.example.heavon.dao.ShowDao;
import com.example.heavon.dao.UserDao;
import com.example.heavon.utils.DlgUtils;
import com.example.heavon.utils.HttpUtils;
import com.example.heavon.views.ShowWebView;
import com.example.heavon.vo.Show;

import static android.view.KeyEvent.KEYCODE_BACK;

public class ShowActivity extends BasicActivity {

    private int mSid = 0;
    private String url= "";

    private ShowDao showDao = new ShowDao();
    // UI preference.
    private ShowWebView mShowBoxWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        //初始化
        initialize();
        initUI();
    }

    /**
     * 初始化，用于接收参数
     */
    public void initialize() {
        Bundle bundle = this.getIntent().getExtras();
        int sid = bundle.getInt("sid");
        if (sid > 0) {
            mSid = sid;
            url = showDao.getShowUrlById(mSid);
        }
    }

    /**
     * 初始化UI
     */
    public void initUI(){
        mShowBoxWeb = (ShowWebView) findViewById(R.id.show_box_web);
        //加载URL
        if(!url.isEmpty()){
            Log.e("showActivity", url);
            mShowBoxWeb.loadUrl(url);
        }
        Toast.makeText(this, "id = "+mSid, Toast.LENGTH_SHORT).show();

        initToolBar(null);
    }

    /**
     * 拦截返回按钮事件，如果webview有页面可返回则返回webview上一页面
     * @param keyCode 按钮
     * @param event 事件
     * @return 是否响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KEYCODE_BACK && mShowBoxWeb.canGoBack()){
            mShowBoxWeb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show, menu);

        //已登录且是管理员
        UserDao userDao = new UserDao();
        if(userDao.checkLogin(this) && userDao.getLevel(this) >= Constant.LEVEL_MANAGER){
            menu.findItem(R.id.menu_edit).setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_favorite:{
                /**----------wait to modify-----------**/

                Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();
//                mShowBoxWeb.loadUrl("javascript:setText('喜欢')");
                /**----------wait to modify-----------**/
            }break;
            case R.id.menu_share:{
                /**----------wait to modify-----------**/

                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();

                /**----------wait to modify-----------**/
            }break;
            case R.id.menu_edit:{
                /**----------跳转到节目编辑页-----------**/
                Intent intent = new Intent(this, EditShowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("sid", mSid);
                intent.putExtras(bundle);
                this.startActivity(intent);
                /**----------跳转到节目编辑页-----------**/
            }break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
