package com.example.heavon.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heavon.constant.Constant;
import com.example.heavon.dao.UserDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.myapplication.App;
import com.example.heavon.myapplication.R;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //UI preference.
    private RelativeLayout mLogoutView;
    private RelativeLayout mLoginView;
    private RelativeLayout mShowManagerView;
    private RelativeLayout mShareView;
    private RelativeLayout mFeedbackView;
    private RelativeLayout mProtocolView;
    private RelativeLayout mAboutView;
    private Button mAddShowButton;
    private TextView mAuthorityView;

    private UserDao mUserDao;


    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
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
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        mUserDao = new UserDao();

        mLogoutView = (RelativeLayout) view.findViewById(R.id.person_logout);
        mLogoutView.setOnClickListener(this);

        mLoginView = (RelativeLayout) view.findViewById(R.id.person_login);
        mLoginView.setOnClickListener(this);

        mShowManagerView = (RelativeLayout) view.findViewById(R.id.show_manager);
        mShowManagerView.setOnClickListener(this);

        mShareView = (RelativeLayout) view.findViewById(R.id.share);
        mShareView.setOnClickListener(this);

        mFeedbackView = (RelativeLayout) view.findViewById(R.id.feedback);
        mFeedbackView.setOnClickListener(this);

        mProtocolView = (RelativeLayout) view.findViewById(R.id.protocol);
        mProtocolView.setOnClickListener(this);

        mAboutView = (RelativeLayout) view.findViewById(R.id.person_about);
        mAboutView.setOnClickListener(this);

        mAddShowButton = (Button) view.findViewById(R.id.add_show_button);
        mAddShowButton.setOnClickListener(this);

        mAuthorityView = (TextView) view.findViewById(R.id.authority);

        Log.e("personFragment",mUserDao.checkLogin(getContext())+"");
        if (mUserDao.checkLogin(getContext())) {
            mLoginView.setVisibility(View.GONE);
            mLogoutView.setVisibility(View.VISIBLE);
            TextView nameTv = (TextView) view.findViewById(R.id.name);
            nameTv.setText(mUserDao.getUserName(getContext()));
        } else {
            mLoginView.setVisibility(View.VISIBLE);
            mLogoutView.setVisibility(View.GONE);
        }

        Log.e("personFragment",mUserDao.getLevel(getContext())+"");
        int level = mUserDao.getLevel(getContext());
        if (level == Constant.LEVEL_NORMAL) {
            mAuthorityView.setText("普通用户");
            mAuthorityView.setVisibility(View.VISIBLE); //显示普通用户
        } else if (level == Constant.LEVEL_MEDIA) {
            mAuthorityView.setText("媒体用户");
            mAuthorityView.setVisibility(View.VISIBLE); //显示媒体用户
        } else if (level >= Constant.LEVEL_MANAGER) {
            mShowManagerView.setVisibility(View.VISIBLE);   //显示管理节目按钮
            mAddShowButton.setVisibility(View.VISIBLE); //显示添加节目按钮
            mAuthorityView.setText("管理员");
            mAuthorityView.setVisibility(View.VISIBLE); //显示管理员
        } else {
            mShowManagerView.setVisibility(View.GONE);
            mAddShowButton.setVisibility(View.GONE);
        }
        return view;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_show_button:{
                //跳转到添加节目
                mListener.addShow();
            }break;
            case R.id.person_login:{
                mListener.login();
            }break;
            case R.id.person_logout:{
                mUserDao.logout(new HttpResponse<Map<String, Object>>() {
                    @Override
                    public void getHttpResponse(Map<String, Object> result) {
                        String msg = (String) result.get("msg");
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        if (mListener != null) {
                            //清空本地登录
                            SharedPreferences sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            sp.edit().clear().commit();
                            //清空session
                            App.newInstance().clearSessionCookie();
                            mListener.login();
                        }
                    }
                });
            }break;
            case R.id.show_manager:{
                mListener.manageShow();
            }break;
            case R.id.share:{
//                mListener.share();
            }break;
            case R.id.feedback:{
//                mListener.feedback();
            }break;
            case R.id.protocol:{
//                mListener.protocol();
            }break;
            case R.id.person_about:{
//                mListener.about();
            }break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
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
        void login();
        void addShow();
        void manageShow();
        void feedback();
        void about();
        void share();
        void protocol();
    }
}
