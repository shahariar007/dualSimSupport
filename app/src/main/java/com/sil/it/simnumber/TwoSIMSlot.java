package com.sil.it.simnumber;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

public class TwoSIMSlot {
    Context context;
    public static String TAG = "HHHH";

    public TwoSIMSlot(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public void generate() {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getFirstMethod = telephonyClass.getMethod("getDefault", parameter);

            Log.d(TAG, getFirstMethod.toString());

            Object[] obParameter = new Object[1];
            obParameter[0] = 0;
            TelephonyManager first = (TelephonyManager) getFirstMethod.invoke(null, obParameter);

            Log.d(TAG, "Device Id: " + first.getDeviceId() + ", device status: " + first.getSimState() + ", operator: " + first.getNetworkOperator() + "/" + first.getSimSerialNumber());

            obParameter[0] = 1;
            TelephonyManager second = (TelephonyManager) getFirstMethod.invoke(null, obParameter);

            Log.d(TAG, "Device Id: " + second.getDeviceId() + ", device status: " + second.getSimState() + ", operator: " + second.getNetworkOperator() + "/" + second.getSimSerialNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private static String[] simStatusMethodNames = {"getSimStateGemini", "getSimState"};
//
//
//    public static boolean hasTwoActiveSims(Context context) {
//        boolean first = false, second = false;
//
//        for (String methodName : simStatusMethodNames) {
//            // try with sim 0 first
//            try {
//                first = getSIMStateBySlot(context, methodName, 0);
//                // no exception thrown, means method exists
//                second = getSIMStateBySlot(context, methodName, 1);
//                return first && second;
//            } catch (GeminiMethodNotFoundException e) {
//                // method does not exist, nothing to do but test the next
//            }
//        }
//        return false;
//    }
}
