package com.harderue.s.company.java_m1_sensors;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.githubBtn2).setOnClickListener(view -> {
            Log.i("test", "onCreate: Test click");
            String url = "https://github.com/HarderueStud/JAVA-M1-Sensors/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            CompassFragment compassFragment = new CompassFragment();
            LightSensorFragment lightSensorFragment = new LightSensorFragment();

            transaction.add(R.id.fragment_container, compassFragment);
            transaction.add(R.id.light_sensor_fragment_container, lightSensorFragment); // Add the LightSensorFragment.
            transaction.commit();
        }
    }
}