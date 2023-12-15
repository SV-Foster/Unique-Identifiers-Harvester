/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.UniqueIdentifiersHarvester;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SaveToUniversalDialog.Listener{
    private UniqueIDHarvesterDataClass DataHarvested = null;
    private RecyclerView RecyclerView1 = null;
    private DrawerLayout Drawer1 = null;
    private NavigationView NavigationView1 = null;
    private UnattendedJobClass UnattendedJob = null;
    private int UnattendedJobStepsDone = 0;
    private TextView NavHL1 = null;
    private TextView NavHL2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.UnattendedJob = UnattendedJobClass.InstructionsLoad(PathsClass.SDLastDataPathGet(this));
        VisualsInit();
        DataGetToShow();
        DataToInterface();
    }

    private void VisualsInit(){
        this.setTitle( R.string.app_name );
        Drawer1 = this.findViewById(R.id.DrawerLayout1);
        NavigationView1 = this.findViewById(R.id.nav_view);
        NavigationView1.setNavigationItemSelectedListener(this);
        RecyclerView1 = this.findViewById(R.id.RecyclerView1);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        RecyclerView1.setLayoutManager(llm);
        RecyclerView1.setHasFixedSize(true);
        ActionBarDrawerToggle tgl =
                new ActionBarDrawerToggle
                (
                        this,
                    Drawer1,
                    findViewById(androidx.appcompat.R.id.action_bar),
                    R.string.hint_menubutton_open,
                    R.string.hint_menubutton_close
                );
        Drawer1.addDrawerListener( tgl );
        tgl.syncState();

        View header = NavigationView1.getHeaderView(0);
        NavHL1 = header.findViewById(R.id.nav_header_line1);
        NavHL2 = header.findViewById(R.id.nav_header_line2);
        NavHL1.setText( ProgramVersionClass.CopyrightTextGet( false ));
        NavHL2.setText( GlobalConstsClass.AuthorEmail);
    }

    private void DataToInterface() {
        List<UniqueIDHarvesterDataConvertorClass.DataHarvestedToArrayClass> DataShowArray =
                UniqueIDHarvesterDataConvertorClass.ToListOfStringsWithComments( this.DataHarvested );

        RecyclerViewAdapterClass RecyclerView1Adapter =
                new RecyclerViewAdapterClass(DataShowArray);

        RecyclerView1.setAdapter(RecyclerView1Adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        if ( this.UnattendedJob.JobType == UnattendedJobClass.ConstJobTypeUnattended ) {
            UnattendedJobStart();
        }
    }

    private void UnattendedJobStart() {
        if ( this.UnattendedJob.SaveToFile ){
            String FileFullPath;
            switch (this.UnattendedJob.FileNameType){
                case UnattendedJobClass.ConstFileNamePreset:
                    FileFullPath = this.UnattendedJob.FileFolderPreset + this.UnattendedJob.FileNamePreset;
                    break;

                case UnattendedJobClass.ConstFileNameAutoFolder:
                    FileFullPath = PathsClass.SDLastDataPathGet(this) + this.UnattendedJob.FileNamePreset;
                    break;

                default: // UnattendedJobClass.ConstFileNameAutoFull
                    FileFullPath = this.DataXMLFileAutoNameGet();
            }

            DataSaveToFile( FileFullPath );
        }

        if ( this.UnattendedJob.CopyToClipboard ){
            DataSaveToClipboard();
        }

        if ( this.UnattendedJob.UploadToHTTP ){
            new SaveToHTTP(UnattendedJob.UploadURL, UniqueIDHarvesterDataConvertorClass.ToXML(this.DataHarvested)).execute();
        }

    }

    private void UnattendedJobStepDone(){
        if ( !this.UnattendedJob.AutoCloseApp ){
            return;
        }

        this.UnattendedJobStepsDone++;

        if ( this.UnattendedJobStepsDone < this.UnattendedJob.StepsCountGet( ))
        {
            return;
        }

        this.finishAffinity();
    }

    @Override
    protected void onStop() {
        DataSaveToFileSerialized();

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (this.Drawer1.isDrawerOpen(GravityCompat.START)) {
            this.Drawer1.closeDrawer(GravityCompat.START);
            return;
        }

        this.finishAffinity();
        super.onBackPressed();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (this.Drawer1.isDrawerOpen(GravityCompat.START)) {
                this.Drawer1.closeDrawer(GravityCompat.START);
            } else {
                this.Drawer1.openDrawer(GravityCompat.START);}

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        this.Drawer1.closeDrawer(GravityCompat.START);

        // Resource IDs will be non-final by default in Android Gradle Plugin version 8.0...
        int itemId = item.getItemId();
        if (itemId == R.id.SaveToFile) {
            DataSaveToXMLFile();
            return true;
        }

        if (itemId == R.id.SaveToClipboard) {
            DataSaveToClipboard();
            return true;
        }

        if (itemId == R.id.SaveToHttp) {
            DataSaveToHTTPPOST();
            return true;
        }

        if (itemId == R.id.Help) {
            Intent IntentNew = new Intent(this, AboutActivity.class);
            startActivity(IntentNew);
            return true;
        }

        if (itemId == R.id.ExitApp) {
            this.finishAffinity();
            return true;
        }

        return false;
    }


    //
    //  Save the data for an another app or a human
    //

    private static final int SaveDialogIDFile = 1;
    private static final int SaveDialogIDHTTP = 2;

    private void DataSaveToXMLFile(){
        String FilePathFull = DataXMLFileAutoNameGet();
        SaveToUniversalDialog fsd1 = new SaveToUniversalDialog(SaveDialogIDFile, FilePathFull, getString(R.string.saveto_dialog_hint_file),getString(R.string.saveto_dialog_title_file));
        fsd1.show(getSupportFragmentManager(), "brraq8xwfq");
    }

    private String DataXMLFileAutoNameGet(){
        String FilePathFull = PathsClass.SDLastDataPathGet(this);
        SimpleDateFormat sdf = new SimpleDateFormat(GlobalConstsClass.SDOutputFileNameDateTime, Locale.getDefault());
        FilePathFull += GlobalConstsClass.SDOutputFileNamePrefix + sdf.format(new Date()) + GlobalConstsClass.SDOutputFileNameExtension;

        return FilePathFull;
    }

    private void DataSaveToClipboard(){
        ClipData clip = ClipData.newPlainText(this.getResources().getString(R.string.app_name), UniqueIDHarvesterDataConvertorClass.ToXML(this.DataHarvested));
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.hint_copied_to_clipboard, Toast.LENGTH_LONG).show();
        UnattendedJobStepDone();
    }

    private void DataSaveToHTTPPOST(){
        SaveToUniversalDialog fsd2 = new SaveToUniversalDialog(SaveDialogIDHTTP, GlobalConstsClass.HTTPOutputDefaultURL, getString(R.string.saveto_dialog_hint_http),getString(R.string.saveto_dialog_title_http));
        fsd2.show(getSupportFragmentManager(), "4M6RMzhzUT");
    }

    @Override
    public void SaveToUnvDialogCallback(int ID, String PathEntered) {
        switch (ID) {
            case SaveDialogIDFile:
                DataSaveToFile( PathEntered );

                break;

            case SaveDialogIDHTTP:
                new SaveToHTTP(PathEntered, UniqueIDHarvesterDataConvertorClass.ToXML(this.DataHarvested)).execute();

                break;
        }
    }

    private void DataSaveToFile( String FileFullPath ){
        StringWriteToFile
        (
            new File( FileFullPath ),
            UniqueIDHarvesterDataConvertorClass.ToXML(this.DataHarvested)
        );

        UnattendedJobStepDone();
    }

    private void StringWriteToFile(File myFile, String data) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myFile);
            fileOutputStream.write(data.getBytes());
            Toast.makeText(this, getString(R.string.hint_saved_file_to) + myFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.hint_file_save_error) + myFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class SaveToHTTP extends AsyncTask<Void, Void, Boolean> {
        private String URLToPOST;
        private String StringToPost;
        public final MediaType XML = MediaType.get("application/xml; charset=utf-8");

        public SaveToHTTP(String URLToPOST, String StringToPost){
            this.URLToPOST = URLToPOST;
            this.StringToPost = StringToPost;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(MainActivity.this, R.string.hint_http_seding, Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(this.StringToPost, this.XML);

            try {
                Request request = new Request.Builder()
                    .url(this.URLToPOST)
                    .post(body)
                    .build();

                client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);

            if (s){
                Toast.makeText(MainActivity.this, R.string.hint_http_sent, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, R.string.hint_http_error, Toast.LENGTH_LONG).show();
            }

            UnattendedJobStepDone();
        }
    }


    //
    //  Data object IO for activity needs
    //

    private void DataGetToShow(){
        this.DataHarvested = (UniqueIDHarvesterDataClass)getIntent().getSerializableExtra( GlobalConstsClass.DataHarvestedIntentName );
        if ( this.DataHarvested != null ){
            return;
        }

        if ( DataLoadFromFileSerialized()){
            return;
        }

        this.DataHarvested = new UniqueIDHarvesterDataClass();
    }

    private static final String DataFileName = "data-latest.ser";
    private boolean DataSaveToFileSerialized() {
        ObjectOutputStream oos = null;

        try {
            FileOutputStream fos = this.openFileOutput(DataFileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.DataHarvested);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private boolean DataLoadFromFileSerialized(){
        ObjectInputStream is = null;

        try
        {
            FileInputStream fis = this.openFileInput(DataFileName);
            is = new ObjectInputStream(fis);
            Object readObject = is.readObject();

            if ( !(readObject instanceof UniqueIDHarvesterDataClass))
            {
                return false;
            }

            this.DataHarvested = (UniqueIDHarvesterDataClass)readObject;
            return true;

        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally {
            try
            {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

}
