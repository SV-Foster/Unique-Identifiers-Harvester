/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.UniqueIdentifiersHarvester;

public class GlobalConstsClass {
    public static final int CopyrightYearLow = BuildConfig.CopyrightYearLow;
    public static final int CopyrightYearHigh = BuildConfig.CopyrightYearHigh;
    public static final String AuthorEmail = BuildConfig.AuthorEmail;
    public static final String AuthorName = BuildConfig.AuthorName;
    public static final String AuthorWebsite = BuildConfig.AuthorWebsite;
    public static final String AuthorWebsiteLink = BuildConfig.AuthorWebsiteLink;
    public static final String AuthorWebsiteLinkGo = BuildConfig.AuthorWebsiteLinkGo;

    public static final int ProgramVersionMain = BuildConfig.ProgramVersionMain;
    public static final int ProgramVersionSecond = BuildConfig.ProgramVersionSecond;
    public static final int ProgramVersionThird = BuildConfig.ProgramVersionThird;
    public static final int ProgramVersionForth = BuildConfig.ProgramVersionForth;

    public static final String DataHarvestedIntentName = "DataHarvestedObj";

    public static final String SDOutputFileNamePrefix = "Device UIDs ";
    public static final String SDOutputFileNameDateTime = "yyyy-MM-dd HH-mm-ss";
    public static final String SDOutputFileNameExtension = ".xml";

    public static final String HTTPOutputDefaultURL = "http://demo." + AuthorWebsite + "/a/";
    public static final String HTTPOutputUnattendedURL = "http://demo." + AuthorWebsite + "/b/";
}
