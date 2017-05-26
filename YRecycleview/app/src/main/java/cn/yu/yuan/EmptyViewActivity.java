package cn.yu.yuan;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import java.util.ArrayList;

import yuan.kuo.yu.view.YRecycleview;

/**
 * Created by yukuoyuan on 2017/5/26.
 * 这是一个列表设置空布局的界面
 */
public class EmptyViewActivity extends AppCompatActivity implements YRecycleview.OnRefreshAndLoadMoreListener {

    private YRecycleview rcv_empty_view;
    private TextView tv_empty;
    private ArrayList<String> arrayList = new ArrayList<>();
    private DemoAdapter demoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_view);
        rcv_empty_view = (YRecycleview) findViewById(R.id.rcv_empty_view);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        rcv_empty_view.setEmptyView(tv_empty);
        rcv_empty_view.setLayoutManager(new LinearLayoutManager(this));
        demoAdapter = new DemoAdapter(arrayList);
        rcv_empty_view.setAdapter(demoAdapter);
        rcv_empty_view.setRefreshAndLoadMoreListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
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
