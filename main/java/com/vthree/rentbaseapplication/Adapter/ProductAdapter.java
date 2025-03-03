package com.vthree.rentbaseapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vthree.rentbaseapplication.Activity.ViewProducts;
import com.vthree.rentbaseapplication.ModelClass.ProductModel;
import com.vthree.rentbaseapplication.ModelClass.ProductOrderModel;
import com.vthree.rentbaseapplication.R;
import com.vthree.rentbaseapplication.preferences.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {
    List<ProductModel> mFilterLIst = new ArrayList<ProductModel>();
    List<ProductModel> userModels = new ArrayList<ProductModel>();
    private ProductModel[] listdata;
    Context context;
    static OnArrayItemClick mOnArrayItemClick;
    byte[] imageAsBytes;
    PrefManager prefManager;


    // RecyclerView recyclerView;
    public ProductAdapter(ProductModel[] listdata) {
        this.listdata = listdata;
    }

    public ProductAdapter(List<ProductModel> userModels, Context context) {
        this.userModels = userModels;
        this.context = context;
        this.mFilterLIst = userModels;
        setHasStableIds(true);
    }

    public ProductAdapter(List<ProductModel> userModels) {
        this.userModels = userModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem = layoutInflater.inflate(R.layout.product_item, parent, false);
      ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ProductModel model = mFilterLIst.get(position);
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

        holder.btn_book0rder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final android.app.AlertDialog builder=new AlertDialog.Builder(context).create();
                View view1= LayoutInflater.from(context).inflate(R.layout.dailog_book_product,null);
                TextView txt_prodname=view1.findViewById(R.id.txt_prodname);
                TextView txt_amount=view1.findViewById(R.id.txt_amount);
                final AppCompatEditText edtAddress=view1.findViewById(R.id.edtAddress);
                final AppCompatEditText edtQuantity=view1.findViewById(R.id.edtQuantity);


                txt_prodname.setText(model.getProduct_name());
                txt_amount.setText("Rs. "+model.getPrice());

                prefManager=new PrefManager(context);
                final String cust_id=prefManager.getString("user_id");
                final String customer_pincode=prefManager.getString("pincode");



                Button dailog_ok=(Button)view1.findViewById(R.id.dailog_ok);
                dailog_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProductsOrders").child("data");

                        String productorder_id = databaseReference.push().getKey();
                        String address=edtAddress.getText().toString();
                        String quantity=edtQuantity.getText().toString();
                        databaseReference.child(productorder_id).setValue(new ProductOrderModel(productorder_id,model.getProduct_id(),cust_id,model.getProduct_name(),model.getPrice(),address,customer_pincode,model.getImage(),model.getUser_id(),quantity,model.getFarmer_mobile(),"Success","Pending"))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, context.getString(R.string.order_sucess), Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(context, ViewProducts.class);
                                        context.startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, context.getString(R.string.oreder_fail), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        builder.dismiss();
                    }
                });

                builder.setView(view1);
                builder.setCanceledOnTouchOutside(true);
                builder.show();
            }
        });
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
        ImageView image_equipment;
        Button btn_book0rder;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_name = (TextView) itemView.findViewById(R.id.name);
            this.txt_contact = (TextView) itemView.findViewById(R.id.contact);
            this.quantity=(TextView) itemView.findViewById(R.id.quantity);
            this.image_equipment = (ImageView) itemView.findViewById(R.id.image_equipment);
            // relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            this.btn_book0rder = (Button) itemView.findViewById(R.id.btn_book0rder);

        }
    }

    public interface OnArrayItemClick {
        void setOnArrayItemClickListener(int position, String sellerID);

    }

    public void OnItemClickArrayElement(ProductAdapter.OnArrayItemClick onArrayItemClick) {

        mOnArrayItemClick = onArrayItemClick;
    }

}
