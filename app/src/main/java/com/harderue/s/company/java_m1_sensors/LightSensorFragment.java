package com.harderue.s.company.java_m1_sensors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LightSensorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private LightGraphView lightGraphView;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable updater = new Runnable() {
        @Override
        public void run() {
            if (lightGraphView != null) {
                lightGraphView.invalidate();
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_light_sensor, container, false);
        lightGraphView = new LightGraphView(getActivity());
        rootView.addView(lightGraphView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove the LightGraphView to prevent memory leaks
        lightGraphView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        handler.post(updater);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        handler.removeCallbacks(updater);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightGraphView.addLightValue(event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    private class LightGraphView extends View {
        private final Paint paint = new Paint();
        private final float[] values = new float[60];
        private int index;

        public LightGraphView(Context context) {
            super(context);
            paint.setColor(0xFFFF0000); // Red color for the graph line (prettier) // @color/white
            index = 0;
        }

        public void addLightValue(float value) {
            values[index] = value;
            index = (index + 1) % values.length;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // Draw the graph.
            for (int i = 0; i < values.length - 1; i++) {
                canvas.drawLine(i * (getWidth() / (float) values.length), values[i],
                        (i + 1) * (getWidth() / (float) values.length), values[i + 1], paint);
            }
        }
    }
}
