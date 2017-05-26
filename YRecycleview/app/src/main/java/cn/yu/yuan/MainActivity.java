package cn.yu.yuan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_load_more_refresh;
    private Button bt_load_more_refresh_swipe;
    private Intent intent;
    private Button bt_set_empty_view;
    private Button bt_set_loading_empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_load_more_refresh = (Button) findViewById(R.id.bt_load_more_refresh);
        bt_load_more_refresh_swipe = (Button) findViewById(R.id.bt_load_more_refresh_swipe);
        bt_set_empty_view = (Button) findViewById(R.id.bt_set_empty_view);
        bt_set_loading_empty_view = (Button) findViewById(R.id.bt_set_loading_empty_view);
        bt_load_more_refresh.setOnClickListener(this);
        bt_load_more_refresh_swipe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_load_more_refresh:
                intent = new Intent(MainActivity.this, LoadMoreAndRefreshActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_load_more_refresh_swipe:
                intent = new Intent(MainActivity.this, SwipeLoadMoreAndRefreshActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_set_empty_view:
                intent = new Intent(MainActivity.this, EmptyViewActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_set_loading_empty_view:
                intent = new Intent(MainActivity.this, LoadingEmptyViewActivity.class);
                startActivity(intent);
                break;
        }
    }
}
