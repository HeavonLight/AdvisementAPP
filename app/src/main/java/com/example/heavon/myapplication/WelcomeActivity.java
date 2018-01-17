package com.example.heavon.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.heavon.dao.UserDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.utils.HttpUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WelcomeActivity extends BasicActivity implements Runnable{

    SharedPreferences mSp;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //初始化配置
        TextView appName = (TextView) findViewById(R.id.app_name);
        appName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                handler.removeCallbacks(WelcomeActivity.this);
                final EditText editText = new EditText(WelcomeActivity.this);
                AlertDialog dlg = new AlertDialog.Builder(WelcomeActivity.this)
                        .setTitle("修改HostIP")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String ip = editText.getText().toString();
                                Pattern pattern = Pattern.compile("^(([0-9]{1,3})\\.){3}([0-9]{1,3})$");
                                if (pattern.matcher(ip).matches()) {
                                    HttpUtils.setHostIP(ip);
                                    WelcomeActivity.super.gotoMain();
                                } else {
                                    Toast.makeText(WelcomeActivity.this, "请填写正确IP", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                return false;
            }
        });
        //判断登录状态
        mSp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        handler = new Handler();
        handler.postDelayed(this, 2000); //2000 for release
    }

    @Override
    public void run() {
        final Boolean autoLogin = mSp.getBoolean("AUTO_ISCHECK", false);
        if (autoLogin) {
            UserDao dao = new UserDao();
            String username = mSp.getString("USER_NAME", "");
            String password = mSp.getString("PASSWORD", "");
            dao.login(username, password, new HttpResponse<Map<String, Object>>() {
                @Override
                public void getHttpResponse(Map<String, Object> result) {
                    if ((Boolean) result.get("error")) {
                        gotoLogin();
                    } else {
                        int uid = (int) result.get("uid");
//                                String hashcode = result.get("hashcode").toString();
                        Log.i("login", String.valueOf(uid) + " login!");
                        //登录成功保存登录信息
                        SharedPreferences.Editor editor = mSp.edit();
                        editor.putInt("USER_ID", uid);
//                                editor.putString("HASHCODE", hashcode);
                        editor.commit();

                        //进入主界面
                        gotoMain();
                    }
                }
            });
        } else {
            //跳转到登录页面
            gotoLogin();
        }
    }
}
