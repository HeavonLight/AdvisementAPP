package com.example.heavon.myapplication;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heavon.adapter.MoreShowAdapter;
import com.example.heavon.dao.ShowDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.vo.Show;
import com.example.heavon.vo.ShowFilter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoreShowActivity extends BasicActivity {

    private final int PERPAGE = 6;
    private String mType = "分类";
    private long mTypeId = 0;
    private int mCurPage = 1;
    private List<Show> mShowList;

    //UI reference.
    FrameLayout mShowListScrollView;

    private TextView mShowNone;
    private XRecyclerView mShowRecyclerView;;

    private LinearLayoutManager mLayoutManager;
    //adapter.
    private MoreShowAdapter mMoreShowAdapter;
    private int lastVisibleItem = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_show);

        initialize();
        initUI();
    }

    /**
     * 初始化，用于接收参数
     */
    public void initialize() {
        Bundle bundle = this.getIntent().getExtras();
        String type = bundle.getString("type");
        Long typeid = bundle.getLong("typeid");
        if (type != null && !type.isEmpty()) {
            mType = type;
        }
        if(typeid != null && typeid >= 0){
            mTypeId = typeid;
        }
    }

    /**
     * 初始化UI
     */
    public void initUI() {

        mShowNone = (TextView) findViewById(R.id.show_none);
        // Refresh scroll.
        mShowListScrollView = (FrameLayout) findViewById(R.id.show_list_scroll);

        // Show list.
        mShowRecyclerView = (XRecyclerView) findViewById(R.id.show_recycler_view);
        mShowRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mShowRecyclerView.setLayoutManager(mLayoutManager);
        mShowRecyclerView.setLoadingMoreEnabled(true);
        mShowRecyclerView.setPullRefreshEnabled(true);
        mShowRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        View footView = LayoutInflater.from(this).inflate(R.layout.view_foot, null);
        footView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mShowRecyclerView.setFootView(footView);

        mShowRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshShowList();
            }

            @Override
            public void onLoadMore() {
                loadMoreShow();
            }
        });
        //初始化节目列表
        initShowList();
        initToolBar(mType);
    }

    /**
     * 初始化节目列表
     */
    public void initShowList() {
        ShowDao dao = new ShowDao();
        ShowFilter filter = new ShowFilter("typeid", String.valueOf(mTypeId));
        Log.e("moreShowLocalization", String.valueOf(mTypeId));
        filter.put("perpage", String.valueOf(PERPAGE));
        filter.put("page", String.valueOf(getCurPage()));
        mShowList = new ArrayList<Show>();
        mMoreShowAdapter = new MoreShowAdapter(mShowList, MoreShowActivity.this);
        mShowRecyclerView.setAdapter(mMoreShowAdapter);
        dao.initShowsByFilter(filter, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if ((Boolean) result.get("error")) {
                    //error
                    Toast.makeText(MoreShowActivity.this, (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    List<Show> showList = (List<Show>) result.get("showList");
                    if (showList == null || showList.isEmpty()) {
                        Log.e("moreshowActivity", "showlist is null");
                        mShowList.clear();
                        if (mShowNone != null) {
                            mShowNone.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mShowList = showList;
                        if (mShowNone != null && mShowNone.getVisibility() == View.VISIBLE) {
                            mShowNone.setVisibility(View.INVISIBLE);
                        }
                    }
                    //更新节目列表
                    mMoreShowAdapter.setData(mShowList);
                }
            }
        });
    }

    /**
     * 刷新节目列表
     */
    public void refreshShowList(){
        ShowDao dao = new ShowDao();
        ShowFilter filter = new ShowFilter("typeid", String.valueOf(mTypeId));
        filter.put("perpage", String.valueOf(PERPAGE * getCurPage()));
        filter.put("page", String.valueOf(1));
        Log.e("moreShow refresh perpage", String.valueOf(filter.getFilter().get("perpage")));
        dao.initShowsByFilter(filter, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if ((Boolean) result.get("error")) {
                    //失败
                    Toast.makeText(MoreShowActivity.this, (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    //成功
                    List<Show> showList = (List<Show>) result.get("showList");
                    //判断空状态
                    if (showList == null || showList.isEmpty()) {
                        Log.e("moreshowActivity", "showlist is null");
                        mShowList.clear();
                        if (mShowNone != null) {
                            mShowNone.setVisibility(View.VISIBLE);
                        }
                    }else{
                        mShowList = showList;
                        if (mShowNone != null && mShowNone.getVisibility() == View.VISIBLE) {
                            mShowNone.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                //更新节目列表
                mMoreShowAdapter.setData(mShowList);
                //结束刷新
                mShowRecyclerView.refreshComplete();
            }

        });
    }
    /**
     * 加载更多节目
     */
    public void loadMoreShow() {
        ShowDao dao = new ShowDao();
        ShowFilter filter = new ShowFilter("typeid", String.valueOf(mTypeId));
        filter.put("perpage", String.valueOf(PERPAGE));
        filter.put("page", String.valueOf(getCurPage() + 1));
        Log.e("moreShow loadMore Page", String.valueOf(filter.getFilter().get("page")));
        dao.initShowsByFilter(filter, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if ((Boolean) result.get("error")) {
                    //失败
                    Toast.makeText(MoreShowActivity.this, (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    //成功
                    List<Show> showList = (List<Show>) result.get("showList");
                    if (showList == null || showList.isEmpty()) {
                        Log.e("moreshowActivity", "showlist is null");
                        mShowRecyclerView.setNoMore(true);
                    }else{
                        //判断空状态
                        if (mShowNone != null && mShowNone.getVisibility() == View.VISIBLE) {
                            mShowNone.setVisibility(View.VISIBLE);
                        }
                        //添加加载内容
                        mShowList.addAll(showList);
                        //下一页
                        nextPage();
                        mShowRecyclerView.setNoMore(false);
                    }
                }
                //停止加载
                mShowRecyclerView.loadMoreComplete();
            }
        });
    }

    /**
     * 获取当前页面号
     *
     * @return 当前页面
     */
    private int getCurPage() {
        int curPage = 1;

        return mCurPage;
    }

    /**
     * 翻页
     */
    private void nextPage() {
        mCurPage++;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_list) {
            /**----------wait to modify-----------**/

            Toast.makeText(this, "change list", Toast.LENGTH_SHORT).show();

            /**----------wait to modify-----------**/

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
