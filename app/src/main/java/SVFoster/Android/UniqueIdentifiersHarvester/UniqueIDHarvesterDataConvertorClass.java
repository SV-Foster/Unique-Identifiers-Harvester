/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.UniqueIdentifiersHarvester;

import androidx.annotation.NonNull;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;

public class UniqueIDHarvesterDataConvertorClass {

    static class DataHarvestedToArrayClass {
        public String Header = "";
        public String Value = "";

        public DataHarvestedToArrayClass(String Header, String Value){
            this.Header = Header;
            if ( Value != null ) {
                this.Value = Value;
            } else {
                this.Value = "Value not found";
            }

        }
    }

    public static String ToXML(@NonNull UniqueIDHarvesterDataClass DataHarvested ){
        Serializer serializer = new Persister();
        StringWriter stringWriter = new StringWriter();
        String Result = "";

        // Write the XML to the StringWriter
        try {
            serializer.write(DataHarvested, stringWriter);
            Result = stringWriter.toString();
        } catch (Exception e) {
            Result = e.toString();
        }

        return Result;
    }

    public static List<DataHarvestedToArrayClass> ToListOfStringsWithComments(@NonNull UniqueIDHarvesterDataClass DataHarvested ){
        List<DataHarvestedToArrayClass> Result = new ArrayList<>();

        Result.add( new DataHarvestedToArrayClass( "Android ID",  DataHarvested.AndroidID_way1 ));
        Result.add( new DataHarvestedToArrayClass( "Serial Number (API)",  DataHarvested.SerialNo_way1 ));
        Result.add( new DataHarvestedToArrayClass( "Serial Number (API)",  DataHarvested.SerialNo_way2 ));
        Result.add( new DataHarvestedToArrayClass( "Serial Number (API)",  DataHarvested.SerialNo_way3 ));
        Result.add( new DataHarvestedToArrayClass( "Serial Number (API)",  DataHarvested.SerialNo_way4 ));
        Result.add( new DataHarvestedToArrayClass( "Hostname",  DataHarvested.Hostname_way1 ));
        Result.add( new DataHarvestedToArrayClass( "Hostname",  DataHarvested.Hostname_way2 ));
        Result.add( new DataHarvestedToArrayClass( "Hostname",  DataHarvested.Hostname_way3 ));

        Result.add( new DataHarvestedToArrayClass( "Google Services Framework",  DataHarvested.GSF_way1 ));
        Result.add( new DataHarvestedToArrayClass( "Google Advertising ID",  DataHarvested.ADID_way1 ));

        Result.add( new DataHarvestedToArrayClass( "Count of SIM slots", Integer.toString( DataHarvested.SIMCount_way1 )));
        for (int i=0; i<DataHarvested.IMEIList_way1.size(); i++ ){
            Result.add( new DataHarvestedToArrayClass( "IMEI in slot " + DataHarvested.IMEIList_way1.get(i).SIMSlot,  DataHarvested.IMEIList_way1.get(i).IMEI ));
        }
        for (int i=0; i<DataHarvested.IMEIList_way2.size(); i++ ){
            Result.add( new DataHarvestedToArrayClass( "IMEI in slot " + DataHarvested.IMEIList_way2.get(i).SIMSlot,  DataHarvested.IMEIList_way2.get(i).IMEI ));
        }
        for (int i=0; i<DataHarvested.SIMList_way1.size(); i++ ){
            Result.add( new DataHarvestedToArrayClass( "SIM in slot " + DataHarvested.SIMList_way1.get(i).SIMSlot,  DataHarvested.SIMList_way1.get(i).IMSI ));
        }

        for (int i=0; i<DataHarvested.NICList_way1.size(); i++ ){
            Result.add( new DataHarvestedToArrayClass( "MAC of device " + DataHarvested.NICList_way1.get(i).Name,  DataHarvested.NICList_way1.get(i).MAC ));
        }

        Result.add( new DataHarvestedToArrayClass( "Build Fingerprint",  DataHarvested.BuildFingerprint_way1 ));

        return Result;
    }
}
