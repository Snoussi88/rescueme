package com.snoussi.univox;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.TextView;
import android.widget.Toast;

public class AddressResultReceiver extends ResultReceiver {
    public TextView addressView;

    AddressResultReceiver(Handler handler, TextView addressView) {
        super(handler);
        this.addressView = addressView;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (resultCode==Constants.SUCCESS_RESULT){
            addressView.setText(resultData.getString(Constants.RESULT_DATA_KEY));
        }
    }
}
