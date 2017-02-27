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
import com.example.heavon.interfaceClasses.Filter;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.utils.HttpUtils;
import com.example.heavon.vo.Show;
import com.example.heavon.vo.ShowFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yadong on 16/3/5.
 */
public class SearchDao extends BaseDao{

    public boolean initSearchHistory(Context context, RequestQueue queue, final HttpResponse<Map<String, Object>> response){
        //参数检查
        if(queue == null){
            Log.e("searchDao", "queue is null.");
            return false;
        }
        //检查登录状态
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int uid = sp.getInt("USER_ID", 0);
        if(uid == 0){
            Log.e("searchDao", "user not login.");
            return false;
        }

        final HttpUtils http = new HttpUtils(queue);
        String url = http.getHost() + "Search/getHistory";
        http.getString(url, new Response.Listener<String>(){

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
                        if("必须用post方式".equals(msg)){
                            msg = "请求链接失效";
                        }
                        returnMap.put("msg", msg);
                    } else {
                        //
                        JSONArray dataList = json.getJSONArray("data");
                        Log.e("historyList",dataList.toString());
                        String dataListString = JSON.toJSONString(dataList);
                        Log.e("historyList", dataListString );
                        List<String> historyList = JSON.parseArray(dataListString, String.class);

                        returnMap.put("historyList", historyList);
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

    public Boolean searchShowsByFilter(ShowFilter filter, RequestQueue queue, final HttpResponse<Map<String, Object>> response){
        //参数检查
        if(queue == null){
            Log.e("searchDao", "queue is null.");
            return false;
        }

        final HttpUtils http = new HttpUtils(queue);
        String url = http.getHost() + "Search/index";
        http.postString(url, new Response.Listener<String>(){

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
                        if("必须用post方式".equals(msg)){
                            msg = "请求链接失效";
                        }
                        returnMap.put("msg", msg);
                    } else {
                        //成功
                        JSONArray dataList = json.getJSONArray("data");
                        Log.e("searchList",dataList.toString());
                        String dataListString = JSON.toJSONString(dataList);
                        Log.e("searchListString", dataListString );
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

}
