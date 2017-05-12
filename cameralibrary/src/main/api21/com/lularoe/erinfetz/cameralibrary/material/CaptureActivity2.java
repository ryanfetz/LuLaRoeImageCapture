package com.lularoe.erinfetz.cameralibrary.material;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.lularoe.erinfetz.core.base.material.BaseCaptureActivity;

public class CaptureActivity2 extends BaseCaptureActivity {

    @Override
    @NonNull
    public Fragment getFragment() {
        return Camera2Fragment.newInstance();
    }
}