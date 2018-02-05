package yuan.kuo.yu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import yuan.kuo.yu.R;
import yuan.kuo.yu.load.CircleLoadingView;


/**
 * @author yukuoyuan
 */
public class YRecycleviewRefreshFootView extends LinearLayout {

    private CircleLoadingView pb_y_recycleview_foot_loadmore_progressbar;
    private TextView tv_y_recycleview_foot_loadmore_status;
    private TextView tv_y_recycleview_foot_loadmore_nodata;
    private LinearLayout ll_recycleview_foot_loadmore_no_more_data;
    /**
     * 加载中
     */
    public final static int STATE_LOADING = 0;
    /**
     * 加载完成
     */
    public final static int STATE_COMPLETE = 1;
    /**
     * 没有更多的数据
     */
    public final static int STATE_NOMORE = 2;
    /**
     * 没有数据
     */
    public final static int STATE_NODATA = 3;

    public YRecycleviewRefreshFootView(Context context) {
        super(context);
        initView(context);
    }

    public YRecycleviewRefreshFootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化
     */
    private void initView(Context context) {
        //设置内部内容居中
        setGravity(Gravity.CENTER);
        //设置宽高
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //底部布局
        View mContentView = View.inflate(context, R.layout.foot_recycleview_loadmore, null);
        pb_y_recycleview_foot_loadmore_progressbar = (CircleLoadingView) mContentView.findViewById(R.id.pb_y_recycleview_foot_loadmore_progressbar);
        tv_y_recycleview_foot_loadmore_status = (TextView) mContentView.findViewById(R.id.tv_y_recycleview_foot_loadmore_status);
        tv_y_recycleview_foot_loadmore_nodata = (TextView) mContentView.findViewById(R.id.tv_y_recycleview_foot_loadmore_nodata);
        ll_recycleview_foot_loadmore_no_more_data = (LinearLayout) mContentView.findViewById(R.id.ll_recycleview_foot_loadmore_no_more_data);
        addView(mContentView);
    }

    /**
     * 设置当前状态
     *
     * @param state 需要展示的状态
     */
    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                pb_y_recycleview_foot_loadmore_progressbar.setVisibility(View.VISIBLE);
                tv_y_recycleview_foot_loadmore_status.setText("加载中...");
                ll_recycleview_foot_loadmore_no_more_data.setVisibility(VISIBLE);
                tv_y_recycleview_foot_loadmore_nodata.setVisibility(GONE);

                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                tv_y_recycleview_foot_loadmore_status.setText("加载中...");
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                tv_y_recycleview_foot_loadmore_status.setText("没有更多了..");
                pb_y_recycleview_foot_loadmore_progressbar.setVisibility(View.GONE);
                ll_recycleview_foot_loadmore_no_more_data.setVisibility(VISIBLE);
                tv_y_recycleview_foot_loadmore_nodata.setVisibility(GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_NODATA:
                tv_y_recycleview_foot_loadmore_nodata.setVisibility(VISIBLE);
                ll_recycleview_foot_loadmore_no_more_data.setVisibility(GONE);
                this.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 设置空布局的提示内容
     *
     * @param emptyText 空布局展示的提示语
     */
    public void setEmptyTips(String emptyText) {
        tv_y_recycleview_foot_loadmore_nodata.setText(emptyText);
    }
}
