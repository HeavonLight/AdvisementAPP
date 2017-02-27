package com.example.heavon.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.heavon.adapter.HistoryAdapter;
import com.example.heavon.adapter.MoreShowAdapter;
import com.example.heavon.dao.SearchDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.myapplication.R;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchBeforeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchBeforeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchBeforeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    // UI preference.
    private RecyclerView mSearchHistoryListView;
    private RecyclerView mSearchHotListView;
    private RequestQueue mQueue;
    private List<String> mHistoryList;
    private LinearLayout mSearchHistoryView;
    private LinearLayout mSearchHotView;

    //适配器
    private HistoryAdapter mHistoryAdapter;

    public SearchBeforeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchBeforeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchBeforeFragment newInstance(String param1, String param2) {
        SearchBeforeFragment fragment = new SearchBeforeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_before, container, false);
        mQueue = Volley.newRequestQueue(getContext());

        mSearchHistoryView = (LinearLayout) view.findViewById(R.id.search_history);
        mSearchHotView = (LinearLayout) view.findViewById(R.id.search_hot);

        // 搜索历史列表
        mSearchHistoryListView = (RecyclerView) view.findViewById(R.id.search_history_view);
        mSearchHistoryListView.setHasFixedSize(true);
        final LinearLayoutManager historyLayoutManger = new LinearLayoutManager(getContext());
        historyLayoutManger.setOrientation(LinearLayoutManager.VERTICAL);
        mSearchHistoryListView.setLayoutManager(historyLayoutManger);

        //热搜榜
        mSearchHotListView = (RecyclerView) view.findViewById(R.id.search_hot_view);
        mSearchHotListView.setHasFixedSize(true);
        final GridLayoutManager hotLayoutManager = new GridLayoutManager(getContext(), 2);
        mSearchHotListView.setLayoutManager(hotLayoutManager);

        return view;
    }

    public void initSearchHistory(){
        SearchDao searchDao = new SearchDao();
        searchDao.initSearchHistory(getContext(), mQueue, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if((Boolean) result.get("error")){
                    //失败
                    Toast.makeText(getContext(), (String)result.get("msg"), Toast.LENGTH_SHORT).show();
                    mSearchHistoryView.setVisibility(View.INVISIBLE);
                }else {
                    //成功
                    List<String> historyList = (List<String>) result.get("historyList");
                    if(historyList == null || historyList.isEmpty()){
                        Log.e("searchBeforeFragment", "history is null" );
                        mSearchHistoryView.setVisibility(View.INVISIBLE);
                        return;
                    }

                    mHistoryList = historyList;
                    //显示搜索历史
                    mSearchHistoryView.setVisibility(View.VISIBLE);
//                    if (mShowNone != null && mShowNone.getVisibility() == View.VISIBLE) {
//                        mShowNone.setVisibility(View.INVISIBLE);
//                    }

                    mHistoryAdapter = new HistoryAdapter(historyList, getContext());
                    mSearchHistoryListView.setAdapter(mHistoryAdapter);
                }
            }
        });
    }

    public void initSearchHot(){

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