package jaygoo.widget.dbv;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import jaygoo.widget.dbv.utils.Utils;


/**
 * ================================================
 * 作    者：JayGoo
 * 版    本：1.0.0
 * 创建日期：2017/5/12
 * 描    述: 拖拽监听
 * ================================================
 */
public class DragBadgeViewListener implements OnTouchListener, DragBadgeView.OnDisappearListener {

    private WindowManager mWm;
    private WindowManager.LayoutParams mParams;
    private DragBadgeView mDragBadgeView;
    private View pointLayout;
    private String number;
    private final Context mContext;

    private Handler mHandler;

    public DragBadgeViewListener(Context mContext, View pointLayout,int color) {
        this.mContext = mContext;
        this.pointLayout = pointLayout;

        mDragBadgeView = new DragBadgeView(mContext,color);
        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.format = PixelFormat.TRANSLUCENT;//使窗口支持透明度
        mHandler = new Handler(mContext.getMainLooper());
    }

    public DragBadgeViewListener(Context mContext, View pointLayout,String number , int color) {
        this.mContext = mContext;
        this.pointLayout = pointLayout;
        this.number = number;

        mDragBadgeView = new DragBadgeView(mContext,color);

        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.format = PixelFormat.TRANSLUCENT;//使窗口支持透明度
        mHandler = new Handler(mContext.getMainLooper());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        // 当按下时，将自定义View添加到WindowManager中
        if (action == MotionEvent.ACTION_DOWN) {
            ViewParent parent = v.getParent();
            // 请求其父级View不拦截Touch事件
            parent.requestDisallowInterceptTouchEvent(true);

            int[] points = new int[2];
            //获取pointLayout在屏幕中的位置（layout的左上角坐标）
            pointLayout.getLocationInWindow(points);
            //获取初始小红点中心坐标
            int x = points[0] + pointLayout.getWidth() / 2;
            int y = points[1] + pointLayout.getHeight() / 2;
            // 初始化当前点击的item的信息，数字及坐标
            mDragBadgeView.setStatusBarHeight(Utils.getStatusBarHeight(v));
            mDragBadgeView.setNumber(number);
            mDragBadgeView.initCenter(x, y);
            //设置当前DragBadgeView消失监听
            mDragBadgeView.setOnDisappearListener(this);
            // 添加当前DragBadgeView到WindowManager
            mWm.addView(mDragBadgeView, mParams);
        }
        // 将所有touch事件转交给DragBadgeView处理
        mDragBadgeView.onTouchEvent(event);
        return true;
    }

    @Override
    public void onDisappear(PointF mDragCenter) {
        if (mWm != null && mDragBadgeView.getParent() != null) {
            mWm.removeView(mDragBadgeView);

            //播放气泡爆炸动画
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.anim_bubble_pop);
            AnimationDrawable mAnimDrawable = (AnimationDrawable) imageView
                    .getDrawable();

            final BubbleLayout bubbleLayout = new BubbleLayout(mContext);
            bubbleLayout.setCenter((int) mDragCenter.x, (int) mDragCenter.y - Utils.getStatusBarHeight(mDragBadgeView));

            bubbleLayout.addView(imageView, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT));

            mWm.addView(bubbleLayout, mParams);

            mAnimDrawable.start();

            // 播放结束后，删除该bubbleLayout
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWm.removeView(bubbleLayout);
                }
            }, 501);
        }

    }

    @Override
    public void onReset(boolean isOutOfRange) {
        // 当dragPoint弹回时，去除该View，等下次ACTION_DOWN的时候再添加
        if (mWm != null && mDragBadgeView.getParent() != null) {
            mWm.removeView(mDragBadgeView);
        }
    }
}
