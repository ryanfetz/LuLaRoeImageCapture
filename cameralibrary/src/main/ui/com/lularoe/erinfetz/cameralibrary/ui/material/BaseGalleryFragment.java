package com.lularoe.erinfetz.cameralibrary.ui.material;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lularoe.erinfetz.cameralibrary.R;
import com.lularoe.erinfetz.cameralibrary.base.CameraOutputUriProvider;
import com.lularoe.erinfetz.cameralibrary.base.material.MaterialMediaCaptureContext;
import com.lularoe.erinfetz.cameralibrary.types.CameraIntentKey;
import com.lularoe.erinfetz.core.graphics.Colors;

public abstract class BaseGalleryFragment extends Fragment implements CameraOutputUriProvider, View.OnClickListener {

    protected MaterialMediaCaptureContext mInterface;
    protected int mPrimaryColor;
    protected Uri mOutputUri;
    protected View mControlsFrame;
    protected Button mRetry;
    protected Button mConfirm;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mInterface = (MaterialMediaCaptureContext) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOutputUri = getArguments().getParcelable(CameraIntentKey.OUTPUT_URI);
        mControlsFrame = view.findViewById(R.id.controlsFrame);
        mRetry = (Button) view.findViewById(R.id.retry);
        mConfirm = (Button) view.findViewById(R.id.confirm);

        mPrimaryColor = getArguments().getInt(CameraIntentKey.PRIMARY_COLOR);
        if (Colors.isColorDark(mPrimaryColor)) {
            mPrimaryColor = Colors.darkenColor(mPrimaryColor);
            final int textColor = ContextCompat.getColor(view.getContext(), R.color.mcam_color_light);
            mRetry.setTextColor(textColor);
            mConfirm.setTextColor(textColor);
        } else {
            final int textColor = ContextCompat.getColor(view.getContext(), R.color.mcam_color_dark);
            mRetry.setTextColor(textColor);
            mConfirm.setTextColor(textColor);
        }
        mControlsFrame.setBackgroundColor(mPrimaryColor);

        mRetry.setVisibility(getArguments().getBoolean(CameraIntentKey.ALLOW_RETRY, true) ? View.VISIBLE : View.GONE);

    }

    @Override
    public Uri getOutputUri() {
        return getArguments().getParcelable(CameraIntentKey.OUTPUT_URI);
    }

    void showDialog(String title, String errorMsg) {
        new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(errorMsg)
                .positiveText(android.R.string.ok)
                .show();
    }
}