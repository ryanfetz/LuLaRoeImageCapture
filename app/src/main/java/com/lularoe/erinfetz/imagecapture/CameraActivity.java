package com.lularoe.erinfetz.imagecapture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class CameraActivity extends Activity {
    public static final String ACTION_IMAGE_CAPTURE = "com.lularoe.erinfetz.imagecapture.IMAGE_CAPTURE";
    public static final String INTENT_DATA_FILE = "outputFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent intent = getIntent();

        String outputFile = intent.getStringExtra(INTENT_DATA_FILE);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance(outputFile))
                    .commit();
        }
    }
}
