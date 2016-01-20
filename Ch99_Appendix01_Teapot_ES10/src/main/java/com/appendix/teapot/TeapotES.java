/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appendix.teapot;

// Note: Original code at :  http://github.com/freedomtan/android-utah-teapot

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class TeapotES extends Activity implements SensorEventListener {
		
    private MyGLSurfaceView mGLSurfaceView;
	private SensorManager sensorManager;
	private float[] sensorValues;
	private int sensorMode;
	
	public static final int ACCEL_ID = Menu.FIRST;
	public static final int COMPASS_ID = Menu.FIRST + 1;
    
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create our Preview view and set it as the content of our
		// Activity
		mGLSurfaceView = new MyGLSurfaceView(this);
		mGLSurfaceView.setRenderer(new TeapotRenderer());
		setContentView(mGLSurfaceView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorValues = new float[3];
        sensorMode = ACCEL_ID;
		mGLSurfaceView.setSensor(sensorMode);
	}

	@Override
	protected void onResume() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onResume();
		sensorManager.registerListener(this, 
	        		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
	        		SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, 
	        		sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), 
	        		SensorManager.SENSOR_DELAY_NORMAL);
		mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		sensorManager.unregisterListener(this);
		mGLSurfaceView.onPause();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, ACCEL_ID, 0, R.string.menu_accel);
        menu.add(0, COMPASS_ID, 0, R.string.menu_compass);
        return result;
    }
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		sensorMode  = item.getItemId();
		mGLSurfaceView.setSensor(sensorMode);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (sensorMode) {
		case ACCEL_ID:
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				sensorValues[0] = (float) (event.values[0] / SensorManager.GRAVITY_EARTH);
				sensorValues[1] = (float) (event.values[1] / SensorManager.GRAVITY_EARTH);
				sensorValues[2] = (float) (event.values[2] / SensorManager.GRAVITY_EARTH);
				mGLSurfaceView.onSensorChanged(sensorValues);
			}
			return;
		case COMPASS_ID:
			if (event.sensor.getType()  == Sensor.TYPE_ORIENTATION) {
				if (Math.abs(event.values[0]) > 10)
					mGLSurfaceView.onSensorChanged(event.values);
			}
			return;
		}
	}
}