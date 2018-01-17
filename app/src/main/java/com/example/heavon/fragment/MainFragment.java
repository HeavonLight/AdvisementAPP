package com.example.heavon.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.heavon.adapter.RollLoopAdapter;
import com.example.heavon.dao.ShowDao;
import com.example.heavon.dao.TypeDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.myapplication.R;
import com.example.heavon.myapplication.ShowActivity;
import com.example.heavon.vo.Show;
import com.example.heavon.vo.ShowFilter;
import com.example.heavon.vo.Type;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //UI reference.
    private RollPagerView mRollViewPager;
    private OnFragmentInteractionListener mListener;
//    private ScrollView mShowsScrollView;
    private LinearLayout mShowsView;
//    private EditText mSearchEdit;
    private RelativeLayout mSearchView;


    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        //初始化搜索栏
        mSearchView = (RelativeLayout) view.findViewById(R.id.search_box);
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.search();
                }
            }
        });

        mRollViewPager = (RollPagerView) view.findViewById(R.id.roll_view_pager);
        initRecommand();
//        mShowsScrollView = (ScrollView) view.findViewById(R.id.lv_shows);

        mShowsView = (LinearLayout) view.findViewById(R.id.ll_shows);
        initTypeShows();
        return view;
    }

    //初始化主页节目列表
    public void initTypeShows(){
        if(mShowsView.getChildCount() <= 1){
            //获取所有类型
            final TypeDao typeDao = new TypeDao();
            typeDao.initTypes(new HttpResponse<Map<String, Object>>() {
                @Override
                public void getHttpResponse(Map<String, Object> result) {
                    if ((Boolean) result.get("error")) {
                        //error
                        Toast.makeText(getContext(), (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        List<Type> typeList = (List<Type>) result.get("typeList");
                        if (typeList == null || typeList.isEmpty()) {
                            Log.e("mainFragment", "typelist is null");
                        } else {
                            //更新节目列表
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            for (Type type : typeList) {
                                fragmentTransaction.add(R.id.ll_shows, TypeShowFragment.newInstance(type.getId(), type.getName(), 1));
                            }
                            fragmentTransaction.commit();
                        }
                    }
                }
            });
        }
    }

    //初始化推荐轮播图
    public void initRecommand(){
        //设置播放时间间隔
        mRollViewPager.setPlayDelay(3000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        ShowDao showDao = new ShowDao();
        ShowFilter filter = new ShowFilter();
        filter.put("perpage", "5");
        filter.put("page", "1");
        showDao.initShowsByFilter(filter, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if ((Boolean) result.get("error")) {
                    //error
                    Toast.makeText(getContext(), (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    final List<Show> showList = (List<Show>) result.get("showList");
                    if (showList == null || showList.isEmpty()) {
                        Log.e("mainFragment", "showlist is null");
                        return;
                    }
                    mRollViewPager.setAdapter(new RollLoopAdapter(mRollViewPager, showList));
                    //设置点击事件
                    mRollViewPager.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            //进入节目详情页
                            /**----------跳转到节目详情页-----------**/
                            Intent intent = new Intent(getContext(), ShowActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("sid", showList.get(position).getId());
                            intent.putExtras(bundle);
                            getContext().startActivity(intent);
//                Toast.makeText(getContext(), "click show "+view.getTag(), Toast.LENGTH_SHORT).show();
                            /**----------跳转到节目详情页-----------**/
//                            Toast.makeText(getContext(),"Item "+position+" clicked",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
        mRollViewPager.setHintView(new ColorPointHintView(getContext(), Color.argb(255,41,161,247),Color.WHITE));
        //mRollViewPager.setHintView(new TextHintView(this));
        //mRollViewPager.setHintView(null);
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
        void search();
    }
}
