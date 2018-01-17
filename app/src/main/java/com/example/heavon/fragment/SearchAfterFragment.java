package com.example.heavon.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heavon.adapter.MoreShowAdapter;
import com.example.heavon.dao.SearchDao;
import com.example.heavon.dao.UserDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.myapplication.R;
import com.example.heavon.vo.Show;
import com.example.heavon.vo.ShowFilter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchAfterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchAfterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchAfterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEYWORD = "keyword";
    private static final String PAGE = "page";

    private final int PERPAGE = 4;
    // TODO: Rename and change types of parameters
    private List<Show> mShowList;
    private String mKeyword;
    private int mPage = 1;

    // UI preference.
    private OnFragmentInteractionListener mListener;
    private TextView mShowNone;
    private XRecyclerView mShowListView;
    private MoreShowAdapter mMoreShowAdapter;
//    private RequestQueue mQueue;

    public SearchAfterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param keyword Parameter 1.
     * @param page    Parameter 2.
     * @return A new instance of fragment SearchAfterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchAfterFragment newInstance(String keyword, int page) {
        SearchAfterFragment fragment = new SearchAfterFragment();
        Bundle args = new Bundle();
        args.putString(KEYWORD, keyword);
        args.putInt(PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mKeyword = getArguments().getString(KEYWORD);
            mPage = getArguments().getInt(PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_after, container, false);
//        mQueue = Volley.newRequestQueue(getContext());

        mShowNone = (TextView) view.findViewById(R.id.show_none);
        //搜索结果列表
        mShowListView = (XRecyclerView) view.findViewById(R.id.show_recycler_view);
        mShowListView.setHasFixedSize(true);
        final LinearLayoutManager layoutManger = new LinearLayoutManager(getContext());
        layoutManger.setOrientation(LinearLayoutManager.VERTICAL);
        mShowListView.setLayoutManager(layoutManger);
        mShowListView.setPullRefreshEnabled(false);
        mShowListView.setLoadingMoreEnabled(true);
        mShowListView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //刷新
                initShowList();
            }

            @Override
            public void onLoadMore() {
                //加载
                loadMoreShow();
            }
        });

        initShowList();
        return view;
    }

    /**
     * 初始化节目列表
     */
    public void initShowList() {
        SearchDao searchDao = new SearchDao();
        ShowFilter filter = new ShowFilter("keyword", mKeyword);
        filter.put("perpage", String.valueOf(PERPAGE));
        filter.put("page", String.valueOf(mPage));
        searchDao.searchShowsByFilter(filter, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if ((Boolean) result.get("error")) {
                    //失败
                    Toast.makeText(getContext(), (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    //成功
                    List<Show> showList = (List<Show>) result.get("showList");
                    if (showList == null || showList.isEmpty()) {
                        Log.e("searchAfterFragment", "showlist is null");
                        return;
                    }
                    mShowList = showList;
                    //显示搜索结果
                    if (mShowNone != null && mShowNone.getVisibility() == View.VISIBLE) {
                        mShowNone.setVisibility(View.INVISIBLE);
                    }

                    mMoreShowAdapter = new MoreShowAdapter(showList, getContext());
                    mShowListView.setAdapter(mMoreShowAdapter);
                }
                // 刷新完成必须调用此方法停止刷新
                mShowListView.refreshComplete();
            }
        });
        //保存本地搜索历史
        if (!(new UserDao().checkLogin(getContext()))) {
            searchDao.addSearchHistory(mKeyword);
            Log.e("searchAfterFragment", "local history search put.");
        }
    }

    /**
     * 加载更多节目
     */
    public void loadMoreShow() {
        SearchDao searchDao = new SearchDao();
        ShowFilter filter = new ShowFilter("keyword", mKeyword);
        filter.put("perpage", String.valueOf(PERPAGE));
        filter.put("page", String.valueOf(getCurPage() + 1));
        searchDao.searchShowsByFilter(filter, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if ((Boolean) result.get("error")) {
                    //失败
                    Toast.makeText(getContext(), (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    //成功
                    List<Show> showList = (List<Show>) result.get("showList");
                    if (showList == null || showList.isEmpty()) {
                        Log.e("searchAfterFragment", "showlist is null");
                    } else {
                        //判断空状态
                        if (mShowNone != null && mShowNone.getVisibility() == View.VISIBLE) {
                            mShowNone.setVisibility(View.INVISIBLE);
                        }
                        //添加加载内容
                        mShowList.addAll(showList);
                        for (Show show : showList) {
                            mMoreShowAdapter.insert(show, mMoreShowAdapter.getAdapterItemCount());
                        }

                        //下一页
                        nextPage();
                    }
                }
                // 加载完成必须调用此方法停止加载
                mShowListView.loadMoreComplete();
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

        return mPage;
    }

    /**
     * 翻页
     */
    private void nextPage() {
        mPage++;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
