package com.snoussi.univox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.M;



public class SendPost extends Fragment {
    private EditText msg;
    private DatabaseReference myref;
    private ImageView locationswitch;
    private Spinner spinner;
    private String selectSpin;
    private FirebaseUser user;
    private ArrayList<EmergencyItem> emergencyItems;
    private SpinnerItemAdapter madapter;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    private TextView latitudeText;
    private TextView longtText;
    private Double latitude,longtitude;
    private View view;
    public TextView adressText;
    private MapView mapView;
    private IMapController mapController;
    private static final int PERMISSAO_REQUERIDA = 1;
    ArrayList<OverlayItem> markers;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_send_post, container, false);
        if (Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissoes = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissoes, PERMISSAO_REQUERIDA);
            }
        }

        Configuration.getInstance().load(view.getContext(), PreferenceManager.getDefaultSharedPreferences(view.getContext()));
        myref = FirebaseDatabase.getInstance().getReference("posts");
        msg = view.findViewById(R.id.post_edit);

        user = FirebaseAuth.getInstance().getCurrentUser();

        initList();



        mapView =view.findViewById(R.id.osmMaps);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);



        mapView.setMultiTouchControls(true);
        mapView.setClickable(true);
        mapView.setHorizontalMapRepetitionEnabled(false);
        mapView.setVerticalMapRepetitionEnabled(false);

        mapController = mapView.getController();
        mapController.setZoom(15.0);

        adressText = view.findViewById(R.id.adress_send_fragment);


        latitudeText = view.findViewById(R.id.latitude_send_fragment);
        longtText = view.findViewById(R.id.longtitude_send_fragment);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());


        locationswitch = view.findViewById(R.id.myloc);
        spinner = view.findViewById(R.id.spinner);
        madapter = new SpinnerItemAdapter(view.getContext(), emergencyItems);
        spinner.setAdapter(madapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EmergencyItem clickedItem = (EmergencyItem) parent.getItemAtPosition(position);
                String clickedItemName = clickedItem.getItemName();

                switch (clickedItemName) {
                    case "foster":
                        selectSpin = "foster";
                        break;
                    case "cloths":
                        selectSpin = "cloths";
                        break;
                    case "blood":
                        selectSpin = "blood";
                        break;
                    case "elder":
                        selectSpin = "elder";
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        locationswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });


        view.findViewById(R.id.buttPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String txt = msg.getText().toString();
                String key = myref.push().getKey();
                Post post = new Post(txt, user, 0, 0, latitude, longtitude, selectSpin, userKey,null, key);
                myref.child(key).setValue(post);
            }
        });

        return view;
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData(view);
                                } else {


                                    Location location1 = new Location("providerNA");
                                    location1.setLatitude(location.getLatitude());
                                    location1.setLongitude(location.getLongitude());


                                    latitude = location.getLatitude();
                                    longtitude = location.getLongitude();

                                    addMarker(new GeoPoint(location.getLatitude(),location.getLongitude()));

                                    mapController.setCenter(new GeoPoint(location));

                                    Geocoder geocoder = new Geocoder(view.getContext(),Locale.getDefault());
                                    List<Address> addresses;
                                    try {
                                        addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                                        String address = addresses.get(0).getAddressLine(0);
                                        adressText.setText(address);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }



                                    latitudeText.setText(""+latitude);
                                    longtText.setText(""+longtitude);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(view.getContext(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(View view) {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(4000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setNumUpdates(5);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longtitude = mLastLocation.getLongitude();



            latitudeText.setText(""+latitude);
            longtText.setText(""+longtitude);
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


    private void initList() {
        emergencyItems = new ArrayList<>();
        emergencyItems.add(new EmergencyItem("foster", R.drawable.ic_foster));
        emergencyItems.add(new EmergencyItem("elder", R.drawable.ic_elder));
        emergencyItems.add(new EmergencyItem("blood", R.drawable.ic_money));
        emergencyItems.add(new EmergencyItem("cloths", R.drawable.ic_near_me_black_24dp));

    }

    private void addMarker(GeoPoint center){
        Marker marker = new Marker(mapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.ic_home_black_24dp));
        mapView.getOverlays().clear();
        mapView.getOverlays().add(marker);
        mapView.invalidate();
        marker.setTitle("Casa do Josileudo");}

}




