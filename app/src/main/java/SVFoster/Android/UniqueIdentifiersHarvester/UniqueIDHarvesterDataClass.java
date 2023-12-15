/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.UniqueIdentifiersHarvester;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "AndroidUIDs")
public class UniqueIDHarvesterDataClass implements Serializable {
        public static final int ConstStatusNotCalled = 0;
        public static final int ConstStatusOK = 1;
        public static final int ConstStatusError = 2;
        public static final int ConstStatusErrorNotSupported = 3;
        public static final int ConstStatusErrorDependency = 4;

        public final static int ConstDataStructureVersion = 0;
        @Attribute(name = "DataStructureVersion")
        public int DataStructureVersion = ConstDataStructureVersion;

        // Android
        @Element(name = "AndroidID_way1", required = false)
        public String AndroidID_way1 = null;
        @Element(name = "AndroidID_way1_status")
        public int    AndroidID_way1_status = ConstStatusNotCalled;
        @Element(name = "SerialNo_way1", required = false)
        public String SerialNo_way1 = null;
        @Element(name = "SerialNo_way1_status")
        public int    SerialNo_way1_status = ConstStatusNotCalled;
        @Element(name = "SerialNo_way2", required = false)
        public String SerialNo_way2 = null;
        @Element(name = "SerialNo_way2_status")
        public int    SerialNo_way2_status = ConstStatusNotCalled;
        @Element(name = "SerialNo_way3", required = false)
        public String SerialNo_way3 = null;
        @Element(name = "SerialNo_way3_status")
        public int    SerialNo_way3_status = ConstStatusNotCalled;
        @Element(name = "SerialNo_way4", required = false)
        public String SerialNo_way4 = null;
        @Element(name = "SerialNo_way4_status")
        public int    SerialNo_way4_status = ConstStatusNotCalled;
        @Element(name = "Hostname_way1", required = false)
        public String Hostname_way1 = null;
        @Element(name = "Hostname_way1_status")
        public int    Hostname_way1_status = ConstStatusNotCalled;
        @Element(name = "Hostname_way2", required = false)
        public String Hostname_way2 = null;
        @Element(name = "Hostname_way2_status")
        public int    Hostname_way2_status = ConstStatusNotCalled;
        @Element(name = "Hostname_way3", required = false)
        public String Hostname_way3 = null;
        @Element(name = "Hostname_way3_status")
        public int    Hostname_way3_status = ConstStatusNotCalled;

        // Google
        @Element(name = "GSF_way1", required = false)
        public String GSF_way1 = null;
        @Element(name = "GSF_way1_status")
        public int    GSF_way1_status = ConstStatusNotCalled;
        @Element(name = "ADID_way1", required = false)
        public String ADID_way1 = null;
        @Element(name = "ADID_way1_status")
        public int    ADID_way1_status = ConstStatusNotCalled;

        // Cell network
        @ElementList(name = "IMEIList_way1", type = CellRadioIDClass.class)
        public List<CellRadioIDClass> IMEIList_way1 = new ArrayList<>();
        @Element(name = "IMEIList_way1_status")
        public int                    IMEIList_way1_status = ConstStatusNotCalled;
        @ElementList(name = "IMEIList_way2", type = CellRadioIDClass.class)
        public List<CellRadioIDClass> IMEIList_way2 = new ArrayList<>();
        @Element(name = "IMEIList_way2_status")
        public int                    IMEIList_way2_status = ConstStatusNotCalled;
        @Element(name = "SIMCount_way1")
        public int                    SIMCount_way1 = 0;
        @Element(name = "SIMCount_way1_status")
        public int                    SIMCount_way1_status = ConstStatusNotCalled;
        @ElementList(name = "SIMList_way1", type = SIMDataClass.class)
        public List<SIMDataClass>     SIMList_way1 = new ArrayList<>();
        @Element(name = "SIMList_way1_status")
        public int                    SIMList_way1_status = ConstStatusNotCalled;

        // NIC
        @ElementList(name = "NICList_way1", type = NICIDClass.class)
        public List<NICIDClass> NICList_way1 = new ArrayList<>();
        @Element(name = "NICList_way1_status")
        public int              NICList_way1_status = ConstStatusNotCalled;

        // NonUniq
        @Element(name = "BuildFingerprint_way1", required = false)
        public String BuildFingerprint_way1 = null;
        @Element(name = "BuildFingerprint_way1_status")
        public int    BuildFingerprint_way1_status = ConstStatusNotCalled;


        UniqueIDHarvesterDataClass()
        {

        }

        public void SIMList_way1Add(int SIMSlot, String IMSI, String Serial){
                this.SIMList_way1.add(
                        new SIMDataClass(SIMSlot, IMSI, Serial)
                );
        }

        public void IMEIList_way1Add(int SIMSlot, String IMEI){
                this.IMEIList_way1.add(
                        new CellRadioIDClass(SIMSlot, IMEI)
                );
        }

        public void IMEIList_way2Add(int SIMSlot, String IMEI){
                this.IMEIList_way2.add(
                        new CellRadioIDClass(SIMSlot, IMEI)
                );
        }

        public void NICList_way1Add(String Name, String MAC){
                this.NICList_way1.add(
                        new NICIDClass(Name, MAC)
                );
        }
}

@Root(name = "SIM")
class SIMDataClass implements Serializable {
        @Element(name = "SIMSlot")
        public int SIMSlot = 0;
        @Element(name = "IMSI", required = false)
        public String IMSI = null;
        @Element(name = "Serial", required = false)
        public String Serial = null;

        SIMDataClass()
        {

        }

        public SIMDataClass(int SIMSlot, String IMSI, String Serial) {
                this.SIMSlot = SIMSlot;
                this.IMSI = IMSI;
                this.Serial = Serial;
        }
}

@Root(name = "Radio")
class CellRadioIDClass implements Serializable {
        @Element(name = "SIMSlot")
        public int SIMSlot = 0;
        @Element(name = "IMEI", required = false)
        public String IMEI = null;

        CellRadioIDClass()
        {

        }

        public CellRadioIDClass(int SIMSlot, String IMEI){
                this.SIMSlot = SIMSlot;
                this.IMEI = IMEI;
        }
}

@Root(name = "NIC")
class NICIDClass implements Serializable {
        @Element(name = "Name")
        public String Name = null;
        @Element(name = "MAC")
        public String MAC = null;

        NICIDClass()
        {

        }

        public NICIDClass(String Name, String MAC){
                this.Name = Name;
                this.MAC = MAC;
        }
}
