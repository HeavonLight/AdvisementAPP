package com.example.heavon.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import com.example.heavon.myapplication.R;

/**
 * Created by heavon on 2017/4/6.
 */

public class ManageShowContentView extends HorizontalScrollView {

    private RelativeLayout mDeleteView;
    private int mScrollWidth;   //用于记录滚动条可以滚动的距离
    private OnManageShowDeleteListener onManageShowDeleteListener; //自定义的接口，用于传达滑动事件等
    private Boolean isOpen = false; //记录按钮菜单是否打开，默认关闭false
    private Boolean once = false;   //在onMeasure中只执行一次的判断

    public ManageShowContentView(Context context) {
        super(context);
    }

    public ManageShowContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ManageShowContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(!once){
            mDeleteView = (RelativeLayout) findViewById(R.id.button_delete);
            once = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            this.scrollTo(0,0);
            //获取水平滚动条可以滑动的范围，即右侧按钮的宽度
            mScrollWidth = mDeleteView.getWidth();
        }
    }

    public void setSlidingDeleteButtonListener(OnManageShowDeleteListener listener){
        onManageShowDeleteListener = listener;
    }

    public interface OnManageShowDeleteListener{
        void onMenuIsOpen(View view);
        void onDownOrMove(ManageShowContentView manageShowContentView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                onManageShowDeleteListener.onDownOrMove(this);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                changeScrollx();
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 按滚动条被拖动距离判断关闭或打开菜单
     */
    public void changeScrollx(){
        if(getScrollX() >= (mScrollWidth/2)){
            this.smoothScrollTo(mScrollWidth, 0);
            isOpen = true;
            onManageShowDeleteListener.onMenuIsOpen(this);
        }else{
            this.smoothScrollTo(0, 0);
            isOpen = false;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mDeleteView.setTranslationX(l - mScrollWidth);
    }

}
