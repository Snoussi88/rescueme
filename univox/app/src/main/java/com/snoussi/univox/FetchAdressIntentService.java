package com.snoussi.univox;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FetchAdressIntentService extends IntentService {
    private ResultReceiver resultReceiver;

    public FetchAdressIntentService() {
        super("FetchAdressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            String errorMessage = "";
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            if (location != null){
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());


            List<Address> addresses = null;
            try{
                addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            }catch (Exception exception){
                errorMessage = exception.getMessage();
            }

            if (addresses == null || addresses.isEmpty()){
                deliverResultToReceiver(Constants.FAILURE_RESULT,errorMessage);

            }else{
                Address adress = addresses.get(0);
                ArrayList<String> adressFragments = new ArrayList<>();
                for (int i=0; i<=adress.getMaxAddressLineIndex();i++){
                    adressFragments.add(adress.getAddressLine(i));
                }
                deliverResultToReceiver(Constants.SUCCESS_RESULT,
                        TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator"))
                        ,adressFragments));
            }

        }

    }
    private void deliverResultToReceiver(int resultcode,String adressMessage){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY,adressMessage);
        resultReceiver.send(resultcode,bundle);

    }
}
