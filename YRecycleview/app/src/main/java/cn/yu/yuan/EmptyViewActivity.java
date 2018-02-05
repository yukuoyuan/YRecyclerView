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
 * 这是一个列表设置空布局的界面
 */
public class EmptyViewActivity extends AppCompatActivity implements YRecyclerView.OnRefreshAndLoadMoreListener {

    private YRecyclerView rcv_empty_view;
    private ArrayList<String> arrayList = new ArrayList<>();
    private DemoAdapter demoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_view);
        rcv_empty_view = (YRecyclerView) findViewById(R.id.rcv_empty_view);
        rcv_empty_view.setLayoutManager(new LinearLayoutManager(this));
        demoAdapter = new DemoAdapter(arrayList);
        rcv_empty_view.setAdapter(demoAdapter);
        rcv_empty_view.setRefreshAndLoadMoreListener(this);
        rcv_empty_view.showLoadingEmptyView("没数据,等着吧");
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                rcv_empty_view.setLoadingEmptyViewGone();
                demoAdapter.addReFreshData();
                rcv_empty_view.setReFreshComplete();
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                demoAdapter.addRLoadMOreData();
                rcv_empty_view.setReFreshComplete();
            }
        }, 2500);
    }
}
