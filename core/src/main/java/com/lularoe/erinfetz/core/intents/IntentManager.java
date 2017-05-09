package com.lularoe.erinfetz.core.intents;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by ryan.fetz on 5/9/2017.
 */

public class IntentManager {
    private final Context context;

    public IntentManager(Context context){
        this.context = context;
    }

    public List<ResolveInfo> resolvedInfoActivities (Intent intent){
        List<ResolveInfo> resolvedInfoActivities = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolvedInfoActivities;
    }


    public boolean isIntentAvailable(Intent intent) {
        return resolvedInfoActivities(intent).size() > 0;
    }

    public boolean isIntentAvailable(String action) {
        final Intent intent = new Intent(action);
        return isIntentAvailable(intent);
    }
}
