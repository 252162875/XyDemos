package kx.rnd.com.permissionstest.view.decoration;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

/**
 * View悬浮
 * 利用分割线实现悬浮
 */
public class FloatDecoration extends RecyclerView.ItemDecoration {
    private GroupListener mGroupListener;
    private int mGroupHeight = 200;  //悬浮栏高度
    private boolean isAlignLeft = true; //是否靠左边，true 靠左边（默认）、false 靠右边

    public FloatDecoration(GroupListener listener) {
        this.mGroupListener = listener;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        try {
            String groupId = getGroupName(pos);
            if (groupId == null) return;
            //只有是同一组的第一个才显示悬浮栏
            if (pos == 0 || isFirstInGroup(pos)) {
                outRect.top = mGroupHeight;
            } else {
                outRect.top = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();//数据量
        int childCount = parent.getChildCount();//子view数量
        int left = parent.getPaddingLeft();//悬浮栏左边位置
        int right = parent.getWidth() - parent.getPaddingRight();//悬浮栏右边位置
        String preGroupName;
        String currentGroupName = null;
        for (int i = 0; i < childCount; i++) {
            try {
                View view = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(view);
                preGroupName = currentGroupName;
                currentGroupName = getGroupName(position);
                if (currentGroupName == null || TextUtils.equals(currentGroupName, preGroupName))
                    continue;
                int viewBottom = view.getBottom();
                int top = Math.max(mGroupHeight, view.getTop());//top 决定当前顶部第一个悬浮Group的位置
                LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
                int firstVisible = layoutManager.findFirstVisibleItemPosition();

                if (position + 1 < itemCount) {
                    //获取下个GroupName
                    String nextGroupName = getGroupName(position + 1);
                    /*//下一组的第一个View接近头部
                    if (!currentGroupName.equals(nextGroupName) && viewBottom < top) {
//                        System.out.println("viewBottom==:" + viewBottom);
//                        System.out.println("top==:" + top);
                        top = viewBottom;
                    }*/
                    int lastIndexInGroupNow = mGroupListener.getLastIndexInGroup(position) - firstVisible;
                    if (lastIndexInGroupNow < childCount) {
                        View lastView = parent.getChildAt(lastIndexInGroupNow);
                        int mLastViewBottom = lastView.getBottom();
//                        Log.d("数据测试", "最下层的View底部   :" + mLastViewBottom);
//                        Log.d("数据测试", "数据比较" +"  A :"  +(mLastViewBottom + height)  +"  B   :" + top);
                        if (mLastViewBottom < top) {
                            top = mLastViewBottom;
                        }
                    }
                }

                //根据position获取View
                View groupView = getGroupView(position);
                if (groupView == null) throw new Exception("groupView cannot be null");
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mGroupHeight);
                groupView.setLayoutParams(layoutParams);
                groupView.setDrawingCacheEnabled(true);
                groupView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                //指定高度、宽度的groupView
                groupView.layout(0, 0, right, mGroupHeight);
                Bitmap bitmap = groupView.getDrawingCache();
                int marginLeft = isAlignLeft ? 0 : right - groupView.getMeasuredWidth();
                c.drawBitmap(bitmap, left + marginLeft, top - mGroupHeight, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是不是组中的第一个位置
     * 根据前一个组名，判断当前是否为新的组
     */
    private boolean isFirstInGroup(int pos) throws Exception {
        if (pos == 0) {
            return true;
        } else {
            String prevGroupId = getGroupName(pos - 1);
            String groupId = getGroupName(pos);
            return !TextUtils.equals(prevGroupId, groupId);
        }
    }

    /**
     * 获取组名
     *
     * @param position position
     * @return 组名
     */
    private String getGroupName(int position) throws Exception {
        if (mGroupListener != null) {
            return mGroupListener.getGroupName(position);
        } else {
            throw new Exception("GroupListener mGroupListener cannot null ,pls 'setmGroupListener'");
        }
    }

    /**
     * 获取组View
     *
     * @param position position
     * @return 组名
     */
    private View getGroupView(int position) {
        if (mGroupListener != null) {
            return mGroupListener.getGroupView(position);
        } else {
            return null;
        }
    }

    public static class Builder {
        FloatDecoration mDecoration;

        public Builder(GroupListener listener) {
            mDecoration = new FloatDecoration(listener);
        }

        public static Builder init(GroupListener listener) {
            return new Builder(listener);
        }

        /**
         * 设置Group高度
         *
         * @param groutHeight 高度
         * @return this
         */
        public Builder setGroupHeight(int groutHeight) {
            mDecoration.mGroupHeight = groutHeight;
            return this;
        }

        /**
         * 是否靠左边
         * true 靠左边（默认）、false 靠右边
         *
         * @param b b
         * @return this
         */
        public Builder isAlignLeft(boolean b) {
            mDecoration.isAlignLeft = b;
            return this;
        }

        public FloatDecoration build() {
            return mDecoration;
        }
    }
}