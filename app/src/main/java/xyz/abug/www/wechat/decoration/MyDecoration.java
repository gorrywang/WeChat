package xyz.abug.www.wechat.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

/**
 * Created by iswgr on 2017/7/3.
 * RecyclerView分割线
 */

public class MyDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private Drawable mDivider;
    private int mOrientation;
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public static final int[] ATRRS = new int[]{
            android.R.attr.listDivider
    };

    //构造方法
    public MyDecoration(Context mContext, int mOrientation) {
        this.mContext = mContext;
        final TypedArray typedArray = mContext.obtainStyledAttributes(ATRRS);
        this.mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(mOrientation);
    }

    //设置屏幕方向
    private void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            Toast.makeText(mContext, "发生错误", Toast.LENGTH_SHORT).show();
        }
        mOrientation = orientation;
    }

    //画布
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL_LIST) {
            //如果屏幕方向是横向
            drawVerticalLine(c, parent, state);
        } else {
            //如果屏幕方向是竖向
            drawHorizontalLine(c, parent, state);
        }
    }

    //画横线
    private void drawHorizontalLine(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childAt.getLayoutParams();
            int top = childAt.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    //画竖线
    private void drawVerticalLine(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(mOrientation == HORIZONTAL_LIST){
            //画横线，就是往下偏移一个分割线的高度
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }else {
            //画竖线，就是往右偏移一个分割线的宽度
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}
