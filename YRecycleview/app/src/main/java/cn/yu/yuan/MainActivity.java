package cn.yu.yuan;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import yuan.kuo.yu.view.YRecycleview;

public class MainActivity extends AppCompatActivity {
    private List<String> list = new ArrayList<>();
    private YRecycleview ycl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ycl = (YRecycleview) findViewById(R.id.ycl);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            list.add("于" + i);
        }
        final DemoAdapter demoAdapter = new DemoAdapter(list);
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
                Log.i("加载更多","000");
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
