package com.example.heavon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.heavon.myapplication.R;
import com.example.heavon.utils.DensityUtil;
import com.example.heavon.vo.Search;

import java.util.List;

public class HotAdapter extends BaseRecyclerAdapter<HotAdapter.HotAdapterViewHolder> {
    private Context context;
    private List<Search> list;
    private int largeCardHeight, smallCardHeight;

    public HotAdapter(List<Search> list, Context context) {
        this.list = list;
        this.context = context;
        largeCardHeight = DensityUtil.dip2px(context, 150);
        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(final HotAdapterViewHolder holder, int position, boolean isItem) {
        final Search search = list.get(position);

        holder.orderTv.setText(String.valueOf(position+1));
        holder.hotTv.setText(search.getContent());
        holder.hotTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加热搜榜关键词到搜索框


            }
        });
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            holder.rootView.getLayoutParams().height = position % 2 != 0 ? largeCardHeight : smallCardHeight;
        }
    }

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public HotAdapterViewHolder getViewHolder(View view) {
        return new HotAdapterViewHolder(view, false);
    }

    public void setData(List<Search> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public HotAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_search_hot, parent, false);
        HotAdapterViewHolder vh = new HotAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Search search, int position) {
        insert(list, search, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class HotAdapterViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView orderTv;
        public TextView hotTv;
        public int position;

        public HotAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                orderTv = (TextView) itemView
                        .findViewById(R.id.order);
                hotTv = (TextView) itemView
                        .findViewById(R.id.hot);
                rootView = itemView
                        .findViewById(R.id.hot_box);
            }
        }
    }

    public Search getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }

}