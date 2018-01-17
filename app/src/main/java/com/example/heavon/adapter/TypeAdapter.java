package com.example.heavon.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.heavon.myapplication.MoreShowActivity;
import com.example.heavon.myapplication.R;
import com.example.heavon.utils.DensityUtil;
import com.example.heavon.vo.Type;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TypeAdapter extends BaseRecyclerAdapter<TypeAdapter.TypeAdapterViewHolder> {
    private Context context;
    private List<Type> list;
    private int largeCardHeight, smallCardHeight;

    public TypeAdapter(List<Type> list, Context context) {
        this.list = list;
        this.context = context;
        largeCardHeight = DensityUtil.dip2px(context, 150);
        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(final TypeAdapterViewHolder holder, int position, boolean isItem) {
        final Type type = list.get(position);
        if(TextUtils.isEmpty(type.getIcon())){
            Picasso.with(context).load(R.mipmap.teleplay).into(holder.iconIv);
        }else{
            Picasso.with(context).load(type.getIcon()).into(holder.iconIv);
        }

        holder.nameTv.setText(type.getName());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**------------------进入到更多分类节目页面-----------------**/
                Intent intent = new Intent(context, MoreShowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", type.getName());
                bundle.putLong("typeid", type.getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
                /**------------------进入到更多分类节目页面-----------------**/
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
    public TypeAdapterViewHolder getViewHolder(View view) {
        return new TypeAdapterViewHolder(view, false);
    }

    public void setData(List<Type> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public TypeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_type_item, parent, false);
        TypeAdapterViewHolder vh = new TypeAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Type type, int position) {
        insert(list, type, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class TypeAdapterViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public ImageView iconIv;
        public TextView nameTv;
        public int position;

        public TypeAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                iconIv = (ImageView) itemView
                        .findViewById(R.id.type_icon);
                nameTv = (TextView) itemView
                        .findViewById(R.id.type_name);
                rootView = itemView
                        .findViewById(R.id.type_item);
            }
        }
    }

    public Type getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }

}