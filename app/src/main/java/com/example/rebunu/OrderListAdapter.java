package com.example.rebunu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import javax.annotation.Nullable;

public class OrderListAdapter extends ArrayAdapter<Order> {
    private ArrayList<Order> orders;
    private Context context;

    public OrderListAdapter(Context context, ArrayList<Order> orders) {
        super(context, 0, orders);
        this.orders = orders;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.single_order, parent, false);
        }

        Order order = orders.get(position);

        TextView id = view.findViewById(R.id.single_order_textview_id);
        TextView status = view.findViewById(R.id.single_order_textview_status);
        TextView price = view.findViewById(R.id.single_order_textview_price);

        id.setText(order.getId());
        status.setText(Utility.parseOrderStatus(order.getStatus(), true));
        price.setText(order.getPrice().toString());
        return view;
    }

}
