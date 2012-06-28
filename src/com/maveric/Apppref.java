package com.maveric;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Apppref {
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;
	private Context context;
	private static final String BMI = "bmi";
	private static final String REC_WATER = "recWater";
	private static final String REC_WEIGHT = "recWeight";
	private static final String STARTUP = "startUp";

	public Apppref(Context context) {
		this.context = context.getApplicationContext();
		String AppPrefsString = getAppPrefsString(context
				.getString(R.string.APP_PREF_NAME));
		this.appSharedPrefs = context.getSharedPreferences(AppPrefsString,
				Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	private String getAppPrefsString(String prefsString) {
		return context.getPackageName() + "." + prefsString;
	}

	public void setBmi(float bmi) {
		prefsEditor.putFloat(BMI, bmi);
		prefsEditor.commit();
	}

	public float getBmi() {
		return appSharedPrefs.getFloat(BMI, 20);
	}

	public void setRecWeight(float weight) {
		prefsEditor.putFloat(REC_WEIGHT, weight);
		prefsEditor.commit();
	}

	public float getRecWeight() {
		return appSharedPrefs.getFloat(REC_WEIGHT, 60);
	}

	public void setRecWater(float water) {
		prefsEditor.putFloat(REC_WATER, water);
		prefsEditor.commit();
	}

	public float getRecWater() {
		return appSharedPrefs.getFloat(REC_WATER, 3);
	}

	public void setStartup(Boolean yes) {
		prefsEditor.putBoolean(STARTUP, yes);
		prefsEditor.commit();
	}

	public Boolean isStartUp() {
		return appSharedPrefs.getBoolean(STARTUP, true);
	}
}