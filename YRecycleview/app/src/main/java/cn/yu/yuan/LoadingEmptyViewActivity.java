package cn.yu.yuan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import yuan.kuo.yu.view.YRecycleview;

/**
 * Created by yukuoyuan on 2017/5/26.
 * 这是一个可下拉刷新加载更多的空布局界面
 */
public class LoadingEmptyViewActivity extends AppCompatActivity {

    private YRecycleview rcv_loading_empty_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_empty_view);
        rcv_loading_empty_view = (YRecycleview) findViewById(R.id.rcv_loading_empty_view);
    }
}
