package com.example.heavon.myapplication;

//import android.app.Fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.example.heavon.fragment.MainFragment;
import com.example.heavon.fragment.PersonFragment;
import com.example.heavon.fragment.SearchFragment;
import com.example.heavon.fragment.TypeFragment;
import com.example.heavon.fragment.TypeShowFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends BasicActivity implements
        MainFragment.OnFragmentInteractionListener,
        PersonFragment.OnFragmentInteractionListener,
        TypeFragment.OnFragmentInteractionListener,
        TypeShowFragment.OnFragmentInteractionListener,
        View.OnClickListener {

    private RadioButton mIndicatorMain;
    private RadioButton mIndicatorType;
    private RadioButton mIndicatorPerson;
    private List<RadioButton> mIndicatorList;

    private List<Fragment> mFragmentList;
    private android.support.v4.app.FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mCurFragment;

    private int mCurPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();   //初始化UI
    }

    //初始化UI
    public void initUI() {
        initIndicator();
        initFragment();
    }

    //初始化Fragment
    public void initFragment() {
        mFragmentManager = getSupportFragmentManager();

        MainFragment mainFragment = new MainFragment();
        TypeFragment typeFragment = new TypeFragment();
        PersonFragment personFragment = new PersonFragment();

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(mainFragment);
        mFragmentList.add(typeFragment);
        mFragmentList.add(personFragment);

        changeIndicator(0);
    }

    //初始化底部导航
    public void initIndicator() {
        mIndicatorMain = (RadioButton) findViewById(R.id.indicator_main);
        mIndicatorType = (RadioButton) findViewById(R.id.indicator_type);
        mIndicatorPerson = (RadioButton) findViewById(R.id.indicator_person);

        mIndicatorMain.setOnClickListener(this);
        mIndicatorType.setOnClickListener(this);
        mIndicatorPerson.setOnClickListener(this);

        mIndicatorMain.setTag(0);
        mIndicatorType.setTag(1);
        mIndicatorPerson.setTag(2);

        mIndicatorList = new ArrayList<RadioButton>();
        mIndicatorList.add(mIndicatorMain);
        mIndicatorList.add(mIndicatorType);
        mIndicatorList.add(mIndicatorPerson);
    }

    //实现Fragment事件监听
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //进入添加节目页
    @Override
    public void addShow() {
        enterAddShow();
    }

    //进入管理节目页
    @Override
    public void manageShow() {
        enterManageShow();
    }

    @Override
    public void feedback() {
        enterFeedback();
    }

    @Override
    public void about() {
        enterAbout();
    }

    @Override
    public void share() {

    }

    @Override
    public void protocol() {
        enterProtocol();
    }

    //进入搜索页
    @Override
    public void search() {
        enterSearch();
    }

    //跳转到登录页
    @Override
    public void login() {
        gotoLogin();
    }

    @Override
    public void onClick(View view) {
        changeIndicator((Integer) view.getTag());
    }

    private void changeIndicator(int index) {
        mCurPos = index;
        //改变 fragment
        mFragmentTransaction = mFragmentManager.beginTransaction();
        //隐藏其他fragment
        if (null != mCurFragment) {
            mFragmentTransaction.hide(mCurFragment);
        }
        //显示当前fragment
        Fragment fragment = mFragmentManager.findFragmentByTag(mFragmentList.get(mCurPos).getClass().getName());
        if (null == fragment) {
            fragment = mFragmentList.get(index);
        }
        mCurFragment = fragment;
        if (!fragment.isAdded()) {
            mFragmentTransaction.add(R.id.fragment_box, fragment, fragment.getClass().getName());
        } else {
            mFragmentTransaction.show(fragment);
        }
        mFragmentTransaction.commit();
        //改变底部导航
        mIndicatorList.get(mCurPos).setChecked(true);
    }
}
