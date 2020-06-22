package com.snoussi.univox;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;


import static android.os.Build.VERSION_CODES.M;

public class OsmMapActivity extends AppCompatActivity {
    private static final int PERMISSAO_REQUIERED = 1;
    private MapView mapView;
    private String apiKey = "5b3ce3597851110001cf624872047dadd4424c05aa0afe5a7f8fc175";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.osm_map);

        if (Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissoes = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissoes, PERMISSAO_REQUIERED);
            }
        }

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        mapView = findViewById(R.id.mapOsm);

        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);





        mapView.setMultiTouchControls(true);
        mapView.setClickable(true);
        mapView.setHorizontalMapRepetitionEnabled(false);
        mapView.setVerticalMapRepetitionEnabled(false);

        IMapController mapController = mapView.getController();


        //for drawing lines from point A to point B

        //34.040125, -4.993656
        //to
        //34.040814, -4.996054

        GeoPoint gPt0 = new GeoPoint(34.040125d, -4.993656);
        addMarker(gPt0);
        GeoPoint gPt1 = new GeoPoint(34.040814d, -4.996054d);
        addMarker(gPt1);
        addMarker(gPt0);
        Polyline line = new Polyline(mapView);
        line.addPoint(gPt0);
        line.addPoint(gPt1);
        mapView.getOverlays().add(line);
        mapController.setCenter(gPt0);
        mapController.setZoom(17.0);


        findDirections(gPt0,gPt1);



    }

    private void addMarker(GeoPoint center){
        Marker marker = new Marker(mapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.ic_home_black_24dp));
        mapView.getOverlays().clear();
        mapView.getOverlays().add(marker);
        mapView.invalidate();
        marker.setTitle("wiii3, elach khla9it fhad lbleeeeeeeed");}

    private void findDirections(GeoPoint start,GeoPoint end)  {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openrouteservice.org/v2/directions/" +
                "driving-car?api_key=5b3ce3597851110001cf624872047dadd4424c05aa0afe5a7f8fc175&";
        String start_lat = ""+start.getLatitude();
        String start_longt = ""+start.getLongitude();

        String end_lat = ""+end.getLatitude();
        String end_longt = ""+end.getLongitude();

        url = url + "start="+ start_longt+","+start_lat+"&end="+end_longt+","+end_lat;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sos",response);


                KmlDocument kmlDocument = new KmlDocument();
                kmlDocument.parseGeoJSON(response);

                Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);
                Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
                Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 5f, 0x20AA1010);
                FolderOverlay geoJsonOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, defaultStyle, null, kmlDocument);
                mapView.getOverlays().add(geoJsonOverlay);
                mapView.invalidate();
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
                queue.add(stringRequest);



    }
}
