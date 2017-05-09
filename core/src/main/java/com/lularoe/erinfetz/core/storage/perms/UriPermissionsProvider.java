package com.lularoe.erinfetz.core.storage.perms;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by ryan.fetz on 5/9/2017.
 */

public interface UriPermissionsProvider {
    void grantReadWritePermissions(Uri uri, String packageName);
    void revokeReadWritePermissions(Uri uri);

    void grantPermissions(Uri uri, String packageName, int flags);
    void revokePermissions(Uri uri, int flags);

    void grantPermissions(Uri uri, Intent intent, int flags);
    void grantReadWritePermissions(Uri uri, Intent intent);
}
