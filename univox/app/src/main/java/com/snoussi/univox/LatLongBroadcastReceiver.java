package com.snoussi.univox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;

public class LatLongBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        if (b != null){
            Double latitude = b.getDouble("latitude");
            Double longitude = b.getDouble("longitude");

            Intent intent1 = new Intent(Constants.RECEIVE_LOCATION);
            intent1.putExtra("latitude",latitude);
            intent1.putExtra("longitude",longitude);
            context.sendBroadcast(intent1);

            //LatLng from = new LatLng(latitude,longitude);
            //LatLng to = new LatLng();

            //FirebaseDatabase.getInstance().getReference("helpRequests").child()

        }


    }

}
