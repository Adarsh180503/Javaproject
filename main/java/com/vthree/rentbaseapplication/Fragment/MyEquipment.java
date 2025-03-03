package com.vthree.rentbaseapplication.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vthree.rentbaseapplication.Activity.EquipmentRegisterActivity;
import com.vthree.rentbaseapplication.Adapter.EquipmentListAdapter;
import com.vthree.rentbaseapplication.Adapter.MyEquipmentListAdapter;
import com.vthree.rentbaseapplication.ModelClass.EquipmentModel;
import com.vthree.rentbaseapplication.R;
import com.vthree.rentbaseapplication.preferences.PrefManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class MyEquipment extends Fragment {

    PrefManager prefManager;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    List<EquipmentModel> list = new ArrayList<>();
    RecyclerView recyclerView;
    int flag = 0;
    MyEquipmentListAdapter adapter;
    Button addequipment;
    String user_id;

    // TODO: Rename and change types and number of parameters
    public static MyEquipment newInstance() {
        MyEquipment fragment = new MyEquipment();

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
         View view=inflater.inflate(R.layout.fragment_my_equipment, container, false);

        prefManager=new PrefManager(getActivity());
        user_id = prefManager.getString("user_id");
        databaseReference = FirebaseDatabase.getInstance().getReference("EquipmentDetail").child("image");
        adapter = new MyEquipmentListAdapter(list, getContext());

        addequipment=(Button) view.findViewById(R.id.addequipment);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();



        adapter.OnItemClickArrayElement(new OnItemClickListenerArray());

        recyclerView.setAdapter(adapter);

        addequipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EquipmentRegisterActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    private class OnItemClickListenerArray implements MyEquipmentListAdapter.OnArrayItemClick {
        @Override
        public void setOnArrayItemClickListener(final int position, final String sellerID) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query key = ref.child("EquipmentDetail").child("image").orderByChild("equipment_id").equalTo(sellerID);
            key.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getEquipment_id() == sellerID) {
                                list.remove(i);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        Log.d("position", "" + appleSnapshot.getKey());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("position", "onCancelled", databaseError.toException());
                }
            });
            Log.d("position11", "" + position + "   " + key + "  " + sellerID);
            //databaseReference.child("seller").child(key).removeValue();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query key = databaseReference.child("EquipmentDetail").child("image").orderByChild("user_id").equalTo(user_id);
        key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            EquipmentModel model = snapshot.getValue(EquipmentModel.class);
                            //  latLng=getLocationFromAddress(model.getAddress());
                            //   Log.d("data2",mLastLocation.getLatitude()+" : "+mLastLocation.getLongitude()+" : "+latLng.latitude+" : "+latLng.longitude);
                            // boolean fenc= checkRadius(10000,mLastLocation.getLatitude(),mLastLocation.getLongitude(),latLng.latitude,latLng.longitude);
                            if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (model.getEquipment_id().equals(list.get(i).getEquipment_id())) {
                                        flag = 1;
                                        Log.d("data2", model.getEquipment_name().toString());
                                    }
                                }
                                if (flag == 1) {
                                } else {
                                    flag = 0;
                                    //if (fenc==true) {
                                    list.add(model);
                                    Log.d("data", model.getEquipment_name().toString());
                                    Log.d("data", model.getContact().toString());
                                       /* MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(latLng);
                                        markerOptions.title(model.getEquipment_name());

                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);*/
                                    // }
                                }
                            } else {
                                // if (fenc==true) {
                                list.add(model);
                                Log.d("data11", model.getEquipment_name().toString());
                                Log.d("data11", model.getContact().toString());
                                //  progressDialog.dismiss();
                                 /*   MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                                    markerOptions.title(model.getEquipment_name());

                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);*/
                                // }
                            }
                            //  Log.d("datas", model.getAddress().toString()+"  lat: "+mLastLocation.getLatitude()+"long: "+mLastLocation.getLongitude()+"  val: "+fenc);
                        }

                    } catch (Exception e) {
                        Log.d("dataqq", e.getMessage());
                    }

                    HashSet<EquipmentModel> hashSet = new HashSet<EquipmentModel>();
                    hashSet.addAll(list);
                    list.clear();
                    list.addAll(hashSet);
                    Log.d("size1", "" + list.size());

                    Collections.sort(list);

                    for(EquipmentModel str: list){
                        System.out.println(str);
                    }

                    adapter.notifyDataSetChanged();
                }
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("data", databaseError.getMessage());
            }
        });


    }

}
