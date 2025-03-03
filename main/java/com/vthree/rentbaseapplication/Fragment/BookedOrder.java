package com.vthree.rentbaseapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vthree.rentbaseapplication.Activity.BookedOrderActivity;
import com.vthree.rentbaseapplication.Activity.LoginActivity;
import com.vthree.rentbaseapplication.Activity.RegisterActivity;
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


public class BookedOrder extends Fragment {


    RecyclerView book_order_recycler;
    PrefManager prefManager;
    String user_id;
    DatabaseReference databaseReference;

    List<BookingModel> list;
    BookOrderAdapter myOrderAdapter;
    int flag=0;

    private ViewPager view_pager;
    private TabLayout tab_layout;

    Button past_booking,upcoming_booking,todays_booking;

    // TODO: Rename and change types and number of parameters
    public static BookedOrder newInstance() {
        BookedOrder fragment = new BookedOrder();

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
        View view =  inflater.inflate(R.layout.fragment_booked_order, container, false);

        view_pager = (ViewPager) view.findViewById(R.id.view_pager);
        setupViewPager(view_pager);

        tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);

     /*   past_booking=view.findViewById(R.id.past_booking);
        upcoming_booking=view.findViewById(R.id.upcoming_booking);
        todays_booking=view.findViewById(R.id.todays_booking);*/

     //   book_order_recycler=view.findViewById(R.id.book_order_recycler);
        prefManager = new PrefManager(getContext());
        user_id = prefManager.getString("user_id");
        databaseReference = FirebaseDatabase.getInstance().getReference("equipmentBooking").child("data");
       /* book_order_recycler.hasFixedSize();
        book_order_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
      list=new ArrayList<BookingModel>();
          myOrderAdapter=new BookOrderAdapter(list,getContext());
        book_order_recycler.setAdapter(myOrderAdapter);*/

