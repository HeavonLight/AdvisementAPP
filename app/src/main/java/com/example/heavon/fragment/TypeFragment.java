package com.example.heavon.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.heavon.adapter.TypeAdapter;
import com.example.heavon.dao.TypeDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.myapplication.BasicActivity;
import com.example.heavon.myapplication.R;
import com.example.heavon.vo.Type;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TypeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TypeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private XRecyclerView mTypeListView;
    private List<Type> mTypeList;
    private TypeAdapter mTypeAdapter;

    public TypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TypeFragment newInstance(String param1, String param2) {
        TypeFragment fragment = new TypeFragment();
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
        View view = inflater.inflate(R.layout.fragment_type, container, false);
        //工具栏位置调整（沉浸式）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout typeLayout = (LinearLayout) view.findViewById(R.id.fragment_type);
            int barHeight = typeLayout.getPaddingTop();
            if(getContext() instanceof Activity){
                barHeight = BasicActivity.getStatusBarHeight((Activity) getContext());
            }
            typeLayout.setPadding(typeLayout.getPaddingLeft(),barHeight, typeLayout.getPaddingRight(), typeLayout.getPaddingBottom());
        }

        mTypeListView = (XRecyclerView) view.findViewById(R.id.type_list);
        mTypeListView.setHasFixedSize(true);
        final GridLayoutManager layoutManger = new GridLayoutManager(getContext(), 4);
        layoutManger.setOrientation(LinearLayoutManager.VERTICAL);
        mTypeListView.setLayoutManager(layoutManger);
        mTypeListView.setPullRefreshEnabled(false);
        mTypeListView.setLoadingMoreEnabled(false);
        mTypeList = new ArrayList<>();
        TypeDao typeDao = new TypeDao();
        //初始化分类列表
        typeDao.initTypes(new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {

                if((Boolean)(result.get("error"))){
                    //error
                    Toast.makeText(getContext(), (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                }else{
                    mTypeList = (List<Type>) result.get("typeList");
                    //默认显示系统分类
                    if(mTypeList == null || mTypeList.isEmpty()){
                        initTypeList();
                    }
                    Log.e("type", mTypeList.toString());
                    mTypeAdapter = new TypeAdapter(mTypeList, getContext());
                    mTypeListView.setAdapter(mTypeAdapter);
                }
            }
        });

        return view;
    }

    public void initTypeList(){
        int[] typeIcons = {R.mipmap.recommand, R.mipmap.teleplay, R.mipmap.movie, R.mipmap.variety, R.mipmap.network_drama, R.mipmap.talk_show,
        R.mipmap.car, R.mipmap.documentary, R.mipmap.commonweal, R.mipmap.packshower, R.mipmap.manga, R.mipmap.entertainment,R.mipmap.music,
        R.mipmap.children, R.mipmap.comedy,R.mipmap.original, R.mipmap.tourizom, R.mipmap.military, R.mipmap.clip_video, R.mipmap.game,
        R.mipmap.sports, R.mipmap.health, R.mipmap.hot, R.mipmap.cloud_movie};

        String[] typeNames = {"推荐","电视剧","电影","综艺","网剧","脱口秀","汽车","纪录片","公益","拍客","动漫","娱乐","音乐","儿童","喜剧","原创","旅游",
                "军事","片花","游戏","运动","健康","热点","网络电影"};

//        for(int i = 0; i < typeIcons.length; i++){
//            mTypeList.add(new Type((long)(i+1), typeNames[i], typeIcons[i]));
//        }
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
