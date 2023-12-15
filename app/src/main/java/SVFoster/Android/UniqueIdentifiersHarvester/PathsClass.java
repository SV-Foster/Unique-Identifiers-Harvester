/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.UniqueIdentifiersHarvester;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.io.File;

public class PathsClass {
    public static String SDLastDataPathGet(Context cnx){
        File[] SDPathsFull = ContextCompat.getExternalFilesDirs(cnx, null);
        if (SDPathsFull.length < 1){
            return "/";
        }

        String BufferString = SDPathsFull[ SDPathsFull.length - 1 ].getAbsolutePath();
        BufferString = IncludeTrailingPathDelimiter( BufferString );
        return BufferString;
    }

    public static final String ConstTrailingPathDelimiter = "/";
    public static String IncludeTrailingPathDelimiter(String pth){
        if (pth == null) {
            return pth;
        }

        if (pth.length()<1) {
            return pth;
        }

        if (pth.endsWith(ConstTrailingPathDelimiter)){
            return pth;
        }

        return pth + ConstTrailingPathDelimiter;
    }
}
