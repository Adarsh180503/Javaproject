package com.vthree.rentbaseapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vthree.rentbaseapplication.ModelClass.ProductOrderModel;
import com.vthree.rentbaseapplication.R;

import java.util.List;

public class MyProductOrderAdapter extends RecyclerView.Adapter<MyProductOrderAdapter.ViewHolder> {
    List<ProductOrderModel> bookingModelsl;
    Context context;

    public MyProductOrderAdapter(List<ProductOrderModel> bookingModelsl, Context context) {
        this.bookingModelsl = bookingModelsl;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_my_order, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.row_my_order_name.setText(context.getString(R.string.product)+" : "+bookingModelsl.get(position).getProduct_name());
        holder.row_booking_date.setText("Price : Rs."+bookingModelsl.get(position).getPrice());
        holder.row_Date.setText("");
        holder.row_time.setText("");
        holder.row_booking_time.setText("Address : "+bookingModelsl.get(position).getAddress());
        holder.row_booking_status.setText(bookingModelsl.get(position).getApprove_status());
        if (bookingModelsl.get(position).getApprove_status().equals("Pending")){
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.yellow));
        }else if (bookingModelsl.get(position).getApprove_status().equals("Success")){
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }else {
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.reject));
        }

    }

    @Override
    public int getItemCount() {
        return bookingModelsl.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView row_my_order_name, row_booking_date, row_booking_time, row_booking_status,row_time,row_Date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            row_my_order_name = itemView.findViewById(R.id.row_my_order_name);
            row_booking_date = itemView.findViewById(R.id.row_booking_date);
            row_booking_time = itemView.findViewById(R.id.row_booking_time);
            row_booking_status = itemView.findViewById(R.id.row_booking_status);
            row_time = itemView.findViewById(R.id.row_time);
            row_Date = itemView.findViewById(R.id.row_Date);
        }
    }
}
