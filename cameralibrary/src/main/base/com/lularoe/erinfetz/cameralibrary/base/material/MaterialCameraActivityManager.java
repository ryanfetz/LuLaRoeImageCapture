package com.lularoe.erinfetz.cameralibrary.base.material;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.lularoe.erinfetz.cameralibrary.R;
import com.lularoe.erinfetz.cameralibrary.base.CameraConfiguration;
import com.lularoe.erinfetz.cameralibrary.material.CaptureActivity;
import com.lularoe.erinfetz.cameralibrary.material.CaptureActivity2;
import com.lularoe.erinfetz.cameralibrary.types.CameraIntentKey;
import com.lularoe.erinfetz.cameralibrary.types.Cameras;
import com.lularoe.erinfetz.cameralibrary.types.Media;

@SuppressWarnings("WeakerAccess")
public class MaterialCameraActivityManager {



    private Context mContext;
    private Activity mActivityContext;
    private android.app.Fragment mAppFragment;
    private android.support.v4.app.Fragment mSupportFragment;
    private boolean mIsFragment = false;
    private MaterialCameraStyleConfiguration.Builder styleConfiguration;
    private CameraConfiguration.Builder cameraConfiguration;

    public MaterialCameraActivityManager(@NonNull Activity context) {
        mContext = context;
        mActivityContext = context;
        styleConfiguration = new MaterialCameraStyleConfiguration.Builder();
        cameraConfiguration = new CameraConfiguration.Builder();
        styleConfiguration.primaryColorAttr(context, R.attr.colorPrimary);
    }

    public MaterialCameraActivityManager(@NonNull android.app.Fragment context) {
        mIsFragment = true;
        mContext = context.getActivity();
        mAppFragment = context;
        mSupportFragment = null;
        styleConfiguration = new MaterialCameraStyleConfiguration.Builder();
        cameraConfiguration = new CameraConfiguration.Builder();
        styleConfiguration.primaryColorAttr(mContext, R.attr.colorPrimary);
    }

    public MaterialCameraActivityManager(@NonNull android.support.v4.app.Fragment context) {
        mIsFragment = true;
        mContext = context.getContext();
        mSupportFragment = context;
        mAppFragment = null;
        styleConfiguration = new MaterialCameraStyleConfiguration.Builder();
        cameraConfiguration = new CameraConfiguration.Builder();
        styleConfiguration.primaryColorAttr(mContext, R.attr.colorPrimary);
    }

    public Intent getIntent(Uri outputUri) {
        MaterialCameraStyleConfiguration sc =styleConfiguration.build();
        CameraConfiguration cc = cameraConfiguration.build();

        final Class<?> cls = !cc.isForceCamera1() && Cameras.hasCamera2(mContext, cc.getMediaAction() == Media.MEDIA_ACTION_PHOTO) ?
                CaptureActivity2.class : CaptureActivity.class;

        Intent intent = new Intent(mContext, cls)
                .putExtra(CameraIntentKey.MATERIAL_STYLE_INTENT_KEY, sc)
                .putExtra(CameraIntentKey.CAMERA_CONFIG_INTENT_KEY, cc)
                .putExtra(CameraIntentKey.OUTPUT_URI, outputUri);


        return intent;
    }

    public void start(Uri outputUri, int requestCode) {
        if (mIsFragment && mSupportFragment != null)
            mSupportFragment.startActivityForResult(getIntent(outputUri), requestCode);
        else if (mIsFragment && mAppFragment != null)
            mAppFragment.startActivityForResult(getIntent(outputUri), requestCode);
        else
            mActivityContext.startActivityForResult(getIntent(outputUri), requestCode);
    }
}