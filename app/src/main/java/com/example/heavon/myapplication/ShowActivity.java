package com.example.heavon.myapplication;

import android.content.res.ObbInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.heavon.dao.ShowDao;
import com.example.heavon.utils.HttpUtils;

import static android.view.KeyEvent.KEYCODE_BACK;

public class ShowActivity extends BasicActivity {

    private int mSid = 0;
    private String url= "";

    private ShowDao showDao = new ShowDao();
    // UI preference.
    private WebView mShowBoxWeb;

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
        mShowBoxWeb = (WebView) findViewById(R.id.show_box_web);
        mShowBoxWeb.getSettings().setJavaScriptEnabled(true);
        mShowBoxWeb.getSettings().setDefaultTextEncodingName("utf-8");
        mShowBoxWeb.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void toast(String msg){
                Toast.makeText(ShowActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }, "showObj");
        mShowBoxWeb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                shouldOverrideUrlLoading(view, request);
                return true;
            }
        });
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_favorite:{
                /**----------wait to modify-----------**/

                Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();
                mShowBoxWeb.loadUrl("javascript:setText('喜欢')");
                /**----------wait to modify-----------**/
            }break;
            case R.id.menu_share:{
                /**----------wait to modify-----------**/

                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();

                /**----------wait to modify-----------**/
            }break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
