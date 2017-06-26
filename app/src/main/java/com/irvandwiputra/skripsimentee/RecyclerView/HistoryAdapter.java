package com.irvandwiputra.skripsimentee.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.irvandwiputra.skripsimentee.Model.Order;
import com.irvandwiputra.skripsimentee.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alvinoktavianus on 6/26/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<Order> mOrders;
    private Context context;

    public HistoryAdapter(List<Order> mOrders, Context context) {
        this.mOrders = mOrders;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View historyView = layoutInflater.inflate(R.layout.recycler_order, parent, false);
        ViewHolder viewHolder = new ViewHolder(historyView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = mOrders.get(position);
        TextView orderNo = holder.txtOrderNo;
        orderNo.setText(order.getOrder_no());
        TextView course = holder.txtCourse;
        course.setText(order.getCourse_name());
        TextView description = holder.txtDescription;
        description.setText(order.getOrder_description());
        TextView status = holder.txtStatus;
        status.setText(order.getStatus());
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.recyclerOrderNo)
        TextView txtOrderNo;
        @Bind(R.id.recyclerTxtCourse)
        TextView txtCourse;
        @Bind(R.id.recyclerTxtDescription)
        TextView txtDescription;
        @Bind(R.id.recyclerTxtStatus)
        TextView txtStatus;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
