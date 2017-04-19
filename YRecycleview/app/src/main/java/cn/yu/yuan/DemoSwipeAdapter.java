package cn.yu.yuan;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukuo on 2016/4/30.
 */
public class DemoSwipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SwipeDate> list = new ArrayList<>();

    public DemoSwipeAdapter(List<SwipeDate> list) {
        this.list = list;
    }

    public void addReFreshData() {
        notifyDataSetChanged();
    }

    public void addRLoadMOreData() {
        notifyDataSetChanged();
    }

    /**
     * 删除一个数据的方法
     *
     * @param position 索引
     */
    // TODO 一定要按照这个方式写,不然会crash,希望你有更好的解决方案
    public void removeData(int position) {
        notifyItemRemoved(position + 1);
        if (position != list.size()) {
            if (position == 0) {
                list.remove(position);
                notifyDataSetChanged();
            } else if (position == (list.size() - 1)) {
                list.remove(position);
                notifyItemRangeChanged(position, 0);
            } else {
                list.remove(position-1);
                notifyItemRangeChanged(position, list.size() - position + 1);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = View.inflate(parent.getContext(), R.layout.item_swipe_menu, null);
            return new MySwipeMenuHolder(view);
        } else {
            view = View.inflate(parent.getContext(), R.layout.item_content, null);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.item_content.setText(list.get(position).name);
        } else if (holder instanceof MySwipeMenuHolder) {
            MySwipeMenuHolder myHolder = (MySwipeMenuHolder) holder;
            myHolder.item_content.setText(list.get(position).name + "######" + position);
            myHolder.item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), list.get(position).name, Toast.LENGTH_SHORT).show();
                }
            });
            myHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeData(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private final TextView item_content;

        public MyHolder(View itemView) {
            super(itemView);
            item_content = (TextView) itemView.findViewById(R.id.item_content);
        }
    }

    class MySwipeMenuHolder extends RecyclerView.ViewHolder {

        private final TextView delete;
        private final TextView ok;
        private final TextView item_content;

        public MySwipeMenuHolder(View itemView) {
            super(itemView);
            item_content = (TextView) itemView.findViewById(R.id.item_content);

            delete = (TextView) itemView.findViewById(R.id.delete);
            ok = (TextView) itemView.findViewById(R.id.ok);
        }
    }
}
