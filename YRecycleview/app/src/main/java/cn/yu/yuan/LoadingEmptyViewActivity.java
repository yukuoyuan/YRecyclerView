package cn.yu.yuan;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import yuan.kuo.yu.view.YRecyclerView;

/**
 * Created by yukuoyuan on 2017/5/26.
 * 这是一个可下拉刷新加载更多的空布局界面
 */
public class LoadingEmptyViewActivity extends AppCompatActivity implements YRecyclerView.OnRefreshAndLoadMoreListener {

    private YRecyclerView rcv_loading_empty_view;
    private ArrayList<String> arrayList = new ArrayList<>();
    private DemoAdapter demoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_empty_view);
        rcv_loading_empty_view = (YRecyclerView) findViewById(R.id.rcv_loading_empty_view);
        rcv_loading_empty_view.setLayoutManager(new LinearLayoutManager(this));
        rcv_loading_empty_view.showLoadingEmptyView("稍等哦亲!暂无数据~");
        demoAdapter = new DemoAdapter(arrayList);
        rcv_loading_empty_view.setAdapter(demoAdapter);
        rcv_loading_empty_view.setRefreshAndLoadMoreListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                demoAdapter.addReFreshData();
                rcv_loading_empty_view.setReFreshComplete();
                rcv_loading_empty_view.setLoadingEmptyViewGone();
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                demoAdapter.addRLoadMOreData();
                rcv_loading_empty_view.setReFreshComplete();
                rcv_loading_empty_view.setLoadingEmptyViewGone();
            }
        }, 2500);

    }
}
