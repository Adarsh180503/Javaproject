package com.vthree.rentbaseapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vthree.rentbaseapplication.ModelClass.ProductOrderModel;
import com.vthree.rentbaseapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProductOrderAdapterCustomer extends RecyclerView.Adapter<ProductOrderAdapterCustomer.ViewHolder> implements Filterable {
    List<ProductOrderModel> mFilterLIst = new ArrayList<ProductOrderModel>();
    List<ProductOrderModel> userModels = new ArrayList<ProductOrderModel>();
    private ProductOrderModel[] listdata;
    Context context;
    static OnArrayItemClick mOnArrayItemClick;
    byte[] imageAsBytes;

    // RecyclerView recyclerView;
    public ProductOrderAdapterCustomer(ProductOrderModel[] listdata) {
        this.listdata = listdata;
    }

    public ProductOrderAdapterCustomer(List<ProductOrderModel> userModels, Context context) {
        this.userModels = userModels;
        this.context = context;
        this.mFilterLIst = userModels;
        setHasStableIds(true);
    }

    public ProductOrderAdapterCustomer(List<ProductOrderModel> userModels) {
        this.userModels = userModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem = layoutInflater.inflate(R.layout.order_product_item_customer, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ProductOrderModel model = mFilterLIst.get(position);
        try {
            // final SellerDataModel myListData = listdata[position];
            holder.txt_name.setText(mFilterLIst.get(position).getProduct_name());
            holder.txt_contact.setText("Price : "+mFilterLIst.get(position).getPrice() +"\n"+"Contact : "+mFilterLIst.get(position).getFarmMobile());
            holder.quantity.setText("Qty : "+mFilterLIst.get(position).getQuantity()+"\n"+"Address : "+mFilterLIst.get(position).getAddress() );
            String base = "Base64 string values of some image";
            try {
                imageAsBytes = Base64.decode(mFilterLIst.get(position).getImage().getBytes(), Base64.DEFAULT);

                holder.image_equipment.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            } catch (Exception e) {

            }
        } catch (Exception e) {

        }
        holder.row_status.setText("Approve Status: "+model.getApprove_status());

        holder.row_status_delivery.setVisibility(View.GONE);
        holder.row_status_delivery.setText("Delivery Status: "+model.getDeliver_status());
        holder.row_status_delivery.setTextColor(context.getResources().getColor(R.color.blue));

        if (model.getApprove_status().equals("Approved")){

            holder.row_status.setText(context.getString(R.string.approve));
            holder.row_status.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else if (model.getApprove_status().equals("Disapproved")) {
            holder.row_status.setText(context.getString(R.string.diapprove));
            holder.row_status.setTextColor(context.getResources().getColor(R.color.reject));
        }else {
            holder.row_status.setText(mFilterLIst.get(position).getApprove_status());
            holder.row_status.setTextColor(context.getResources().getColor(R.color.yellow));
        }

     /*   if (mFilterLIst.get(position).getApprove_status().equals("Pending")) {
            holder.row_status.setTextColor(context.getResources().getColor(R.color.yellow));
            // holder.pendding.setVisibility(View.VISIBLE);
        } else if (mFilterLIst.get(position).getApprove_status().equals("Approved")) {
            holder.row_status.setTextColor(context.getResources().getColor(R.color.colorAccent));
            //holder.row_show_details.setVisibility(View.VISIBLE);
            holder.pendding.setVisibility(View.GONE);
            holder.row_status.setText(context.getString(R.string.approve));
        } else {
            holder.row_status.setTextColor(context.getResources().getColor(R.color.reject));
            holder.pendding.setVisibility(View.GONE);
            holder.row_status.setText(context.getString(R.string.diapprove));
        }*/



       /* holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click on item: " + myListData.getDescription(), Toast.LENGTH_LONG).show();
            }
        });*/
        holder.txt_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+model.getFarmMobile()));
               // intent.setData(Uri.parse("tel:"+holder.txt_contact.getText().toString()));
                context.startActivity(intent);
            }
        });

     /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookEquipementActivity.class);
                intent.putExtra("equipmentData", equipmentModel);

                context.startActivity(intent);
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return mFilterLIst.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {

                    mFilterLIst = userModels;

                } else {

                    List<ProductOrderModel> filterdLIst = new ArrayList<>();
                    for (ProductOrderModel phoneModel : mFilterLIst) {

                        if (phoneModel.getProduct_name().toLowerCase().contains(charString)) {

                            filterdLIst.add(phoneModel);
                        }
                    }

                    mFilterLIst = filterdLIst;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterLIst;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mFilterLIst = ((List<ProductOrderModel>) results.values);
                notifyDataSetChanged();
            }
        };

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // public ImageView imageView;
        public TextView txt_name, txt_contact,quantity,row_status,row_status_delivery;
        public RelativeLayout relativeLayout;
        ImageView image_equipment;


        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_name = (TextView) itemView.findViewById(R.id.name);
            this.txt_contact = (TextView) itemView.findViewById(R.id.contact);
            this.quantity=(TextView) itemView.findViewById(R.id.quantity);
            this.image_equipment = (ImageView) itemView.findViewById(R.id.image_equipment);

            row_status = (TextView) itemView.findViewById(R.id.row_status);
            row_status_delivery= (TextView) itemView.findViewById(R.id.row_status_delivery);


        }
    }

    public interface OnArrayItemClick {
        void setOnArrayItemClickListener(int position, String sellerID);

    }

    public void OnItemClickArrayElement(OnArrayItemClick onArrayItemClick) {

        mOnArrayItemClick = onArrayItemClick;
    }

}
