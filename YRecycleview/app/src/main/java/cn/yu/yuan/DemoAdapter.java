package cn.yu.yuan;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukuo on 2016/4/30.
 */
public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.MyHolder> {
    private List<String> list = new ArrayList<>();

    public DemoAdapter(List<String> list) {
        this.list = list;
    }

    public void addReFreshData() {
        list.add(0,"添加刷新数据");
        notifyDataSetChanged();
    }

    public void addRLoadMOreData() {
        list.add("添加加载更多数据");
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tv_item.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private final TextView tv_item;

        public MyHolder(View itemView) {
            super(itemView);
            tv_item = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }
}
