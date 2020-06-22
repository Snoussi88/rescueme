package com.snoussi.univox;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelpActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private String helpFrom, helpto;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private Button start, end;
    private Button test;
    private GoogleMap mMap;
    private Double latitude, longitude;
    private FirebaseUser user;
    private String from;
    private ClusterManager<MyItem> clusterManager;
    private List<Message> mMessages;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpactivity);
        mMessages = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        user = FirebaseAuth.getInstance().getCurrentUser();


        IntentFilter intentFilter = new IntentFilter(Constants.RECEIVE_LOCATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);


        test = findViewById(R.id.test);



        start = findViewById(R.id.start);
        end = findViewById(R.id.stop);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            helpFrom = bundle.getString("from");
            helpto = bundle.getString("helpTo");
            if (helpFrom != null) {
                FirebaseDatabase.getInstance().getReference("help_requests").child(helpFrom).setValue(new HelpRequest(helpFrom, user.getUid()));
            }

        if (helpFrom != null){
            FirebaseDatabase.getInstance().getReference("help_requests").child(helpFrom)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HelpRequest helpRequest = dataSnapshot.getValue(HelpRequest.class);
                            if (helpRequest.getLatFrom() != 0 && helpRequest.getLatUser() != 0){
                                Log.d("sos","latFrom: "+helpRequest.getLatFrom()+" and latUser: "+ helpRequest.getLatUser());

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }else{
            FirebaseDatabase.getInstance().getReference("help_requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HelpRequest helpRequest = dataSnapshot.getValue(HelpRequest.class);
                            if (helpRequest.getLatFrom() != 0 && helpRequest.getLatUser() != 0) {
                                Log.d("sos", "latFrom: " + helpRequest.getLatFrom() + " and latUser: " + helpRequest.getLatUser());
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }





        }


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HelpActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);

                } else {
                    startLocationService();
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationService();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            helpFrom = bundle.getString("from");
            helpto = bundle.getString("helpTo");
        }
        final RelativeLayout relativeLayout = findViewById(R.id.relativelayout);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relativeLayout.getVisibility() == View.INVISIBLE){
                    relativeLayout.setVisibility(View.VISIBLE);
                }else {
                    relativeLayout.setVisibility(View.INVISIBLE);
                }


            }
        });





        recyclerView = findViewById(R.id.chat_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final EditText messageET = findViewById(R.id.sendmessage);

        ImageView send  = findViewById(R.id.sendbutt23);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageET.getText().toString();
                messageET.setText("");
                stockMessage(message);
            }
        });

        populateRecyclerView();


    }

    private void populateRecyclerView() {
        if (helpFrom == null){
            mMessages.clear();
            FirebaseDatabase.getInstance().getReference("HelpChat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"_"+helpto)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot item: dataSnapshot.getChildren()){
                                Message message = item.getValue(Message.class);
                                mMessages.add(message);
                                recyclerView.setAdapter(new AdapterMessage(getApplicationContext(),mMessages));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }else{
            mMessages.clear();
            FirebaseDatabase.getInstance().getReference("HelpChat").child(helpFrom+"_"+FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot item: dataSnapshot.getChildren()){
                                Message message = item.getValue(Message.class);
                                mMessages.add(message);
                                recyclerView.setAdapter(new AdapterMessage(getApplicationContext(),mMessages));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }
    }

    private void stockMessage(String message) {
        if (helpFrom == null){
            FirebaseDatabase.getInstance().getReference("HelpChat")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"_"+helpto)
                    .push().setValue(new Message(message,FirebaseAuth.getInstance().getCurrentUser().getUid(),new Date().getTime()));
        }else {
            FirebaseDatabase.getInstance().getReference("HelpChat")
                    .child(helpFrom+"_"+FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .push().setValue(new Message(message,FirebaseAuth.getInstance().getCurrentUser().getUid(),new Date().getTime()));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location Service started", Toast.LENGTH_SHORT).show();

        }
    }

    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location Service stoped", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (helpFrom != null){
            FirebaseDatabase.getInstance().getReference("help_requests").child(helpFrom)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HelpRequest helpRequest = dataSnapshot.getValue(HelpRequest.class);
                            if (helpRequest.getLatFrom() != 0 && helpRequest.getLatUser() != 0){
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(helpRequest.getLatUser(),helpRequest.getLongtUser()))
                                        .title(helpRequest.getTo())
                                        .snippet("SOS position"));


                                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(helpRequest.getLatFrom(),helpRequest.getLongtFrom()))
                                .title(helpRequest.getFrom()).snippet("helper position"));


                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(helpRequest.getLatUser(),helpRequest.getLongtUser())));
                                mMap.setMinZoomPreference(15);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }else {
            FirebaseDatabase.getInstance().getReference("help_requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HelpRequest helpRequest = dataSnapshot.getValue(HelpRequest.class);
                            if (helpRequest.getLatFrom() != 0 && helpRequest.getLatUser() != 0){
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(helpRequest.getLatUser(),helpRequest.getLongtUser()))
                                        .title(helpRequest.getTo())
                                        .snippet("SOS position"));


                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(helpRequest.getLatFrom(),helpRequest.getLongtFrom()))
                                        .title(helpRequest.getFrom()).snippet("helper position"));


                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(helpRequest.getLatUser(),helpRequest.getLongtUser())));
                                mMap.setMinZoomPreference(15);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.RECEIVE_LOCATION)) {
                Bundle bundle = intent.getExtras();
                latitude = bundle.getDouble("latitude");
                longitude = bundle.getDouble("longitude");
                //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                //mMap.setMinZoomPreference(15);
                if (helpFrom != null) {
                    FirebaseDatabase.getInstance().getReference("help_requests").child(helpFrom)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    HelpRequest request = dataSnapshot.getValue(HelpRequest.class);
                                    request.setLatUser(latitude);
                                    request.setLongtUser(longitude);
                                    FirebaseDatabase.getInstance().getReference("help_requests").child(helpFrom)
                                            .setValue(request);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
                else{
                    FirebaseDatabase.getInstance().getReference("help_requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    HelpRequest helpRequest = dataSnapshot.getValue(HelpRequest.class);
                                    helpRequest.setLongtFrom(longitude);
                                    helpRequest.setLatFrom(latitude);
                                    FirebaseDatabase.getInstance().getReference("help_requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(helpRequest);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }
        }
    };
}




