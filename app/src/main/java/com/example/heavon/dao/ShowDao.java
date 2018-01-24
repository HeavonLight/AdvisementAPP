package com.example.heavon.dao;

import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.myapplication.R;
import com.example.heavon.vo.ShowFilter;
import com.example.heavon.utils.HttpUtils;
import com.example.heavon.vo.Show;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.heavon.vo.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heavon on 2017/2/14.
 */

public class ShowDao extends BaseDao {

    /**
     * 根据筛选条件初始化节目列表
     *
     * @param filter   过滤条件
     * @param response 回调方法
     * @return
     */
    public void initShowsByFilter(ShowFilter filter, final HttpResponse<Map<String, Object>> response) {
        List<Show> showList = new ArrayList<Show>();
        HttpUtils http = new HttpUtils();
        String getShowsUrl = http.getHost() + "Show/getShows";

        http.postString(getShowsUrl, new Response.Listener<String>() {
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
                        //节目列表数据
                        JSONArray dataList = json.getJSONArray("data");
                        String dataListString = JSON.toJSONString(dataList);
                        Log.e("showList", dataListString);
                        List<Show> showList = JSON.parseArray(dataListString, Show.class);

                        returnMap.put("showList", showList);
                    }
                } catch (Exception e) {
                    Log.e("showError", e.getMessage());
                    e.getStackTrace();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("error", true);
                    map.put("msg", e.getMessage());
                    returnMap = map;
                }
                Log.e("show", returnMap.toString());
                //将returnMap作为参数回调
                response.getHttpResponse(returnMap);
            }
        }, filter.getFilter());

    }

    /**
     * 获取节目详情页的URL
     * @param id 节目id
     * @return 节目详情url链接
     */
    public String getShowUrlById(int id) {
        String showUrl = HttpUtils.getHost() + "Show/getShow/id/" + String.valueOf(id);

        return showUrl;
    }

    /**
     * 获取节目编辑页的URL
     * @param id 节目id
     * @return 节目编辑页的url链接
     */
    public String getEditShowUrlById(int id){
        String editShowUrl = HttpUtils.getHost() + "Show/editShow/id/" + String.valueOf(id);

        return editShowUrl;
    }

    /**
     * 获取节目添加页的URL
     * @return 节目添加页的url链接
     */
    public String getAddShowUrl(){

        return HttpUtils.getHost() + "Show/addShow";
    }

    /**
     * 获取筛选信息列表
     * @param filter    筛选信息名称
     * @param response  回调方法
     */
    public void getFilterList(String filter, final HttpResponse<Map<String, Object>> response){

        HttpUtils http = new HttpUtils();
        String filtersUrl = http.getHost() + "Show/getFilters";
        ShowFilter showFilter = new ShowFilter("filter", filter);

        http.postString(filtersUrl, new Response.Listener<String>() {
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
                        //
                        JSONArray dataList = json.getJSONArray("data");

                        Log.e("showDao", dataList.toString());
                        String dataListString = JSON.toJSONString(dataList);
                        Log.e("showList", dataListString);
                        List<Type> list = JSON.parseArray(dataListString, Type.class);
//                        List<String> filterList = new ArrayList<String>();
//                        for(int i = 0; i < list.size(); i++){
//                            filterList.put( list.get(i).get("name").toString() );
//                        }
                        returnMap.put("filterList", list);
                        //
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("error", true);
                    map.put("msg", e.getMessage());
                    returnMap = map;
                }
                Log.e("show", returnMap.toString());
                //将returnMap作为参数回调
                response.getHttpResponse(returnMap);
            }
        }, showFilter.getFilter());
    }

    public Show getShowById(int id) {
        Show show = new Show();

        return show;
    }

    public Show getShowByFilter(ShowFilter filter, final HttpResponse<Map<String, Object>> response) {
        Show show = new Show();

        return show;
    }

    public List<Show> getFavoriteShows() {
        List<Show> favoriteList = new ArrayList<Show>();


        return favoriteList;
    }

    /**
     * 获取招商状态背景颜色
     * @param investment    招商状态
     * @return              招商状态背景颜色
     */
    public @DrawableRes int getInvestmentColor(String investment){
        if(TextUtils.equals(investment, "未招商")){
            return R.color.colorInvestmentNot;
        }else if(TextUtils.equals(investment, "招商结束")){
            return R.color.colorInvestmentEnd;
        }else if(TextUtils.equals(investment, "招商中")){
            return R.color.colorInvestmentIng;
        }else if(TextUtils.equals(investment, "待定")){
            return R.color.colorInvestmentDoing;
        }
        return R.color.colorInvestmentNone;
    }
}
