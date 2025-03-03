package com.vthree.rentbaseapplication.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vthree.rentbaseapplication.Activity.MyOrderActivity;
import com.vthree.rentbaseapplication.Adapter.MyOrderAdapter;
import com.vthree.rentbaseapplication.ModelClass.BookingModel;
import com.vthree.rentbaseapplication.R;
import com.vthree.rentbaseapplication.preferences.PrefManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MyOrder extends Fragment {
    PrefManager prefManager;
    String user_id;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    List<BookingModel> list;
    MyOrderAdapter myOrderAdapter;
    int flag=0;

    // TODO: Rename and change types and number of parameters
    public static MyOrder newInstance() {
        MyOrder fragment = new MyOrder();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        prefManager = new PrefManager(getActivity());
        user_id = prefManager.getString("user_id");
        databaseReference = FirebaseDatabase.getInstance().getReference("equipmentBooking").child("data");
        recyclerView=view.findViewById(R.id.myorder_recycler);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        myOrderAdapter=new MyOrderAdapter(list,getActivity());
        recyclerView.setAdapter(myOrderAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("daa", "onresume");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("b_id");
        key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BookingModel model = snapshot.getValue(BookingModel.class);

                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if (model.getB_id().equals(list.get(i).getB_id())) {
                                flag = 1;
                                Log.d("data2", model.getEquipment_name().toString());
                            }
                        }
                        if (flag == 1) {
                        } else {
                            flag = 0;

                            list.add(model);
                            Log.d("data", model.getEquipment_name().toString());
                            Log.d("data", model.getBook_contact().toString());

                        }
                    } else {

                        list.add(model);
                        Log.d("data11", model.getEquipment_name().toString());
                        Log.d("data11", model.getBook_contact().toString());

                    }
                    // list.add(model);
                    HashSet<BookingModel> hashSet = new HashSet<BookingModel>();
                    hashSet.addAll(list);
                    list.clear();
                    list.addAll(hashSet);
                    Log.d("size1", "" + list.size());
                    for(int i=0; i <list.size(); i++){
                        if(!user_id.equals(list.get(i).getUser_id())){
                            list.remove(i);
                            // adapter.notifyDataSetChanged();
                        }
                    }

                    myOrderAdapter.notifyDataSetChanged();

                    Log.d("daaa", model.getEquipment_name().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
