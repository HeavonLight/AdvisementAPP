package com.example.heavon.views;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heavon.myapplication.R;
import com.example.heavon.myapplication.ShowActivity;
import com.example.heavon.utils.DensityUtil;
import com.example.heavon.vo.Show;
import com.squareup.picasso.Picasso;

/**
 * Created by heavon on 2017/2/15.
 */

public class TypeShowContentView extends LinearLayout {

    private ImageView mThumbView;
    private ImageView mFavoriteButton;
    private TextView mInvestmentStatusView;
    private TextView mNameView;
    private TextView mCastView;

    private int imgWidth;
    private int imgHeight;

    public TypeShowContentView(Context context) {
        super(context);
        initialize(context);
    }

    public TypeShowContentView(Context context, AttributeSet attr) {
        super(context, attr);
        initialize(context);
    }

    public TypeShowContentView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        initialize(context);
    }

    private void initialize(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_type_show_content, this);

        mThumbView = (ImageView) findViewById(R.id.thumb);
        if(mThumbView== null) {
            Log.e("TypeShowContentView", "thumb view is null.");
            return;
        }
        mFavoriteButton = (ImageView) findViewById(R.id.bt_favorite);

        mInvestmentStatusView = (TextView) findViewById(R.id.investment_status);
        mNameView = (TextView) findViewById(R.id.name);
        mCastView = (TextView) findViewById(R.id.cast);
    }

    public void initShow(Show show){
        String thumb;
        if(show == null){
            Log.e("TypeShowContentView", "Show Info is null.");
            return;
        } else if (show.getThumb() == null || show.getThumb().isEmpty()){
            Log.e("TypeShowContentView", "Show thumb is null.");
//            thumb = "http://s16.sinaimg.cn/large/003gRgrCzy73OGZAV434f&690";
            thumb = "error";
        } else {
            thumb = show.getThumb();
        }

        Log.e("initShow", thumb);
        imgWidth = DensityUtil.dip2px(getContext(), 200);
        imgHeight = DensityUtil.dip2px(getContext(), 124);
        Picasso.with(getContext()).load(thumb).centerCrop().resize(imgWidth,imgHeight).error(R.drawable.none_img).into(mThumbView);
        mThumbView.setTag(show.getId());
        mThumbView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /**----------跳转到节目详情页-----------**/
                Intent intent = new Intent(getContext(), ShowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("sid", (int)view.getTag());
                intent.putExtras(bundle);
                getContext().startActivity(intent);
//                Toast.makeText(getContext(), "click show "+view.getTag(), Toast.LENGTH_SHORT).show();
                /**----------跳转到节目详情页-----------**/
            }
        });

        mFavoriteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /**----------wait to modify-----------**/
                Toast.makeText(getContext(), "favorite", Toast.LENGTH_SHORT).show();
                /**----------wait to modify-----------**/
            }
        });

        String investment = show.getInvestment_status();
        if(TextUtils.equals(investment, "未招商")){
            mInvestmentStatusView.setBackgroundResource(R.color.colorInvestmentNot);
        }else if(TextUtils.equals(investment, "招商结束")){
            mInvestmentStatusView.setBackgroundResource(R.color.colorInvestmentEnd);
        }else if(TextUtils.equals(investment, "招商中")){
            mInvestmentStatusView.setBackgroundResource(R.color.colorInvestmentIng);
        }else if(TextUtils.equals(investment, "执行中")){
            mInvestmentStatusView.setBackgroundResource(R.color.colorInvestmentDoing);
        }
        mInvestmentStatusView.setText(show.getInvestment_status());

        mNameView.setText(show.getName());
        mCastView.setText(show.getCast());
    }

}
