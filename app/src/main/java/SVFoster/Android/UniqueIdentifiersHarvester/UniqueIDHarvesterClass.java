/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.UniqueIdentifiersHarvester;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class UniqueIDHarvesterClass {
    public static void AllCollect( Context ContextCaller, UniqueIDHarvesterDataClass data ){
        AndroidIDsGet(ContextCaller, data );
        GoogleIDsGet(ContextCaller, data );
        CallNetworkIDsGet(ContextCaller, data );
        NICIDsGet(ContextCaller, data );
        NonUniqGet(ContextCaller, data );
    }

    //
    // OS and hardware
    //
    public static void AndroidIDsGet( Context context, UniqueIDHarvesterDataClass data ){
        AndroidAndroidIDWay1Get(context, data);
        AndroidSerialNoWay1Get(data);
        AndroidSerialNoWay2Get(data);
        AndroidSerialNoWay3Get(data);
        AndroidSerialNoWay4Get(data);
        AndroidHostnameWay1Get(data);
        AndroidHostnameWay2Get(data);
        AndroidHostnameWay3Get(data);
        //AndroidFingerprintedPartitionsWay1Get(TextOut);
    }

    public static void AndroidAndroidIDWay1Get(Context context, UniqueIDHarvesterDataClass data) {
        data.AndroidID_way1 = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        data.AndroidID_way1_status = UniqueIDHarvesterDataClass.ConstStatusOK;
    }

    public static void AndroidSerialNoWay1Get(UniqueIDHarvesterDataClass data) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);

            data.SerialNo_way1 = (String) (get.invoke(c, "ro.serialno", "unknown"));
            data.SerialNo_way1_status = UniqueIDHarvesterDataClass.ConstStatusOK;

        } catch (Exception ignored) {
            data.SerialNo_way1_status = UniqueIDHarvesterDataClass.ConstStatusError;
        }
    }

    public static void AndroidSerialNoWay2Get(UniqueIDHarvesterDataClass data) {
        try {
            Class myclass = Class.forName("android.os.SystemProperties");
            Method[] methods = myclass.getMethods();
            Object[] params = new Object[]{
                    new String("ro.serialno"),
                    new String("Unknown")
            };

            data.SerialNo_way2 = (String) (methods[2].invoke(myclass, params));
            data.SerialNo_way2_status = UniqueIDHarvesterDataClass.ConstStatusOK;
        } catch (Exception ignored) {
            data.SerialNo_way2_status = UniqueIDHarvesterDataClass.ConstStatusError;
        }
    }

    public static void AndroidSerialNoWay3Get(UniqueIDHarvesterDataClass data) {
        data.SerialNo_way3 = Build.SERIAL;
        data.SerialNo_way3_status = UniqueIDHarvesterDataClass.ConstStatusOK;
    }

    public static void AndroidSerialNoWay4Get(UniqueIDHarvesterDataClass data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            data.SerialNo_way4_status = UniqueIDHarvesterDataClass.ConstStatusErrorNotSupported;
            return;
        }

        data.SerialNo_way4 = Build.getSerial();
        data.SerialNo_way4_status = UniqueIDHarvesterDataClass.ConstStatusOK;
    }

    public static void AndroidHostnameWay1Get(UniqueIDHarvesterDataClass data) {
        try {
            InetAddress netHost = InetAddress.getLocalHost();

            data.Hostname_way1 = netHost.getHostName();
            data.Hostname_way1_status = UniqueIDHarvesterDataClass.ConstStatusOK;
        } catch (Exception ignored) {
            data.Hostname_way1_status = UniqueIDHarvesterDataClass.ConstStatusError;
        }
    }

    public static void AndroidHostnameWay2Get(UniqueIDHarvesterDataClass data) {
        String BufferString = "";
        LineNumberReader output = null;

        try {
            output = CLICall( "uname -n" );

            for (; null != BufferString; ) {
                BufferString = output.readLine();
                if (BufferString != null) {
                    data.Hostname_way2 = BufferString.trim();
                    data.Hostname_way2_status = UniqueIDHarvesterDataClass.ConstStatusOK;
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            data.Hostname_way2_status = UniqueIDHarvesterDataClass.ConstStatusError;
        }
    }

    public static void AndroidHostnameWay3Get(UniqueIDHarvesterDataClass data) {
        String BufferString = "";
        LineNumberReader output = null;

        try {
            output = CLICall( "cat /proc/sys/kernel/hostname" );

            for (; null != BufferString; ) {
                BufferString = output.readLine();
                if (BufferString != null) {
                    data.Hostname_way3 = BufferString.trim();
                    data.Hostname_way3_status = UniqueIDHarvesterDataClass.ConstStatusOK;
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            data.Hostname_way3_status = UniqueIDHarvesterDataClass.ConstStatusError;
        }
    }

    //
    // Google
    //
    public static void GoogleIDsGet( Context context, UniqueIDHarvesterDataClass data ){
        GoogleGSFWay1Get(context, data);
        GoogleADIDWay1Get(context, data);
    }

    public static void GoogleADIDWay1Get(Context context, UniqueIDHarvesterDataClass data) {
        String BufferString = null;
        AdvertisingIdClient.Info idInfo = null;
        data.ADID_way1_status = UniqueIDHarvesterDataClass.ConstStatusError;

        try {
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context.getApplicationContext());

        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            BufferString = idInfo.getId();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        data.ADID_way1 = BufferString;
        data.ADID_way1_status = UniqueIDHarvesterDataClass.ConstStatusOK;
    }

    private static final Uri sUri = Uri.parse("content://com.google.android.gsf.gservices");
    public static void GoogleGSFWay1Get(Context context, UniqueIDHarvesterDataClass data) {
        String BufferString = null;
        Cursor query = null;
        data.GSF_way1_status = UniqueIDHarvesterDataClass.ConstStatusError;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            query = context.getContentResolver().query(sUri, null, null, new String[] { "android_id" }, null);
            if (query == null) {
                data.GSF_way1_status = UniqueIDHarvesterDataClass.ConstStatusErrorNotSupported;
                return;
            }
            if (!query.moveToFirst() || query.getColumnCount() < 2) {
                data.GSF_way1_status = UniqueIDHarvesterDataClass.ConstStatusErrorNotSupported;
                return;
            }
            BufferString = Long.toHexString(Long.parseLong(query.getString(1)));
            data.GSF_way1 = BufferString.toUpperCase().trim();
            data.GSF_way1_status = UniqueIDHarvesterDataClass.ConstStatusOK;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        finally {
            if ( query!= null ) {
                query.close();
            }
        }
    }


    //
    // Cell modem and SIM cards
    //
    public static void CallNetworkIDsGet( Context context, UniqueIDHarvesterDataClass data ){
        CallNetworkSIMCountWay1Get(context, data);
        CallNetworkIMEIListWay1Get(context, data);
        CallNetworkIMEIListWay2Get(context, data);
        CallNetworkSIMListWay1Get(context, data);
    };

    public static void CallNetworkSIMCountWay1Get( Context context, UniqueIDHarvesterDataClass data ){
        SubscriptionManager sm = SubscriptionManager.from(context);

        data.SIMCount_way1 = sm.getActiveSubscriptionInfoCountMax();
        data.SIMCount_way1_status = UniqueIDHarvesterDataClass.ConstStatusOK;
    }

    public static void CallNetworkIMEIListWay1Get( Context context, UniqueIDHarvesterDataClass data ){
        if ( data.SIMCount_way1_status != UniqueIDHarvesterDataClass.ConstStatusOK ){
            data.IMEIList_way1_status = UniqueIDHarvesterDataClass.ConstStatusErrorDependency;
            return;
        }

        String BufferString = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        data.IMEIList_way1_status =  UniqueIDHarvesterDataClass.ConstStatusOK;

        for ( int i = 0; i < data.SIMCount_way1; i++ ){
            BufferString = telephonyManager.getDeviceId(i);
            data.IMEIList_way1Add(i, BufferString);
        };
    }

    public static void CallNetworkIMEIListWay2Get( Context context, UniqueIDHarvesterDataClass data ){
        if ( data.SIMCount_way1_status != UniqueIDHarvesterDataClass.ConstStatusOK ){
            data.IMEIList_way2_status = UniqueIDHarvesterDataClass.ConstStatusErrorDependency;
            return;
        }

        String BufferString = null;
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        data.IMEIList_way2_status =  UniqueIDHarvesterDataClass.ConstStatusOK;

        for ( int i = 0; i < data.SIMCount_way1; i++ ){
            BufferString = telephonyManager.getImei(i);
            data.IMEIList_way2Add(i, BufferString);
        };
    }

    public static void CallNetworkSIMListWay1Get( Context context, UniqueIDHarvesterDataClass data ){
        if ( data.SIMCount_way1_status != UniqueIDHarvesterDataClass.ConstStatusOK ){
            data.SIMList_way1_status = UniqueIDHarvesterDataClass.ConstStatusErrorDependency;
            return;
        }

        String BufferString1, BufferString2 = null;
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        data.SIMList_way1_status = UniqueIDHarvesterDataClass.ConstStatusOK;

        BufferString1 = telephonyManager.getSubscriberId();
        BufferString2 = telephonyManager.getSimSerialNumber();
        data.SIMList_way1Add(0, BufferString1, BufferString2);
    }


    //
    // Network
    //
    public static void NICIDsGet( Context context, UniqueIDHarvesterDataClass data ) {
        NICIDsWay1Get( context, data );
    }

    public static void NICIDsWay1Get( Context context, UniqueIDHarvesterDataClass data ) {
        data.NICList_way1_status = UniqueIDHarvesterDataClass.ConstStatusOK;

        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    continue;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b).toUpperCase());
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }

                data.NICList_way1Add(nif.getName(), res1.toString());
            }
        } catch (Exception ex) {
            data.NICList_way1_status = UniqueIDHarvesterDataClass.ConstStatusError;
        }
    }


    //
    // Not unique
    //
    public static void NonUniqGet( Context context, UniqueIDHarvesterDataClass data ){
        NonUniqFingerprintWay1Get( context, data );
    }

    public static void NonUniqFingerprintWay1Get( Context context, UniqueIDHarvesterDataClass data ){
        data.BuildFingerprint_way1 = Build.FINGERPRINT;
        data.BuildFingerprint_way1_status = UniqueIDHarvesterDataClass.ConstStatusOK;
    }


    //
    // Support functions
    //
    private static LineNumberReader CLICall(String callstr) throws IOException {
        Process pp = Runtime.getRuntime().exec( callstr );
        InputStreamReader isr = new InputStreamReader(pp.getInputStream());
        return new LineNumberReader(isr);
    }
}
