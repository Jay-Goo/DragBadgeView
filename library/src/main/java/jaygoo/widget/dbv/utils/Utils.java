package jaygoo.widget.dbv.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

/**
 * ================================================
 * 作    者：JayGoo
 * 版    本：1.0.0
 * 创建日期：2017/5/12
 * 描    述: 屏幕尺寸工具
 * ================================================
 */
public class Utils {


    /**
     * dip 转换成 px
     * @param dip
     * @param context
     * @return
     */
    public static float dip2Dimension(float dip, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
    }


    /**
     * 最全面的获取状态栏高度的方法
     * @param view
     * @return
     */
    public static int getStatusBarHeight(View view) {
        int statusBarHeight = 0;
        if (view == null)return 0;

        //尝试借助应用区域的top属性获取状态栏高度
        //此方式只有onWindowFocusChanged()回调后方会生效，onCreate中调用无效
        if (0 == statusBarHeight) {
            Rect rectangle = new Rect();
            view.getRootView().getWindowVisibleDisplayFrame(rectangle);
            statusBarHeight = rectangle.top;
        }

        statusBarHeight = getStatusBarHeight(view.getContext());
        return statusBarHeight;
    }

    /**
     * 通过系统资源和反射的方式获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;

        //尝试通过系统尺寸资源获取状态栏高度
        try {
            //获取status_bar_height资源的ID
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //尝试借助反射R类实例域方式获取状态栏高度
        if (0 == statusBarHeight){
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                int height = Integer.parseInt(clazz.getField("status_bar_height")
                        .get(object).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return statusBarHeight;
    }
}
