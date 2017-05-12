package com.lularoe.erinfetz.cameralibrary.material;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.lularoe.erinfetz.core.base.material.BaseCaptureActivity;
import com.lularoe.erinfetz.cameralibrary.material.CameraFragment;

public class CaptureActivity extends BaseCaptureActivity {

    @Override
    @NonNull
    public Fragment getFragment() {
        return CameraFragment.newInstance();
    }
}