package com.example.heavon.myapplication;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.heavon.gen.DaoMaster;
import com.example.heavon.gen.DaoSession;

import java.net.CookieStore;
import java.util.Map;

/**
 * Created by heavon on 2017/2/27.
 */

public class App extends Application {
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    //    private static final String SESSION_COOKIE = "JSESSIONID";
    private static final String SESSION_COOKIE = "PHPSESSID";

    private static App instance;
    private RequestQueue requestQueue;
    private SharedPreferences preferences;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private boolean sessionOn = true;

    public static App newInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        requestQueue = Volley.newRequestQueue(this);

        //更新数据库
        setupDatabase();
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }


    public void setSessionOn(boolean on) {
        sessionOn = on;
    }

    /**
     * 检查返回的Response header中有没有session
     *
     * @param responseHeaders Response Headers.
     */
    public final void checkSessionCookie(Map<String, String> responseHeaders) {
        Log.e("test head", responseHeaders.toString());
        if (!sessionOn) {
            return;
        }
        if (responseHeaders.containsKey(SET_COOKIE_KEY) &&
                responseHeaders.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = responseHeaders.get(SET_COOKIE_KEY);
            Log.e("test cc", cookie);

            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = preferences.edit();
                prefEditor.putString(SESSION_COOKIE, cookie);
                prefEditor.commit();
            }
        }
    }

    /**
     * 添加session到Request header中
     *
     * @param requestHeaders
     */
    public final void addSessionCookie(Map<String, String> requestHeaders) {
        if (!sessionOn) {
            return;
        }
        String sessionId = preferences.getString(SESSION_COOKIE, "");
        Log.e("test sse", sessionId);
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (requestHeaders.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(requestHeaders.get(COOKIE_KEY));
            }
            Log.e("appStart", "session get ");

            requestHeaders.put(COOKIE_KEY, builder.toString());
        }
    }

    /**
     * 清空session
     */
    public final void clearSessionCookie() {
        if (!sessionOn) {
            return;
        }
        String sessionId = preferences.getString(SESSION_COOKIE, "");
        Log.e("test delete", sessionId);
        if (sessionId.length() > 0) {
            preferences.edit().clear().commit();
        }
        Log.e("test delete after", preferences.getString(SESSION_COOKIE, ""));
    }

    private void setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
