/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.UniqueIdentifiersHarvester;

import android.os.Environment;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

@Root(name = "UnattendedJob")
public class UnattendedJobClass implements Serializable {
    public final static int ConstDataStructureVersion = 0;
    @Attribute(name = "DataStructureVersion")
    public int DataStructureVersion = ConstDataStructureVersion;

    public static final int ConstJobTypeManual = 1;
    public static final int ConstJobTypeUnattended = 2;
    @Element(name = "JobType")
    public int JobType = ConstJobTypeManual;

    @Element(name = "SaveToFile")
    public Boolean SaveToFile = false;
    public static final int ConstFileNameAutoFull = 1;
    public static final int ConstFileNameAutoFolder = 2;
    public static final int ConstFileNamePreset = 3;
    @Element(name = "FileNameType")
    public int FileNameType = ConstFileNameAutoFull;
    @Element(name = "FileNamePreset")
    public String FileNamePreset = "UIDs harvested.xml";
    @Element(name = "FileFolderPreset")
    public String FileFolderPreset = "/sdcard/";

    @Element(name = "CopyToClipboard")
    public Boolean CopyToClipboard = false;

    @Element(name = "UploadToHTTP")
    public Boolean UploadToHTTP = false;
    @Element(name = "UploadURL")
    public String UploadURL = GlobalConstsClass.HTTPOutputUnattendedURL;

    @Element(name = "AutoCloseApp")
    public Boolean AutoCloseApp = false;

    public final static String UnattendedFileName = "Unattended UIDs.xml";

    public static UnattendedJobClass InstructionsLoad(String FolderPath){
        try {
            Serializer serializer = new Persister();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(UnattendedFilePathFullGet(FolderPath)));
            UnattendedJobClass ujc = serializer.read(UnattendedJobClass.class, bufferedReader);
            if ( ujc.DataStructureVersion != ConstDataStructureVersion ){
                return new UnattendedJobClass();
            }
            return ujc;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new UnattendedJobClass();
    }

    public void InstructionsSave(String FolderPath) {
        Serializer serializer = new Persister();
        StringWriter stringWriter = new StringWriter();
        String StringXML = "";

        // Write the XML to the file provided
        try {
            serializer.write(this, stringWriter);
            StringXML = stringWriter.toString();

            File fl = new File( UnattendedFilePathFullGet(FolderPath));
            FileOutputStream fos = new FileOutputStream(fl);
            fos.write(StringXML.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String UnattendedFilePathFullGet(String FolderPath){
        return FolderPath + UnattendedFileName;
    }

    public int StepsCountGet(){
        int cnt = 0;
        if ( JobType == ConstJobTypeUnattended ){
            cnt++;
        }

        if ( CopyToClipboard ){
            cnt++;
        }

        if ( UploadToHTTP ){
            cnt++;
        }

        return cnt;
    }
}
