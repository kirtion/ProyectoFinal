package es.chinonymous.clientemovil.utility;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class Acelerometro implements SensorEventListener {

    private float deltaX, deltaY, deltaZ;
    public static Acelerometro acelerometro = new Acelerometro();

    public Acelerometro() {
        deltaX = 0;
        deltaY = 0;
        deltaZ = 0;
    }

    public static Acelerometro getInstance() {
        return acelerometro;
    }

    public void setListener(Context context) {
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        deltaX = event.values[1];
        deltaY = event.values[0];
        deltaZ = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public float getDeltaZ() {
        return deltaZ;
    }
}
