package com.sil.it.simnumber;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView txtSimNumber;
    private Button btnSimNumber;
    public int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;
    private TextView txtSecureNumber;

    private void initViews() {
        txtSecureNumber = (TextView) findViewById(R.id.txtSecureNumber);
        txtSimNumber = (TextView) findViewById(R.id.txtSimNumber);
        btnSimNumber = (Button) findViewById(R.id.btnSimNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        btnSimNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grantPermission();
            }
        });
    }

    public void grantPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);

        } else {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    goBD();
                } else {
                    simNumberGenerate();
                }
                demoCall();
                // new TwoSIMSlot(this).generate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public void goBD() {
        StringBuffer sb = new StringBuffer();
        final SubscriptionManager subscriptionManager = SubscriptionManager.from(this);
        final List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
            final CharSequence carrierName = subscriptionInfo.getCarrierName();
            final CharSequence displayName = subscriptionInfo.getDisplayName();
            final int mcc = subscriptionInfo.getMcc();
            final int mnc = subscriptionInfo.getMnc();
            final String subscriptionInfoNumber = subscriptionInfo.getNumber();
            Log.d("TAGA", subscriptionInfo.getSimSlotIndex() + "==getSimSlotIndex");
            Log.d("TAGA", carrierName + "==carrierName");
            Log.d("TAGA", displayName + "==displayName");
            Log.d("TAGA", mcc + "==mcc");
            Log.d("TAGA", mnc + "==mnc");
            Log.d("TAGA", subscriptionInfoNumber + "=subscriptionInfoNumber");
            Log.d("TAGA", subscriptionInfo.getIccId() + "=getIccId");
            sb.append("Sim Number :" + subscriptionInfo.getIccId() + "\n" + "Network Operator :" + mcc + mnc + "\n");

        }
        txtSecureNumber.setText(sb);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            demoCall();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                goBD();
            } else {
                simNumberGenerate();
            }


        } else {

            Toast.makeText(this, "TRY AGAIN", Toast.LENGTH_SHORT).show();
        }
        return;

    }

    @SuppressLint("MissingPermission")
    private void demoCall() {
        TelephonyManager telephonyManager = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        txtSimNumber.setText("Default:" + telephonyManager.getSimSerialNumber() + "/" + telephonyManager.getNetworkOperator());

        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //txtSecureNumber.setText("secure ID :" + android_id);
    }

    public void simNumberGenerate() {
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);

        String imeiSIM1 = telephonyInfo.getImsiSIM1();
        String imeiSIM2 = telephonyInfo.getImsiSIM2();
        String imeiSIM1NUM = telephonyInfo.isSIM1Numbr();
        String imeiSIM2NUM = telephonyInfo.isSIM2Numbr();

        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

        boolean isDualSIM = telephonyInfo.isDualSIM();

        TextView tv = (TextView) findViewById(R.id.txtSecureNumber);
        tv.setText(" SIM NO:1 : " + imeiSIM1 + "\n" +
                " SIM NO:2 : " + imeiSIM2 + "\n" +
                " IS DUAL SIM : " + isDualSIM + "\n" +
                " IS SIM1 READY : " + isSIM1Ready + "\n" +
                " IS SIM2 READY : " + isSIM2Ready + "\n" +
                " Network 1 Code: " + imeiSIM1NUM + "\n" +
                " Network 2 Code: : " + imeiSIM2NUM + "\n");
    }
}