      /*  past_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list=new ArrayList<BookingModel>();
              PastBooking();
            }
        });

        upcoming_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list=new ArrayList<BookingModel>();
               UpcomingBooking();
            }
        });
        todays_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //list=new ArrayList<BookingModel>();
             //  TodaysBooking();
                Fragment fragment= new TodaysBooking();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment); // fragmen container id in first parameter is the  container(Main layout id) of Activity
                transaction.addToBackStack(null);  // this will manage backstack
                transaction.commit();
            }
        });*/


        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(Todays.newInstance(1), getString(R.string.today));
        adapter.addFragment(Past.newInstance(2), getString(R.string.past));
        adapter.addFragment(Upcoming.newInstance(3), getString(R.string.upcoming));
        viewPager.setAdapter(adapter);
    }


    public static class Todays extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        RecyclerView book_order_recycler;
        PrefManager prefManager;
        String user_id;
        DatabaseReference databaseReference;

        List<BookingModel> list;
        BookOrderAdapter myOrderAdapter;
        int flag=0;
      //  LinearLayout emptyview;

        public Todays() {
        }

        public static Todays newInstance(int sectionNumber) {
            Todays fragment = new Todays();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_todays_booking, container, false);
         //   emptyview =view.findViewById(R.id.emptyview);
            book_order_recycler=view.findViewById(R.id.book_order_recycler);
            prefManager = new PrefManager(getContext());
            user_id = prefManager.getString("user_id");
            databaseReference = FirebaseDatabase.getInstance().getReference("equipmentBooking").child("data");
            book_order_recycler.hasFixedSize();
            book_order_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
            list=new ArrayList<BookingModel>();
            TodaysBooking();
         //   myOrderAdapter=new BookOrderAdapter(list,getContext());
          //  book_order_recycler.setAdapter(myOrderAdapter);

            return view;
        }


        public void TodaysBooking(){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy");
            String formattedDate = df.format(c);

            Log.e("Datea",formattedDate+"//"+formattedDate);

            //     Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("b_id");
            Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("book_from_date").equalTo(formattedDate);

            // Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("book_date").endAt(String.valueOf(today));
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
                            if(!user_id.equals(list.get(i).getBook_user_id())){
                                list.remove(i);
                               // adapter.notifyDataSetChanged();
                            }
                        }

                        if(list.size()>0){

                            myOrderAdapter=new BookOrderAdapter(list,getContext());
                            book_order_recycler.setAdapter(myOrderAdapter);
                            myOrderAdapter.notifyDataSetChanged();
                            Log.d("daaa", model.getEquipment_name().toString());
                        }else{

                            Toast.makeText(getContext(), getString(R.string.not_today_booking), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    public static class Past extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        RecyclerView book_order_recycler;
        PrefManager prefManager;
        String user_id;
        DatabaseReference databaseReference;

        List<BookingModel> list;
        BookOrderAdapter myOrderAdapter;
        int flag=0;
        //  LinearLayout emptyview;

        public Past() {
        }

        public static Past newInstance(int sectionNumber) {
            Past fragment = new Past();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_todays_booking, container, false);
            //   emptyview =view.findViewById(R.id.emptyview);
            book_order_recycler=view.findViewById(R.id.book_order_recycler);
            prefManager = new PrefManager(getContext());
            user_id = prefManager.getString("user_id");
            databaseReference = FirebaseDatabase.getInstance().getReference("equipmentBooking").child("data");
            book_order_recycler.hasFixedSize();
            book_order_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
            list=new ArrayList<BookingModel>();
            PastBooking();
            //   myOrderAdapter=new BookOrderAdapter(list,getContext());
            //  book_order_recycler.setAdapter(myOrderAdapter);

            return view;
        }


        public void PastBooking(){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy");
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String formattedDate = df.format(cal.getTime());//yesterday date

            Log.e("Datea",formattedDate+"//"+formattedDate);

            //     Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("b_id");
            Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("status").equalTo("Approved");

            // Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("book_date").endAt(String.valueOf(today));
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
                            if(!user_id.equals(list.get(i).getBook_user_id())){
                                list.remove(i);
                                // adapter.notifyDataSetChanged();
                            }
                        }
                        if(list.size()>0){

                            myOrderAdapter=new BookOrderAdapter(list,getContext());
                            book_order_recycler.setAdapter(myOrderAdapter);
                            myOrderAdapter.notifyDataSetChanged();
                            Log.d("daaa", model.getEquipment_name().toString());
                        }/*else{
                            Toast.makeText(getContext(), getString(R.string.not_past_booking), Toast.LENGTH_SHORT).show();

                        }*/

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

    public static class Upcoming extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        RecyclerView book_order_recycler;
        PrefManager prefManager;
        String user_id;
        DatabaseReference databaseReference;

        List<BookingModel> list;
        BookOrderAdapter myOrderAdapter;
        int flag=0;
        //  LinearLayout emptyview;

        public Upcoming() {
        }

        public static Upcoming newInstance(int sectionNumber) {
            Upcoming fragment = new Upcoming();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_todays_booking, container, false);
            //   emptyview =view.findViewById(R.id.emptyview);
            book_order_recycler=view.findViewById(R.id.book_order_recycler);
            prefManager = new PrefManager(getContext());
            user_id = prefManager.getString("user_id");
            databaseReference = FirebaseDatabase.getInstance().getReference("equipmentBooking").child("data");
            book_order_recycler.hasFixedSize();
            book_order_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
            list=new ArrayList<BookingModel>();
            UpcomingBooking();
            //   myOrderAdapter=new BookOrderAdapter(list,getContext());
            //  book_order_recycler.setAdapter(myOrderAdapter);

            return view;
        }


        public void UpcomingBooking(){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy");
            String formattedDate = df.format(c);

            Log.e("Datea",formattedDate+"//"+formattedDate);

            //     Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("b_id");
         //   Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("book_date").startAt(formattedDate);
            Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("status").equalTo("Pending");

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
                            if(!user_id.equals(list.get(i).getBook_user_id())){
                                list.remove(i);
                                // adapter.notifyDataSetChanged();
                            }
                        }

                        if(list.size()>0){

                            myOrderAdapter=new BookOrderAdapter(list,getContext());
                            book_order_recycler.setAdapter(myOrderAdapter);
                            myOrderAdapter.notifyDataSetChanged();
                            Log.d("daaa", model.getEquipment_name().toString());
                        }else{

                            Toast.makeText(getContext(),  getString(R.string.not_upcoing_booking), Toast.LENGTH_SHORT).show();


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

  /*  @Override
    public void onResume() {
        super.onResume();
        Log.d("daa", "onresume");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
     //   Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("user_id").equalTo(user_id);


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy");
        String formattedDate = df.format(c);

        Log.e("Datea",formattedDate);

        Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("b_id");
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
                    Log.d("size1", "" + list.size());

                    myOrderAdapter.notifyDataSetChanged();

                    Log.d("daaa", model.getEquipment_name().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/



}
