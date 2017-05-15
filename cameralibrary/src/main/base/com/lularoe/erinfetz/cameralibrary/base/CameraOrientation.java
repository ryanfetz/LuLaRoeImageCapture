package com.lularoe.erinfetz.cameralibrary.base;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.lularoe.erinfetz.cameralibrary.types.Orientation;
import com.lularoe.erinfetz.core.media.Degrees;
import com.lularoe.erinfetz.cameralibrary.util.DisplayOrientation;

public class CameraOrientation implements Parcelable {

    @Orientation.SensorPosition
    protected int sensorPosition = Orientation.SENSOR_POSITION_UNSPECIFIED;

    @Orientation.DeviceDefaultOrientation
    protected int deviceDefaultOrientation = Configuration.ORIENTATION_UNDEFINED;

    @Degrees.DegreeUnits
    protected int degrees = Degrees.DEGREES_0;

    public CameraOrientation(){

    }

    @Orientation.SensorPosition
    public int getSensorPosition() {
        return sensorPosition;
    }

    public void setSensorPosition(@Orientation.SensorPosition int sensorPosition) {
        this.sensorPosition = sensorPosition;
    }

    @Orientation.DeviceDefaultOrientation
    public int getDeviceDefaultOrientation() {
        return deviceDefaultOrientation;
    }

    public void setDeviceDefaultOrientation(@Orientation.DeviceDefaultOrientation int deviceDefaultOrientation) {
        this.deviceDefaultOrientation = deviceDefaultOrientation;
    }

    @Degrees.DegreeUnits
    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(@Degrees.DegreeUnits int degrees) {
        this.degrees = degrees;
    }

    public void deviceDefaultOrientation(@NonNull Context context){
        @Orientation.DeviceDefaultOrientation
        final int defaultOrientation = DisplayOrientation.getDeviceDefaultOrientation(context);

        this.setDeviceDefaultOrientation(defaultOrientation);
    }

    public void sersorChanged(SensorEvent sensorEvent){
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (sensorEvent.values[0] < 4 && sensorEvent.values[0] > -4) {
                if (sensorEvent.values[1] > 0) {
                    // UP
                    this.setSensorPosition(Orientation.SENSOR_POSITION_UP);
                    this.setDegrees(this.getDeviceDefaultOrientation() == Configuration.ORIENTATION_PORTRAIT ? Degrees.DEGREES_0 : Degrees.DEGREES_90);
                } else if (sensorEvent.values[1] < 0) {
                    // UP SIDE DOWN
                    this.setSensorPosition(Orientation.SENSOR_POSITION_UP_SIDE_DOWN);
                    this.setDegrees(this.getDeviceDefaultOrientation() == Configuration.ORIENTATION_PORTRAIT ? Degrees.DEGREES_180 : Degrees.DEGREES_270);
                }
            } else if (sensorEvent.values[1] < 4 && sensorEvent.values[1] > -4) {
                if (sensorEvent.values[0] > 0) {
                    // LEFT
                    this.setSensorPosition(Orientation.SENSOR_POSITION_LEFT);
                    this.setDegrees(this.getDeviceDefaultOrientation() == Configuration.ORIENTATION_PORTRAIT ? Degrees.DEGREES_90 : Degrees.DEGREES_180);
                } else if (sensorEvent.values[0] < 0) {
                    // RIGHT
                    this.setSensorPosition(Orientation.SENSOR_POSITION_RIGHT);
                    this.setDegrees(this.getDeviceDefaultOrientation() == Configuration.ORIENTATION_PORTRAIT ? Degrees.DEGREES_270 : Degrees.DEGREES_0);
                }
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sensorPosition);
        dest.writeInt(deviceDefaultOrientation);
        dest.writeInt(degrees);
    }

    @SuppressWarnings("WrongConstant")
    protected CameraOrientation(Parcel in) {
        sensorPosition = in.readInt();
        deviceDefaultOrientation = in.readInt();
        degrees = in.readInt();
    }

    public static final Creator<CameraOrientation> CREATOR = new Creator<CameraOrientation>() {
        @Override
        public CameraOrientation createFromParcel(Parcel in) {
            return new CameraOrientation(in);
        }

        @Override
        public CameraOrientation[] newArray(int size) {
            return new CameraOrientation[size];
        }
    };

}
