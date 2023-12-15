/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.UniqueIdentifiersHarvester;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;


public class CollectActivity extends AppCompatActivity {
    private final int ConstPermissionsRequestID = 23;
    private final String[] ConstPermissionsList =
            {
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
    private TextView LabelInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        VisualsInit();
    }

    private void VisualsInit() {
        this.setTitle(R.string.app_name);
        this.LabelInfo = this.findViewById(R.id.textView2);
        this.LabelInfo.setText(R.string.labelinfo_waiting_permission);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!PermissionsCheckAll()) {
            ActivityCompat.requestPermissions(this, this.ConstPermissionsList, this.ConstPermissionsRequestID);
            return;
        }

        HarvestStart();
    }

    private Boolean PermissionsCheckAll(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }

        for (String s : this.ConstPermissionsList){
            if ( ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED ) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != this.ConstPermissionsRequestID){
            return;
        }

        if (grantResults.length <= 0){
            Terminate();
            return;
        }

        if (permissions.length != grantResults.length){
            Terminate();
            return;
        }

        for (int i = 0; i < permissions.length; i++){
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                Terminate();
                return;
            }
        }

        // if there are more than 1 permission requested some androids restart the activity after onRequestPermissionsResult call
        HarvestStart();
    }

    private void Terminate(){
        this.LabelInfo.setText( R.string.labelinfo_no_permission );
        this.finishAffinity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // NOP
    }

    private void HarvestStart(){
        HarvesterThread ht = new HarvesterThread();
        ht.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    protected class HarvesterThread extends AsyncTask<Void, Void, UniqueIDHarvesterDataClass> {
        protected void onPreExecute() {
            CollectActivity.this.LabelInfo.setText( R.string.labelinfo_collecting );
        }

        @Override
        protected UniqueIDHarvesterDataClass doInBackground(Void... voids) {
            UniqueIDHarvesterDataClass data = new UniqueIDHarvesterDataClass();
            UniqueIDHarvesterClass.AllCollect( CollectActivity.this, data );

            return data;
        }

        protected void onPostExecute(UniqueIDHarvesterDataClass data) {
            CollectActivity.this.LabelInfo.setText( R.string.labelinfo_done );
            Intent IntentNew = new Intent(CollectActivity.this, MainActivity.class);
            IntentNew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            IntentNew.putExtra(GlobalConstsClass.DataHarvestedIntentName, data);
            startActivity(IntentNew);
        }
    }
}
