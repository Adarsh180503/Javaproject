package com.vthree.rentbaseapplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.vthree.rentbaseapplication.ModelClass.ProductModel;
import com.vthree.rentbaseapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProductFarmerAdapter extends RecyclerView.Adapter<ProductFarmerAdapter.ViewHolder> implements Filterable {
    List<ProductModel> mFilterLIst = new ArrayList<ProductModel>();
    List<ProductModel> userModels = new ArrayList<ProductModel>();
    private ProductModel[] listdata;
    Context context;
    static ProductFarmerAdapter.OnArrayItemClick mOnArrayItemClick;
    byte[] imageAsBytes;

    // RecyclerView recyclerView;
    public ProductFarmerAdapter(ProductModel[] listdata) {
        this.listdata = listdata;
    }

    public ProductFarmerAdapter(List<ProductModel> userModels, Context context) {
        this.userModels = userModels;
        this.context = context;
        this.mFilterLIst = userModels;
        setHasStableIds(true);
    }

    public ProductFarmerAdapter(List<ProductModel> userModels) {
        this.userModels = userModels;
    }

    @Override
    public ProductFarmerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem = layoutInflater.inflate(R.layout.product_item_farmer, parent, false);
        ProductFarmerAdapter.ViewHolder viewHolder = new ProductFarmerAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductFarmerAdapter.ViewHolder holder, final int position) {
        final ProductModel equipmentModel = mFilterLIst.get(position);
        try {
            // final SellerDataModel myListData = listdata[position];
            holder.txt_name.setText(mFilterLIst.get(position).getProduct_name());
            holder.txt_contact.setText("Contact : "+mFilterLIst.get(position).getFarmer_mobile());
            holder.quantity.setText("Qty : "+mFilterLIst.get(position).getQuantity() +"\n"+mFilterLIst.get(position).getDescription() );
            String base = "Base64 string values of some image";
            try {
                imageAsBytes = Base64.decode(mFilterLIst.get(position).getImage().getBytes(), Base64.DEFAULT);

                holder.image_equipment.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            } catch (Exception e) {

            }
        } catch (Exception e) {

        }

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
                intent.setData(Uri.parse("tel:"+mFilterLIst.get(position).getFarmer_mobile()));
                context.startActivity(intent);
            }
        });

        holder.IvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteDialoge(position);
            }
        });
    }

    void confirmDeleteDialoge(final int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.want_to_delete));
// Add the buttons
        builder.setPositiveButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mOnArrayItemClick.setOnArrayItemClickListener(position, mFilterLIst.get(position).getProduct_id());
            }
        });
// Set other dialog properties

// Create the AlertDialog
        AlertDialog dialog = builder.create();

        dialog.show();
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

                    List<ProductModel> filterdLIst = new ArrayList<>();
                    for (ProductModel phoneModel : mFilterLIst) {

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

                mFilterLIst = ((List<ProductModel>) results.values);
                notifyDataSetChanged();
            }
        };

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // public ImageView imageView;
        public TextView txt_name, txt_contact,quantity;
        public RelativeLayout relativeLayout;
        ImageView image_equipment,IvDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_name = (TextView) itemView.findViewById(R.id.name);
            this.txt_contact = (TextView) itemView.findViewById(R.id.contact);
            this.quantity=(TextView) itemView.findViewById(R.id.quantity);
            this.image_equipment = (ImageView) itemView.findViewById(R.id.image_equipment);
            this.IvDelete = (ImageView) itemView.findViewById(R.id.IvDelete);
            // relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);


        }
    }

    public interface OnArrayItemClick {
        void setOnArrayItemClickListener(int position, String sellerID);

    }

    public void OnItemClickArrayElement(ProductFarmerAdapter.OnArrayItemClick onArrayItemClick) {

        mOnArrayItemClick = onArrayItemClick;
    }

}
