package com.example.heavon.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.heavon.myapplication.R;
import com.example.heavon.myapplication.ShowActivity;
import com.example.heavon.utils.DensityUtil;
import com.example.heavon.vo.Search;
import com.example.heavon.vo.Show;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends BaseRecyclerAdapter<HistoryAdapter.HistoryAdapterViewHolder> {
    private Context context;
    private List<Search> list;
    private int largeCardHeight, smallCardHeight;

    public HistoryAdapter(List<Search> list, Context context) {
        this.list = list;
        this.context = context;
        largeCardHeight = DensityUtil.dip2px(context, 150);
        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(final HistoryAdapterViewHolder holder, int position, boolean isItem) {
        final Search search = list.get(position);

        holder.historyTv.setText(search.getContent());
        holder.historyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加搜索历史到搜索框


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
    public HistoryAdapterViewHolder getViewHolder(View view) {
        return new HistoryAdapterViewHolder(view, false);
    }

    public void setData(List<Search> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public HistoryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_search_history, parent, false);
        HistoryAdapterViewHolder vh = new HistoryAdapterViewHolder(v, true);
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

    public class HistoryAdapterViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public ImageView clockIv;
        public TextView historyTv;
        public int position;

        public HistoryAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                clockIv = (ImageView) itemView
                        .findViewById(R.id.clock);
                historyTv = (TextView) itemView
                        .findViewById(R.id.history);
                rootView = itemView
                        .findViewById(R.id.history_box);
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