package com.example.heavon.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.heavon.myapplication.R;
import com.example.heavon.vo.Show;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by heavon on 2017/2/13.
 */

public class RollLoopAdapter extends LoopPagerAdapter {
    private int[] imgs = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5
    };
    private List<Show> mShowList;

    public RollLoopAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    public RollLoopAdapter(RollPagerView viewPager, List<Show> showList) {
        super(viewPager);
        if(showList != null && !showList.isEmpty()){
            mShowList = showList;
        }
    }

    @Override
    public View getView(ViewGroup container, int position) {
        String thumb = mShowList.get(position).getThumb();
        if(TextUtils.isEmpty(thumb)){
            thumb = "error";
        }
        ImageView view = new ImageView(container.getContext());
        Picasso.with(view.getContext()).load(thumb).error(R.drawable.none_img).into(view);
//        view.setImageResource(imgs[position]);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getRealCount() {
        return mShowList.size();
    }
}
