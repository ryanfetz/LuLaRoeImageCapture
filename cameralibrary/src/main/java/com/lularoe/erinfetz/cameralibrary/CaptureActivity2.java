package com.lularoe.erinfetz.cameralibrary;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.lularoe.erinfetz.cameralibrary.internal.BaseCaptureActivity;
import com.lularoe.erinfetz.cameralibrary.internal.Camera2Fragment;

public class CaptureActivity2 extends BaseCaptureActivity {

    @Override
    @NonNull
    public Fragment getFragment() {
        return Camera2Fragment.newInstance();
    }
}