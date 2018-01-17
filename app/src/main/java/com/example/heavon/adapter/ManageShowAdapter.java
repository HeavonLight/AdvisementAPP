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
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.heavon.myapplication.R;
import com.example.heavon.myapplication.ShowActivity;
import com.example.heavon.utils.DensityUtil;
import com.example.heavon.views.ManageShowContentView;
import com.example.heavon.vo.Show;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by heavon on 2017/4/1.
 */

public class ManageShowAdapter extends BaseRecyclerAdapter<ManageShowAdapter.ManageShowAdapterViewHolder> implements ManageShowContentView.OnManageShowDeleteListener{

    private Context context;
    private OnSlidingViewClickListener onSlidingViewClickListener;
    private ManageShowContentView mMenu = null;
    private List<Show> list;
    private int largeCardHeight, smallCardHeight;
    private int imgWidth, imgHeight;

    public ManageShowAdapter(List<Show> list, Context context) {
        this.list = list;
        this.context = context;
        this.onSlidingViewClickListener = (OnSlidingViewClickListener) context;
        largeCardHeight = DensityUtil.dip2px(context, 150);
        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(final ManageShowAdapter.ManageShowAdapterViewHolder holder, int position, boolean isItem) {
        final Show show = list.get(position);
//        String thumb = "http://s16.sinaimg.cn/large/003gRgrCzy73OGZAV434f&690";
        String thumb = "error";
        if (show != null && show.getThumb() != null && !show.getThumb().isEmpty()) {
            thumb = show.getThumb();
        } else {
            Log.e("ManageShowAdapter", "Show thumb error.");
        }
        Log.e("initShow", show.toString());

        Picasso.with(context).load(thumb).error(R.drawable.none_img).into(holder.thumbIv);
        holder.thumbIv.setTag(show.getId());
        holder.thumbIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getLayoutPosition();
                    onSlidingViewClickListener.onItemClick(view, n);
                }
                /**----------跳转到节目详情页-----------**/
                Intent intent = new Intent(context, ShowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("sid", (int)holder.thumbIv.getTag());
                intent.putExtras(bundle);
                context.startActivity(intent);
                /**----------跳转到节目详情页-----------**/
            }
        });
        holder.investmentTv.setText(show.getInvestment_status());
        holder.nameTv.setText(show.getName());
        holder.distributionPlatformTv.setText(context.getString(R.string.title_distribution_platform) + show.getDistribution_platform());
        holder.companyTv.setText(show.getCompany());
        holder.favoriteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**----------wait to modify-----------**/
                Toast.makeText(context, "favorite", Toast.LENGTH_SHORT).show();
                /**----------wait to modify-----------**/
            }
        });
        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition();
                onSlidingViewClickListener.onDeleteBtnCilck(view, n);
                /**----------wait to modify-----------**/
                Toast.makeText(context, "delete show "+show.getId(), Toast.LENGTH_SHORT).show();

                /**----------wait to modify-----------**/
            }
        });
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            holder.rootView.getLayoutParams().height = position % 2 != 0 ? largeCardHeight : smallCardHeight;
        }
        Log.e("root view", String.valueOf(holder.rootView.getLayoutParams().width));
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
    public ManageShowAdapter.ManageShowAdapterViewHolder getViewHolder(View view) {
        return new ManageShowAdapter.ManageShowAdapterViewHolder(view, false);
    }

    public void setData(List<Show> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ManageShowAdapter.ManageShowAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_manage_show_content, parent, false);
        ManageShowAdapter.ManageShowAdapterViewHolder vh = new ManageShowAdapter.ManageShowAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Show show, int position) {
        insert(list, show, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class ManageShowAdapterViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public ImageView thumbIv;
        public ImageView favoriteBt;
        public TextView investmentTv;
        public TextView nameTv;
        public TextView distributionPlatformTv;
        public TextView companyTv;
        public RelativeLayout deleteView;
        public CheckBox checkBox;
        public int position;

        public ManageShowAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                thumbIv = (ImageView) itemView.findViewById(R.id.thumb);
                favoriteBt = (ImageView) itemView.findViewById(R.id.bt_favorite);
                investmentTv = (TextView) itemView.findViewById(R.id.investment_status);
                nameTv = (TextView) itemView.findViewById(R.id.name);
                distributionPlatformTv = (TextView) itemView.findViewById(R.id.tv_platform);
                companyTv = (TextView) itemView.findViewById(R.id.company);
                deleteView = (RelativeLayout) itemView.findViewById(R.id.button_delete);
                checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
                rootView = itemView.findViewById(R.id.show_box);
                ((ManageShowContentView)rootView).setSlidingDeleteButtonListener(ManageShowAdapter.this);
            }
        }
    }

    public Show getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (ManageShowContentView) view;
    }

    /**
     * 滑动或者点击了Item监听
     * @param manageShowContentView
     */
    @Override
    public void onDownOrMove(ManageShowContentView manageShowContentView) {
        if(menuIsOpen()){
            if(mMenu != manageShowContentView){
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
//        mMenu.closeMenu();
        mMenu = null;
    }
    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if(mMenu != null){
            return true;
        }
        return false;
    }

    public interface OnSlidingViewClickListener {
        void onItemClick(View view,int position);
        void onDeleteBtnCilck(View view,int position);
    }
}
