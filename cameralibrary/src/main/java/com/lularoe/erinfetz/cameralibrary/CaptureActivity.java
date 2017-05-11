package com.lularoe.erinfetz.cameralibrary;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.lularoe.erinfetz.cameralibrary.internal.BaseCaptureActivity;
import com.lularoe.erinfetz.cameralibrary.internal.CameraFragment;

public class CaptureActivity extends BaseCaptureActivity {

    @Override
    @NonNull
    public Fragment getFragment() {
        return CameraFragment.newInstance();
    }
}