package yuan.kuo.yu.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * GitHub: https://github.com/yukuoyuan
 * Blog  : http://blog.csdn.net/easkshark
 * Date  : 2018/1/16
 * Time  : 09:07
 * Desc  : 这是一个重构版的自动刷新布局
 * Editor: yukuoyuan
 *
 * @author yukuoyuan
 */

public class YRecyclerView extends RecyclerView {

    /**
     * 是否可以刷新的常量(默认可以刷新)
     */
    private boolean pullRefreshEnabled = true;
    /**
     * 是否可以加载更多的常量(默认可以加载更多)
     */
    private boolean loadMoreEnabled = true;
    /**
     * 头布局集合
     */
    private ArrayList<View> mHeaderViews = new ArrayList<>();
    /**
     * 脚布局集合
     */
    private ArrayList<View> mFootViews = new ArrayList<>();
    /**
     * 当前的头布局
     */
    private YRecycleviewRefreshHeadView mHeadView;
    /**
     * 刷新或者加载更多的监听器
     */
    private OnRefreshAndLoadMoreListener refreshAndLoadMoreListener;
    /**
     * 是否在加载中
     */
    private boolean isLoadingData = false;
    /**
     * 是否没有更多的数据了
     */
    private boolean isNoMore = false;
    /**
     * 最后的y坐标(默认为-1)
     */
    private float mLastY = -1;
    /**
     * 下拉的阻率
     */
    private static final float DRAG_RATE = 2;
    /**
     * 适配器
     */
    private WrapAdapter mWrapAdapter;
    /**
     * 刷新头布局的类型
     */
    private static final int TYPE_REFRESH_HEADER = -5;
    /**
     * 正常数据类型
     */
    private static final int TYPE_NORMAL = 0;
    /**
     * 脚布局类型
     */
    private static final int TYPE_FOOTER = -3;
    /**
     * 头布局类型集合
     */
    private static List<Integer> sHeaderTypes = new ArrayList<>();
    /**
     * 数据观察者
     */
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();
    private static final int HEADER_INIT_INDEX = 10000;

    public YRecyclerView(Context context) {
        this(context, null);
    }

