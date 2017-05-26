package cn.yu.yuan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import yuan.kuo.yu.view.YRecycleview;

/**
 * Created by yukuoyuan on 2017/5/26.
 * 这是一个列表设置空布局的界面
 */
public class EmptyViewActivity extends AppCompatActivity {

    private YRecycleview rcv_empty_view;
    private TextView tv_empty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_view);
        rcv_empty_view = (YRecycleview) findViewById(R.id.rcv_empty_view);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        rcv_empty_view.setEmptyView(tv_empty);
        rcv_empty_view.setLayoutManager(new LinearLayoutManager(this));

    }
}
