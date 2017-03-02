package com.example.heavon.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.heavon.dao.SearchDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.myapplication.R;
import com.example.heavon.vo.Show;
import com.example.heavon.vo.ShowFilter;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //UI preference
    private EditText mSearchEdit;
    private ImageView mSearchButton;
    private ImageView mSearchCancel;
    private Button mSearchCancelLink;

    private OnFragmentInteractionListener mListener;
//    private RequestQueue mQueue;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        mQueue = Volley.newRequestQueue(getContext());

        mSearchEdit = (EditText) view.findViewById(R.id.search);
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (TextUtils.isEmpty(text)) {
                    //文本框为空
                    mSearchCancel.setVisibility(View.INVISIBLE);
                    mListener.onTextChange(true);

                } else {
                    //文本框文本改动
                    mSearchCancel.setVisibility(View.VISIBLE);
                    mListener.onTextChange(false);
                }
            }
        });
//        mSearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(mListener != null){
//                    if(b){
//                        mListener.onFragmentInteraction(null);
//                        mSearchEdit.clearFocus();
//                    }
//                }else{
//                    Log.d("search_activity","listener null");
//                }
//            }
//        });

        mSearchButton = (ImageView) view.findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(this);
        mSearchCancel = (ImageView) view.findViewById(R.id.search_eidt_cancel);
        mSearchCancel.setOnClickListener(this);
        mSearchCancelLink = (Button) view.findViewById(R.id.link_search_cancel);
        mSearchCancelLink.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    public void initUI() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_button: {
                //搜索
                search();
            }
            break;
            case R.id.search_eidt_cancel: {
                //取消输入
                mSearchEdit.setText("");
            }
            break;
            case R.id.link_search_cancel: {
                //取消搜索
                mListener.onCancelSearch();
            }
            break;
            default:
                break;
        }
    }

    /**
     * 搜索
     */
    protected void search() {
        String keyword = mSearchEdit.getText().toString().trim();
        //检查参数
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(getContext(), "请输入搜索内容", Toast.LENGTH_SHORT).show();
            return;
        }
        //搜索
        mListener.onShowSearch(keyword);
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

        void onShowSearch(String keyword);

        void onCancelSearch();

        void onTextChange(boolean isEmpty);
    }
}
