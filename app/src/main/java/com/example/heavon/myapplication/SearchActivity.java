package com.example.heavon.myapplication;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.heavon.dao.SearchDao;
import com.example.heavon.dao.ShowDao;
import com.example.heavon.fragment.MainFragment;
import com.example.heavon.fragment.PersonFragment;
import com.example.heavon.fragment.SearchAfterFragment;
import com.example.heavon.fragment.SearchBeforeFragment;
import com.example.heavon.fragment.SearchFragment;
import com.example.heavon.fragment.SearchOnFragment;
import com.example.heavon.fragment.TypeFragment;
import com.example.heavon.vo.Show;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BasicActivity implements
        SearchFragment.OnFragmentInteractionListener,
        SearchBeforeFragment.OnFragmentInteractionListener,
        SearchOnFragment.OnFragmentInteractionListener,
        SearchAfterFragment.OnFragmentInteractionListener{


    // UI preference.
    private RecyclerView mSearchHistoryView;
    private RecyclerView mSearchHotView;

    // fragment.
    private List<Fragment> mFragmentList;
    private android.support.v4.app.FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mCurFragment;

    private int mCurPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initUI();
    }

    public void initUI(){

        initFragment();
    }

    public void initFragment(){
        mFragmentManager = getSupportFragmentManager();

        SearchBeforeFragment beforeFragment = new SearchBeforeFragment();
        SearchOnFragment onFragment = new SearchOnFragment();
        SearchAfterFragment afterFragment = new SearchAfterFragment();

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(beforeFragment);
        mFragmentList.add(onFragment);
        mFragmentList.add(afterFragment);

        changeFragment(0);
    }

    private void changeFragment(int index){
        mCurPos = index;
        mFragmentTransaction = mFragmentManager.beginTransaction();

        if(null != mCurFragment){
            if(mCurFragment instanceof SearchAfterFragment){
                mFragmentTransaction.remove(mCurFragment);
            }else {
                mFragmentTransaction.hide(mCurFragment);
            }
        }

        Fragment fragment = mFragmentManager.findFragmentByTag(mFragmentList.get(mCurPos).getClass().getName());
        if(null == fragment){
            fragment = mFragmentList.get(index);
        }
        mCurFragment = fragment;

        if(!fragment.isAdded()){
            mFragmentTransaction.add(R.id.fragment_box, fragment, fragment.getClass().getName());
        }else{
            mFragmentTransaction.show(fragment);
        }

        mFragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onShowSearch(String keyword) {
        Log.e("searchActivity", "search keyword "+keyword);
        SearchAfterFragment fragment = SearchAfterFragment.newInstance(keyword, 1);
        mFragmentList.set(2, fragment);
        changeFragment(2);
    }

    @Override
    public void onCancelSearch() {
        this.finish();
    }

    @Override
    public void onTextChange(boolean isEmpty) {
        if(isEmpty){
            changeFragment(0);
        }else{
            changeFragment(1);
        }
    }
}
