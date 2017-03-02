package com.example.heavon.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.heavon.constant.Constant;
import com.example.heavon.interfaceClasses.Filter;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.myapplication.App;
import com.example.heavon.utils.HttpUtils;
import com.example.heavon.vo.Search;
import com.example.heavon.vo.Show;
import com.example.heavon.vo.ShowFilter;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yadong on 16/3/5.
 */
public class SearchDao extends BaseDao {

    com.example.heavon.gen.SearchDao db;

    /**
     * 初始化搜索历史记录
     *
     * @param response 回调方法
     * @return 是否获取成功
     */
    public boolean initSearchHistory(final HttpResponse<Map<String, Object>> response) {
//        //参数检查
//        if(queue == null){
//            Log.e("searchDao", "queue is null.");
//            return false;
//        }

        final HttpUtils http = new HttpUtils();
        String url = http.getHost() + "Search/getHistory";
        http.getString(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                Map<String, Object> returnMap = new HashMap<String, Object>();
                JSONObject json;
                Boolean responseError;
                try {
                    json = JSON.parseObject(s);
                    responseError = json.getBoolean("error");
                    returnMap.put("error", responseError);
                    if (responseError) {
                        String msg = json.getString("data");
                        if ("必须用post方式".equals(msg)) {
                            msg = "请求链接失效";
                        }
                        returnMap.put("msg", msg);
                        //未登录
                        if (json.getInteger("code") == Constant.CODE_UNLOGIN) {
                            returnMap.put("isLogined", false);
                        }
                    } else {
                        //
                        JSONArray dataList = json.getJSONArray("data");
                        Log.e("historyList", dataList.toString());
                        String dataListString = JSON.toJSONString(dataList);
                        Log.e("historyList", dataListString);
                        List<Search> historyList = JSON.parseArray(dataListString, Search.class);

                        returnMap.put("historyList", historyList);
                        returnMap.put("isLogined", true);
                        //
                    }
                } catch (Exception e) {
                    Log.e("searchError", e.getMessage());
                    e.getStackTrace();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("error", true);
                    map.put("msg", e.getMessage());
                    returnMap = map;
                }
                Log.e("searchHistory", returnMap.toString());
                //将returnMap作为参数回调
                response.getHttpResponse(returnMap);
            }
        });

        return true;
    }

    /**
     * 初始化热搜榜
     *
     * @param response 回调方法
     * @return 是否获取成功
     */
    public boolean initSearchHot(final HttpResponse<Map<String, Object>> response) {
        HttpUtils http = new HttpUtils();
        String url = http.getHost() + "Search/getHot";
        http.getString(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Map<String, Object> returnMap = new HashMap<String, Object>();
                JSONObject json;
                Boolean responseError;
                try {
                    //失败
                    json = JSON.parseObject(s);
                    responseError = json.getBoolean("error");
                    returnMap.put("error", responseError);
                    if (responseError) {
                        String msg = json.getString("msg");
                        if ("必须用post方式".equals(msg)) {
                            msg = "请求链接失效";
                        }
                        returnMap.put("msg", msg);
                    } else {
                        //成功
                        JSONArray dataList = json.getJSONArray("data");
                        Log.e("hotList", dataList.toString());
                        String dataListString = JSON.toJSONString(dataList);
                        Log.e("hotList", dataListString);
                        List<Search> hotList = JSON.parseArray(dataListString, Search.class);

                        returnMap.put("hotList", hotList);
                        //
                    }
                } catch (Exception e) {
                    Log.e("searchError", e.getMessage());
                    e.getStackTrace();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("error", true);
                    map.put("msg", e.getMessage());
                    returnMap = map;
                }
                Log.e("searchHot", returnMap.toString());
                //将returnMap作为参数回调
                response.getHttpResponse(returnMap);
            }
        });

        return true;
    }

    /**
     * 根据过滤条件搜索节目
     *
     * @param filter   过滤条件
     * @param response 回调方法
     * @return 是否搜索成功
     */
    public boolean searchShowsByFilter(ShowFilter filter, final HttpResponse<Map<String, Object>> response) {
//        //参数检查
//        if(queue == null){
//            Log.e("searchDao", "queue is null.");
//            return false;
//        }

        final HttpUtils http = new HttpUtils();
        String url = http.getHost() + "Search/index";
        http.postString(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                Map<String, Object> returnMap = new HashMap<String, Object>();
                JSONObject json;
                Boolean responseError;
                try {
                    json = JSON.parseObject(s);
                    responseError = json.getBoolean("error");
                    returnMap.put("error", responseError);
                    if (responseError) {
                        //失败
                        String msg = json.getString("data");
                        /**--------待封装----------**/
                        if ("必须用post方式".equals(msg)) {
                            msg = "请求链接失效";
                        }
                        returnMap.put("msg", msg);
                    } else {
                        //成功
                        JSONArray dataList = json.getJSONArray("data");
                        Log.e("searchList", dataList.toString());
                        String dataListString = JSON.toJSONString(dataList);
                        Log.e("searchListString", dataListString);
                        List<Show> showList = JSON.parseArray(dataListString, Show.class);

                        returnMap.put("showListString", dataListString);
                        returnMap.put("showList", showList);
                        //
                    }
                } catch (Exception e) {
                    Log.e("showError", e.getMessage());
                    e.getStackTrace();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("error", true);
                    map.put("msg", e.getMessage());
                    returnMap = map;
                }
                Log.e("searchReturn", returnMap.toString());
                //将returnMap作为参数回调
                response.getHttpResponse(returnMap);
            }
        }, filter.getFilter());

        return true;
    }

    /**
     * 清空搜索历史记录
     */
    public boolean clearSearchHistory(final HttpResponse<Map<String, Object>> response) {
        HttpUtils http = new HttpUtils();
        String url = http.getHost() + "Search/deleteSearch";
        http.postString(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Map<String, Object> returnMap = new HashMap<String, Object>();
                JSONObject json;
                Boolean responseError;
                try {
                    //失败
                    json = JSON.parseObject(s);
                    responseError = json.getBoolean("error");
                    returnMap.put("error", responseError);
                    String msg = json.getString("data");
                    if (responseError) {
                        //失败
                        if ("必须用post方式".equals(msg)) {
                            msg = "请求链接失效";
                        }
                        //未登录
                        if (json.getInteger("code") == Constant.CODE_UNLOGIN) {
                            returnMap.put("isLogined", false);
                        }
                    } else {
                        //成功
                        Log.e("searchDeleteResult", msg);
                    }
                    returnMap.put("msg", msg);
                } catch (Exception e) {
                    Log.e("searchError", e.getMessage());
                    e.getStackTrace();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("error", true);
                    map.put("msg", e.getMessage());
                    returnMap = map;
                }
                Log.e("clearSearch", returnMap.toString());
                //将returnMap作为参数回调
                response.getHttpResponse(returnMap);
            }
        }, new HashMap<String, String>());

        return true;
    }

    /**
     * 添加本地搜索记录
     *
     * @param content 搜索内容
     */
    public void addSearchHistory(String content) {
        db = App.newInstance().getDaoSession().getSearchDao();

        //如果有内容则更新，无内容则插入
        long count = db.queryBuilder()
                .where(com.example.heavon.gen.SearchDao.Properties.Content.eq(content))
                .count();
        if (count > 0) {
            //更新查询时间
            db.update(new Search(content, new Date().getTime()));
        } else {
            //插入搜索记录
            db.insert(new Search(content, new Date().getTime()));
        }
    }

    /**
     * 获取本地搜索历史记录
     *
     * @return 搜索历史列表
     */
    public List<Search> getSearchHistory() {
        db = App.newInstance().getDaoSession().getSearchDao();

        //查询搜索历史
        List<Search> historyList = db.queryBuilder().limit(4)
                .orderDesc(com.example.heavon.gen.SearchDao.Properties.Time)
                .build().list();

        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        //查询结果以 List 返回
        return historyList;

    }

    /**
     * 清空本地搜索历史记录
     */
    public void clearSearchHistory() {
        db = App.newInstance().getDaoSession().getSearchDao();

        //清空搜索历史
        db.deleteAll();
    }


}