    public YRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        /**
         * 初始化
         */
        init(context);
    }


    /**
     * 这是一个初始化的方法
     *
     * @param context
     */
    private void init(Context context) {
        //可以进行刷新
        if (pullRefreshEnabled) {
            //获取头布局
            YRecycleviewRefreshHeadView refreshHeader = new YRecycleviewRefreshHeadView(getContext());
            //添加进去
            mHeaderViews.add(0, refreshHeader);
            mHeadView = refreshHeader;
        }
        YRecycleviewRefreshFootView footView = new YRecycleviewRefreshFootView(getContext());
//        LinearLayout.LayoutParams mFootViewlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        footView.setLayoutParams(mFootViewlayoutParams);
        addFootView(footView);
        mFootViews.get(0).setVisibility(GONE);
    }

    /**
     * 当滚动状态改变的时候调用的方法
     *
     * @param screenState 滚动状态,0是滚动停止的状态,1正在被外部拖拽,一般为用户正在用手指滚动,2自动滚动开始
     */
    @Override
    public void onScrollStateChanged(int screenState) {
        super.onScrollStateChanged(screenState);
        /**
         * 如果滚动状态停止,并且刷新监听不为空,并且不是正在加载数据,并且可以进行刷新
         */
        if (screenState == RecyclerView.SCROLL_STATE_IDLE && refreshAndLoadMoreListener != null && !isLoadingData && loadMoreEnabled) {
            /**
             * 获取布局管理
             */
            LayoutManager layoutManager = getLayoutManager();
            /**
             * 最后一个显示的条目
             */
            int lastVisibleItemPosition;
            /**
             * 如果是网格布局
             */
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                /**
                 * 如果是瀑布流布局
                 */
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                /**
                 * 如果是线性布局
                 */
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            /**
             * 如果说有数据,最后一条显示的数据是最后一个条目,并且自孩子条目大于,并且还有数据,并且脚布局的状态是不在刷新中
             */
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 && layoutManager.getItemCount() > layoutManager.getChildCount()
                    && !isNoMore && mHeadView.getStatus() < YRecycleviewRefreshHeadView.STATE_REFRESHING) {
                /**
                 * 获取脚布局
                 */
                View footView = mFootViews.get(0);
                isLoadingData = true;
                /**
                 * 如果脚布局是刷新脚布局的话(设置刷新中)
                 */
                if (footView instanceof YRecycleviewRefreshFootView) {
                    ((YRecycleviewRefreshFootView) footView).setState(YRecycleviewRefreshFootView.STATE_LOADING);
                } else {
                    /**
                     * 如果是其他布局的话就显示
                     */
                    footView.setVisibility(View.VISIBLE);
                }
                /**
                 * 回调刷新中的监听
                 */
                refreshAndLoadMoreListener.onLoadMore();
            }
        }
    }

    /**
     * 当触摸控件的时候调用的方法
     *
     * @param e 触摸事件
     * @return 是否拦截事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mLastY == -1) {
            mLastY = e.getRawY();
        }
        switch (e.getAction()) {
            /**
             * 当按下的时候
             */
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            /**
             * 当移动的时候
             */
            case MotionEvent.ACTION_MOVE:
                final float deltaY = e.getRawY() - mLastY;
                mLastY = e.getRawY();
                if (isOnTop() && pullRefreshEnabled) {
                    mHeadView.onMove(deltaY / DRAG_RATE);
                    if (mHeadView.getVisibleHeight() > 0 && mHeadView.getStatus() < YRecycleviewRefreshHeadView.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            /**
             * 手指离开
             */
            default:
                mLastY = -1;
                /**
                 * 如果在顶部,并且还可以刷新
                 */
                if (isOnTop() && pullRefreshEnabled) {
                    /**
                     * 如果头布局释放意图,就回调刷新
                     */
                    if (mHeadView.releaseAction()) {
                        if (refreshAndLoadMoreListener != null) {
                            refreshAndLoadMoreListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 设置适配器
     *
     * @param adapter 包装的适配器
     */
    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }


    /**
     * 包装的适配器
     */
    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {
        private RecyclerView.Adapter adapter;
        /**
         * 选择的位置
         */
        private int mCurrentPosition;
        /**
         * 头布局位置
         */
        private int headerPosition = 1;

        public WrapAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        /**
         * 获取条目类型
         *
         * @param position
         * @return
         */
        @Override
        public int getItemViewType(int position) {
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isHeader(position)) {
                position = position - 1;
                return sHeaderTypes.get(position);
            }
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }
            int adjPosition = position - getHeadersCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return TYPE_NORMAL;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                mCurrentPosition++;
                return new SimpleViewHolder(mHeaderViews.get(0));
            } else if (isContentHeader(mCurrentPosition)) {
                if (viewType == sHeaderTypes.get(mCurrentPosition - 1)) {
                    mCurrentPosition++;
                    return new SimpleViewHolder(mHeaderViews.get(headerPosition++));
                }
            } else if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mFootViews.get(0));
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            /**
             * 处理回来的position索引
             */
            if (isHeader(position)) {
                return;
            }
            int adjPosition = position - getHeadersCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }
        }

        /**
         * 获取条目列表数据
         *
         * @return
         */
        @Override
        public int getItemCount() {
            if (adapter != null) {
                return getHeadersCount() + getFootersCount() + adapter.getItemCount();
            } else {
                return getHeadersCount() + getFootersCount();
            }
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount()) {
                int adjPosition = position - getHeadersCount();
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        /**
         * 是否是头布局
         *
         * @param position
         * @return
         */
        public boolean isHeader(int position) {
            return position >= 0 && position < mHeaderViews.size();
        }

        /**
         * 是否刷新头布局
         *
         * @param position
         * @return
         */
        public boolean isRefreshHeader(int position) {
            return position == 0;
        }

        /**
         * 是否是内容头布局
         *
         * @param position
         * @return
         */
        public boolean isContentHeader(int position) {
            return position >= 1 && position < mHeaderViews.size();
        }

        /**
         * 是否是低脚布局
         *
         * @param position
         * @return
         */
        public boolean isFooter(int position) {
            return position < getItemCount() && position >= getItemCount() - mFootViews.size();
        }

        /**
         * 获取头布局条目
         *
         * @return
         */
        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        /**
         * 获取脚布局的条目
         *
         * @return
         */
        public int getFootersCount() {
            return mFootViews.size();
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.registerAdapterDataObserver(observer);
            }
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.unregisterAdapterDataObserver(observer);
            }
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * 这是一个数据观察者
     */
    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }

    }

    /**
     * 是否是顶部
     *
     * @return 是否在顶部
     */
    public boolean isOnTop() {
        /**
         * 如果有头布局.并且头布局的父布局不为空
         */
        return !(mHeaderViews == null || mHeaderViews.isEmpty()) && mHeaderViews.get(0).getParent() != null;
    }
    /**
     * 添加头布局
     *
     * @param headView 刷新头布局
     */
    public void addHeadView(View headView) {
        if (pullRefreshEnabled && !(mHeaderViews.get(0) instanceof YRecycleviewRefreshHeadView)) {
            YRecycleviewRefreshHeadView refreshHeader = new YRecycleviewRefreshHeadView(getContext());
            mHeaderViews.add(0, refreshHeader);
            mHeadView = refreshHeader;
        }
        mHeaderViews.add(headView);
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
    }
    /**
     * 添加底部布局
     *
     * @param footView 布局
     */
    private void addFootView(View footView) {
        mFootViews.add(footView);
    }

    /**
     * 获取最大值
     *
     * @param into 数组数据源
     * @return 返回最大值
     */
    private int findMax(int[] into) {
        int max = into[0];
        for (int value : into) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 设置刷新和加载更多的监听器
     *
     * @param refreshAndLoadMoreListener 监听器
     */
    public void setRefreshAndLoadMoreListener(OnRefreshAndLoadMoreListener refreshAndLoadMoreListener) {
        this.refreshAndLoadMoreListener = refreshAndLoadMoreListener;
    }

    /**
     * 刷新和加载更多的监听器
     */
    public interface OnRefreshAndLoadMoreListener {
        /**
         * 刷新数据的回调
         */
        void onRefresh();

        /**
         * 加载数据的回调
         */
        void onLoadMore();
    }

    /**
     * 设置是否启用下拉刷新功能
     *
     * @param isEnabled 是否可以进行下拉刷新
     */
    public void setRefreshEnabled(boolean isEnabled) {
        pullRefreshEnabled = isEnabled;
    }

    /**
     * 设置刷新完成
     */
    public void setReFreshComplete() {
        mHeadView.refreshComplete();
    }

    /**
     * 设置加载更多完成
     */
    public void setloadMoreComplete() {
        //设置加载数据为false
        isLoadingData = false;
        View footView = mFootViews.get(0);
        if (footView instanceof YRecycleviewRefreshFootView) {
            ((YRecycleviewRefreshFootView) footView).setState(YRecycleviewRefreshFootView.STATE_COMPLETE);
        } else {
            footView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置是否启用上拉加载功能
     *
     * @param isEnabled 是否启用上啦加载更多
     */
    public void setLoadMoreEnabled(boolean isEnabled) {
        loadMoreEnabled = isEnabled;
        //如果不启用加载更多功能,就隐藏脚布局
        if (!isEnabled && mFootViews.size() > 0) {
            mFootViews.get(0).setVisibility(GONE);
        }
    }

    /**
     * 设置没有更多数据了
     *
     * @param noMore 是否没有更多数据
     */
    public void setNoMoreData(boolean noMore) {
        this.isNoMore = noMore;
        View footView = mFootViews.get(0);
        ((YRecycleviewRefreshFootView) footView).setState(isNoMore ? YRecycleviewRefreshFootView.STATE_NOMORE : YRecycleviewRefreshFootView.STATE_COMPLETE);
    }

    /**
     * 设置是否没有数据(列表数据为空)
     *
     * @param isNo         是否没有数据
     * @param emptyContext 空数据展示的提示
     */
    public void setIsNoData(boolean isNo, String emptyContext) {
        this.isNoMore = isNo;
        View footView = mFootViews.get(0);
        ((YRecycleviewRefreshFootView) footView).setState(isNoMore ? YRecycleviewRefreshFootView.STATE_NODATA : YRecycleviewRefreshFootView.STATE_COMPLETE);
        ((YRecycleviewRefreshFootView) footView).setEmptyTips(emptyContext);
    }

    /**
     * 还原所有的状态
     */
    public void reSetStatus() {
        setloadMoreComplete();
        setReFreshComplete();
    }

    /**
     * 设置可以加载的空布局
     *
     * @param emptyContext 空数据展示的提示
     */
    public void showLoadingEmptyView(String emptyContext) {
        setIsNoData(true, emptyContext);
    }

    /**
     * 设置隐藏脚布局
     */
    public void setLoadingEmptyViewGone() {
        setIsNoData(false, "");
    }
}
