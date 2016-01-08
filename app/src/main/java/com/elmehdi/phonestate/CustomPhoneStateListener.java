package com.elmehdi.phonestate;

import android.telephony.PhoneStateListener;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

public class CustomPhoneStateListener extends PhoneStateListener {
    private final Context mContext;
    public static String LOG_TAG = "com.elmehdi.phonestate.CustomPhoneStateListener";

    public CustomPhoneStateListener(Context context) {
        mContext = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        CharSequence text = "";
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_IDLE");
                text = "CALL_STATE_IDLE";
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_RINGING");
                text = "CALL_STATE_RINGING";
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_OFFHOOK");
                text = "CALL_STATE_OFFHOOK";
                break;
            default:
                Log.i(LOG_TAG, "UNKNOWN_STATE: " + state);
                text = "UNKNOWN_STATE: " + state;
                break;
        }
        Toast.makeText(this.mContext, text, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onCallForwardingIndicatorChanged(boolean cfi) {
        super.onCallForwardingIndicatorChanged(cfi);
        Log.i(LOG_TAG, "onCallForwardingIndicatorChanged: " + cfi);
    }
}