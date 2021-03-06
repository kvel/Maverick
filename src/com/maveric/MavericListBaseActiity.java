package com.maveric;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.maveric.enums.calender;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public abstract class MavericListBaseActiity extends ListActivity {
	protected Context context;
	protected TextView diet, workOut, metaBolic, inter;
	protected Button home;
	private int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentToLayout();
		diet = (TextView) findViewById(R.id.Diet_tracker);
		workOut = (TextView) findViewById(R.id.workout_tracker);
		metaBolic = (TextView) findViewById(R.id.metapolic_typing);
		inter = (TextView) findViewById(R.id.intract);
		home = (Button) findViewById(R.id.home_button);
		/* login Activity not use for nw via menu */
		// member.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Intent singup = new Intent(context, LoginActivity.class);
		// startActivity(singup);
		// }
		// });
		if (diet != null) {
			diet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent dietTracker = new Intent(context,
							CalendarViewActivity.class);
					dietTracker.putExtra("class",
							calender.DIET_TRACKER.getValue());
					startActivity(dietTracker);
				}
			});
			workOut.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent workOutTracker = new Intent(context,
							CalendarViewActivity.class);
					workOutTracker.putExtra("class",
							calender.WORK_OUT_TRACKER.getValue());
					startActivity(workOutTracker);
				}
			});
			metaBolic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent queries = new Intent(context, Queries.class);
					startActivity(queries);
				}
			});
			inter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent singup = new Intent(context,
							StaticPageMainActivity.class);
					startActivity(singup);
				}
			});
		}
		if (home != null)
			home.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					loding("Home",1000);
					Intent home = new Intent(context, DashBoardActivity.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					startActivity(home);
				}
			});
	}

	protected abstract void setContentToLayout();

	protected void toast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	protected float getBmi(float weight, float height) {
		float mheight = height / 100;

		return (float) (weight / (mheight * mheight));
	}

	protected float getRecWater(float weight) {
		return (float) (2.20462262 * 0.03 * weight);
	}

	protected float recWeight(float weight, float height) {
		float mheight = height / 100;
		return (float) (getBmi(weight, height) * mheight * mheight);
	}

	protected boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	protected String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("d-MMMM-yyyy");
		return format.format(c.getTime());
	}

	private String getStringFromDate(Long date) {
		return new SimpleDateFormat("d-MMMM-yyyy").format(date);
	}

	protected String prevDate(String date) {
		return getStringFromDate(getDateFromString(date).getTime()
				- MILLIS_IN_DAY);
	}

	private Date getDateFromString(String date) {
		try {
			return (Date) new SimpleDateFormat("d-MMMM-yyyy").parse(date);
		} catch (ParseException e) {
			Log.e("manikk", e.getMessage());
			return null;
		}
	}

	protected String nextDate(String date) {
		return getStringFromDate(getDateFromString(date).getTime()
				+ MILLIS_IN_DAY);
	}

	protected void metabolicQueries() {
		Intent singup = new Intent(context, metabolicQueries.class);
		startActivity(singup);
	}

	protected void loding(String title, final int time) {
		progressDialog = ProgressDialog.show(MavericListBaseActiity.this, title
				+ "...", "Processing your request");

		new Thread() {
			public void run() {
				try {
					sleep(time);
				} catch (Exception e) {
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
				}
				progressDialog.dismiss();
			}
		}.start();
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.i("manikk", "List Destroy");
		if (progressDialog != null)
			progressDialog.dismiss();
	}
}
