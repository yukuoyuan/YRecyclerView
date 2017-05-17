package cn.yu.yuan;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import yuan.kuo.yu.view.YRecycleview;

/**
 * Created by yukuoyuan on 2017/3/25.
 * 这是一个测试上啦刷新,下拉加载更多的界面
 */
public class SwipeLoadMoreAndRefreshActivity extends AppCompatActivity {
    private List<SwipeDate> list = new ArrayList<>();
    private YRecycleview ycl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadmore_refresh);
        ycl = (YRecycleview) findViewById(R.id.ycl);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            SwipeDate swipeDate = new SwipeDate();
            swipeDate.name = "于" + i;
            swipeDate.type = 1;
            list.add(swipeDate);
        }
        final DemoSwipeAdapter demoAdapter = new DemoSwipeAdapter(list);
        View view = View.inflate(this, R.layout.emptyview, null);
        ycl.setEmptyView(view);
        ycl.setLayoutManager(new LinearLayoutManager(this));
        ycl.setAdapter(demoAdapter);
        ycl.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        demoAdapter.addReFreshData();
                        ycl.setReFreshComplete();
                    }
                }, 2500);
            }

            @Override
            public void onLoadMore() {
                Log.i("加载更多", "000");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        demoAdapter.addRLoadMOreData();
                        ycl.setloadMoreComplete();
                    }
                }, 2500);
            }
        });
    }
}
