package com.example.heavon.dao;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.utils.HttpUtils;
import com.example.heavon.vo.Show;
import com.example.heavon.vo.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heavon on 2017/5/12.
 */

public class TypeDao {

    public void initTypes(final HttpResponse<Map<String, Object>> response){
        final List<Type> typeList = new ArrayList<Type>();
        HttpUtils http = new HttpUtils();
        String getTypesUrl = http.getHost() + "Show/getTypes";

        http.getString(getTypesUrl, new Response.Listener<String>() {
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
                    } else {
                        //分类列表数据
                        JSONArray dataList = json.getJSONArray("data");
                        String dataListString = JSON.toJSONString(dataList);
                        Log.e("typeList", dataListString);
                        List<Type> typeList = JSON.parseArray(dataListString, Type.class);

                        returnMap.put("typeList", typeList);
                    }
                } catch (Exception e) {
                    Log.e("typeError", e.getMessage());
                    e.getStackTrace();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("error", true);
                    map.put("msg", e.getMessage());
                    returnMap = map;
                }
                Log.e("type", returnMap.toString());
                //将returnMap作为参数回调
                response.getHttpResponse(returnMap);
            }
        });
    }
}
