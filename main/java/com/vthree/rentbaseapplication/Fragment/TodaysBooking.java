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
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vthree.rentbaseapplication.Adapter.BookOrderAdapter;
import com.vthree.rentbaseapplication.ModelClass.BookingModel;
import com.vthree.rentbaseapplication.R;
import com.vthree.rentbaseapplication.preferences.PrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public class TodaysBooking extends Fragment {

    RecyclerView book_order_recycler;
    PrefManager prefManager;
    String user_id;
    DatabaseReference databaseReference;

    List<BookingModel> list;
    BookOrderAdapter myOrderAdapter;
    int flag=0;
   // LinearLayout emptyview;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public TodaysBooking() {
    }

    public static TodaysBooking newInstance(int sectionNumber) {
        TodaysBooking fragment = new TodaysBooking();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_todays_booking, container, false);

       // emptyview =view.findViewById(R.id.emptyview);
        book_order_recycler=view.findViewById(R.id.book_order_recycler);
        prefManager = new PrefManager(getContext());
        user_id = prefManager.getString("user_id");
        databaseReference = FirebaseDatabase.getInstance().getReference("equipmentBooking").child("data");
        book_order_recycler.hasFixedSize();
        book_order_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<BookingModel>();
        myOrderAdapter=new BookOrderAdapter(list,getContext());
        book_order_recycler.setAdapter(myOrderAdapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("daa", "onresume");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //   Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("user_id").equalTo(user_id);


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy");
        String formattedDate = df.format(c);

        Log.e("Datea",formattedDate);

        Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("book_date").equalTo(formattedDate);
        //  Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("book_date").equalTo(formattedDate);
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
                    for(int i=0; i <list.size(); i++){
                        if(!user_id.equals(list.get(i).getUser_id())){
                            list.remove(i);
                            // adapter.notifyDataSetChanged();
                        }
                    }
                    Log.d("size1", "" + list.size());

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
