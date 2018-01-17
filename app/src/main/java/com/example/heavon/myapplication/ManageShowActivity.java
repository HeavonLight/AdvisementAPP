package com.example.heavon.myapplication;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
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

import com.example.heavon.adapter.ManageShowAdapter;
import com.example.heavon.dao.ShowDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.views.ManageShowContentView;
import com.example.heavon.vo.Show;
import com.example.heavon.vo.ShowFilter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManageShowActivity extends BasicActivity implements ManageShowAdapter.OnSlidingViewClickListener{

    private final int PERPAGE = 6;
    private String mType;
    private long mTypeId;
    private int mCurPage = 1;
    private List<Show> mShowList;

    //UI reference.
    FrameLayout mShowListScrollView;

    private TextView mShowNone;
    private XRecyclerView mShowRecyclerView;

    private LinearLayoutManager mLayoutManager;
    //adapter.
    private ManageShowAdapter mManageShowAdapter;
    private int lastVisibleItem = 0;

    private ShowFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_show);

        initialize();
        initUI();
    }

    /**
     * 初始化，用于接收参数
     */
    public void initialize() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String type = bundle.getString("type");
            Long typeid = bundle.getLong("typeid");
            if (type != null && !type.isEmpty()) {
                mType = type;
            }
            if (typeid != null && typeid >= 0) {
                mTypeId = typeid;
            }
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
        mShowRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
        initToolBar(null);
    }

    /**
     * 初始化节目列表
     */
    public void initShowList() {
        ShowDao dao = new ShowDao();
        if(mType != null){
            filter = new ShowFilter("localization", String.valueOf(mTypeId));
            Log.e("manageShowLocalization", String.valueOf(mTypeId));
        }else{
            filter = new ShowFilter();
        }
        filter.put("perpage", String.valueOf(PERPAGE));
        filter.put("page", String.valueOf(getCurPage()));
        mShowList = new ArrayList<Show>();
        mManageShowAdapter = new ManageShowAdapter(mShowList, ManageShowActivity.this);
        mShowRecyclerView.setAdapter(mManageShowAdapter);
        dao.initShowsByFilter(filter, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if ((Boolean) result.get("error")) {
                    //error
                    Toast.makeText(ManageShowActivity.this, (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    List<Show> showList = (List<Show>) result.get("showList");
                    if (showList == null || showList.isEmpty()) {
                        Log.e("manageShowActivity", "showlist is null");
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
                    mManageShowAdapter.setData(mShowList);
                }
            }
        });
    }

    /**
     * 刷新节目列表
     */
    public void refreshShowList(){
        ShowDao dao = new ShowDao();
        filter.put("perpage", String.valueOf(PERPAGE * getCurPage()));
        filter.put("page", String.valueOf(1));
        dao.initShowsByFilter(filter, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if ((Boolean) result.get("error")) {
                    //失败
                    Toast.makeText(ManageShowActivity.this, (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    //成功
                    List<Show> showList = (List<Show>) result.get("showList");
                    //判断空状态
                    if (showList == null || showList.isEmpty()) {
                        Log.e("manageShowActivity", "showlist is null");
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
                mManageShowAdapter.setData(mShowList);
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
        filter.put("perpage", String.valueOf(PERPAGE));
        filter.put("page", String.valueOf(getCurPage() + 1));
        dao.initShowsByFilter(filter, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if ((Boolean) result.get("error")) {
                    //失败
                    Toast.makeText(ManageShowActivity.this, (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    //成功
                    List<Show> showList = (List<Show>) result.get("showList");
                    if (showList == null || showList.isEmpty()) {
                        Log.e("manageShowActivity", "showlist is null");
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
        getMenuInflater().inflate(R.menu.menu_manage_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_select) {
            /**----------wait to modify-----------**/

            Toast.makeText(this, "select show", Toast.LENGTH_SHORT).show();

            /**----------wait to modify-----------**/

            return true;
        } else if (item.getItemId() == R.id.menu_search) {
            /**----------wait to modify-----------**/

            Toast.makeText(this, "search show", Toast.LENGTH_SHORT).show();

            /**----------wait to modify-----------**/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        //图片点击事件
        Toast.makeText(this, "click "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        //删除按钮点击事件
        Toast.makeText(this, "delete "+position, Toast.LENGTH_SHORT).show();
    }
}
