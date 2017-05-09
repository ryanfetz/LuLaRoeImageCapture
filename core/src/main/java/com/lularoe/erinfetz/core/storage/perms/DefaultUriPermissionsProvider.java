package com.lularoe.erinfetz.core.storage.perms;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.lularoe.erinfetz.core.intents.IntentManager;

import java.util.List;


public class DefaultUriPermissionsProvider implements UriPermissionsProvider{
    private final Context context;
    private final IntentManager im;

    public DefaultUriPermissionsProvider(Context context){
        this.context = context;
        this.im = new IntentManager(context);
    }

    @Override
    public void grantReadWritePermissions(Uri uri, String packageName) {
        grantPermissions(uri, packageName, Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }

    @Override
    public void revokeReadWritePermissions(Uri uri) {
        revokePermissions(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }

    @Override
    public void grantPermissions(Uri uri, String packageName, int flags) {
        context.grantUriPermission(packageName, uri, flags);
    }

    @Override
    public void revokePermissions(Uri uri, int flags) {
        context.revokeUriPermission(uri, flags);
    }

    @Override
    public void grantPermissions(Uri uri, Intent intent, int flags) {
        for (ResolveInfo ri : im.resolvedInfoActivities(intent)) {
            grantPermissions(uri, ri.activityInfo.packageName, flags);
        }
        intent.addFlags(flags);
    }

    @Override
    public void grantReadWritePermissions(Uri uri, Intent intent) {
        grantPermissions(uri, intent, Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }
}
