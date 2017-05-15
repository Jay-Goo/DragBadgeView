package jaygoo.dragbadgeview.demo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jaygoo.widget.dbv.DragBadgeViewListener;


/**
 * ================================================
 * 作    者：Horrarndoo
 * 版    本：1.1.0
 * 创建日期：2017/3/20
 * 描    述:
 * ================================================
 */
public class DragBadgeViewAapter extends BaseAdapter {
    private Context mContext;
    //记录已经remove的position
    private HashSet<Integer> mRemoved = new HashSet<Integer>();
    private List<String> list = new ArrayList<String>();

    public DragBadgeViewAapter(Context mContext, List<String> list) {
        super();
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_goo, null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);
        holder.mContent.setText(list.get(position));
        //item固定小红点layout
        //item固定小红点
        final TextView point = holder.mPoint;

        boolean visiable = !mRemoved.contains(position);
        point.setVisibility(visiable ? View.VISIBLE : View.GONE);
        if (visiable) {

            DragBadgeViewListener mGooListener = new DragBadgeViewListener(mContext, point , Color.RED) {
                @Override
                public void onDisappear(PointF mDragCenter) {
                    super.onDisappear(mDragCenter);
                    mRemoved.add(position);
                    notifyDataSetChanged();

                }

                @Override
                public void onReset(boolean isOutOfRange) {
                    super.onReset(isOutOfRange);
                    notifyDataSetChanged();//刷新ListView

                }
            };
            //在point父布局内的触碰事件都进行监听
            point.setOnTouchListener(mGooListener);
        }
        return convertView;
    }

    static class ViewHolder {

        public ImageView mImage;
        public TextView mPoint;
        public TextView mContent;

        public ViewHolder(View convertView) {
            mImage = (ImageView) convertView.findViewById(R.id.iv_head);
            mPoint = (TextView) convertView.findViewById(R.id.point);
            mContent = (TextView) convertView.findViewById(R.id.tv_content);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
