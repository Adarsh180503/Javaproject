package com.vthree.rentbaseapplication.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vthree.rentbaseapplication.Activity.AddProduct;
import com.vthree.rentbaseapplication.Activity.MainActivity;
import com.vthree.rentbaseapplication.Activity.MapNearByActivity;
import com.vthree.rentbaseapplication.Adapter.EquipmentListAdapter;
import com.vthree.rentbaseapplication.ModelClass.EquipmentModel;
import com.vthree.rentbaseapplication.ModelClass.MyListData;
import com.vthree.rentbaseapplication.R;
import com.vthree.rentbaseapplication.preferences.PrefManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public class HomeEquipment extends Fragment implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<EquipmentModel> list = new ArrayList<>();

    RecyclerView recyclerView;
    int flag = 0;
    EquipmentListAdapter adapter;
    SearchView editsearch;
    Location mLastLocation;
    LocationManager mLocationManager;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    LocationRequest mLocationRequest;
    GoogleMap mGoogleMap;
   // FloatingActionButton show_map;
    SupportMapFragment mapFrag;
    Marker mCurrLocationMarker;
    Button btn_show,btn_addprod;
    PrefManager prefManager;


    // TODO: Rename and change types and number of parameters
    public static HomeEquipment newInstance() {
        HomeEquipment fragment = new HomeEquipment();

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
        View view= inflater.inflate(R.layout.fragment_home_equipment, container, false);
        checkLocationPermission();
        prefManager=new PrefManager(getActivity());
        databaseReference = FirebaseDatabase.getInstance().getReference("EquipmentDetail").child("image");
        adapter = new EquipmentListAdapter(list, getContext());
        MyListData[] myListData = new MyListData[]{
                new MyListData("Email", android.R.drawable.ic_dialog_email),
                new MyListData("Info", android.R.drawable.ic_dialog_info),
                new MyListData("Delete", android.R.drawable.ic_delete),
                new MyListData("Dialer", android.R.drawable.ic_dialog_dialer),
                new MyListData("Alert", android.R.drawable.ic_dialog_alert),
                new MyListData("Map", android.R.drawable.ic_dialog_map),
                new MyListData("Email", android.R.drawable.ic_dialog_email),
                new MyListData("Info", android.R.drawable.ic_dialog_info),
                new MyListData("Delete", android.R.drawable.ic_delete),
                new MyListData("Dialer", android.R.drawable.ic_dialog_dialer),
                new MyListData("Alert", android.R.drawable.ic_dialog_alert),
                new MyListData("Map", android.R.drawable.ic_dialog_map),
        };

        btn_show=(Button)view.findViewById(R.id.btn_showmap);
        btn_addprod=view.findViewById(R.id.btn_addprod);
        btn_addprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddProduct.class);
                startActivity(intent);
            }
        });
        //   mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.serachmap);
        //   mapFrag.getMapAsync(this);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return ;
            }
        }*/
        editsearch=(SearchView)view.findViewById(R.id.searchView);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading_firebase));
        progressDialog.show();


//todays date
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        System.out.println(dateToStr);

        adapter.OnItemClickArrayElement(new OnItemClickListenerArray());

        recyclerView.setAdapter(adapter);

        editsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editsearch.setIconified(false);
            }
        });
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        buildGoogleApiClient();

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), MapNearByActivity.class);
                startActivity(intent);
                Log.d("click","click");
            }
        });
       /* show_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), MapNearByActivity.class);
                startActivity(intent);
                Log.d("click","click");
            }
        });*/

        return view;
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    boolean checkRadius(
            int radius,
            double centerLatitude ,
            double centerLongitud,
            double testLatitude,
            double testLongitude
    ){
        float[] results =new  float[1];
        Location.distanceBetween(centerLatitude, centerLongitud, testLatitude, testLongitude, results);
        float distanceInMeters = results[0];
        return distanceInMeters < radius;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            mLastLocation=location;
            Log.d("location",mLastLocation.getLatitude()+"  :   "+mLastLocation.getLongitude());
        }
    };
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            this.mLocationRequest = new LocationRequest();
            this.mLocationRequest.setInterval(1000);
            this.mLocationRequest.setFastestInterval(1000);
            this.mLocationRequest.setPriority(102);
            if (ContextCompat.checkSelfPermission(getContext(), "android.permission.ACCESS_FINE_LOCATION") == 0) {
                LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, this.mLocationRequest, (LocationListener) this);
            }
        }catch (Exception e)
        {

        }

    }

    public void mapDialoge()
    {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.activity_map);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.setTitle("Title...");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!= null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            mLastLocation = location;
            Log.d("location88",mLastLocation.getLatitude()+"  :   "+mLastLocation.getLongitude());
        }


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));

       /* CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(5000)
                .strokeWidth(2)
                .strokeColor(Color.BLUE)
                .fillColor(Color.parseColor("#500084d3"));
        mGoogleMap.addCircle(circleOptions);*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private class OnItemClickListenerArray implements EquipmentListAdapter.OnArrayItemClick {
        @Override
        public void setOnArrayItemClickListener(final int position, final String sellerID) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
         //   Query key = ref.child("EquipmentDetail").child("image").orderByChild("equipment_id").equalTo(sellerID).orderByChild("equipment_id");
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
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.permission_needed))
                        .setMessage(getString(R.string.permi_msg))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        //mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), getString(R.string.permi_denied), Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onResume() {
        super.onResume();
       // DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
       Query key = databaseReference.child("EquipmentDetail").child("image").orderByChild("dist").endAt(20);

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
