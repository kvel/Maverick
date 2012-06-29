package com.maveric;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.maveric.contentprovider.FoodTrackerProvider;
import com.maveric.database.model.FoodTrackerTable;

public class DietTrackerViewerActivity extends MavericListBaseActiity {

	TextView previous;
	TextView next;
	TextView date;
	Context ctx;
	Button close;
	Cursor cursorDate;
	Cursor cursorDetails;;
	ArrayList<String> allDate;
	int count;
	int constantCont;

	@Override
	protected void setContentToLayout() {
		setContentView(R.layout.workoutviewcontainer);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ctx = getApplicationContext();

		previous = (TextView) findViewById(R.id.prev_date);
		next = (TextView) findViewById(R.id.next_date);
		next.setVisibility(View.INVISIBLE);
		date = (TextView) findViewById(R.id.date);

		try {
			cursorDate = managedQuery(FoodTrackerProvider.GET_FOOD_DETAILS_URI,
					null, null, null, null);
			constantCont = cursorDate.getCount();
			Log.i("kumar" + this.getClass(), "constantCont:" + constantCont);
			count = 0;
			cursorDate.moveToFirst();
			if (constantCont > 0) {
				allDate = new ArrayList<String>();
				do {

					allDate.add(cursorDate.getString(cursorDate
							.getColumnIndex(FoodTrackerTable.Column.DATE)));

				} while (cursorDate.moveToNext());

				date.setText(allDate.get(0));
				if (constantCont == 1) {
					next.setVisibility(View.INVISIBLE);
					previous.setVisibility(View.INVISIBLE);
				}
				Log.i("kumar" + this.getClass(), "array count" + allDate.size());
				listViewRefresh(allDate.get(0));
			} else {

			}
		} catch (Exception e1) {
			Log.e("kumar" + this.getClass(),
					"cursor error load date" + e1.getMessage(), e1);
		}

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					if (count <= constantCont) {
						next.setVisibility(View.VISIBLE);
						Log.i("kumar" + this.getClass(),
								"previous start count:" + count);
						count = count + 1;
						String presentDate = allDate.get(count);
						date.setText(presentDate);
						listViewRefresh(presentDate);
						if (count == constantCont - 1) {
							previous.setVisibility(View.INVISIBLE);
						}
						Log.i("kumar" + this.getClass(), "previous end count:"
								+ count);

					}
				} catch (Exception e) {
					Log.e("kumar" + this.getClass(),
							"previous" + e.getMessage(), e);
				}

			}
		});
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					if (count <= constantCont) {
						Log.i("kumar" + this.getClass(), "next start count:"
								+ count);
						count = count - 1;
						String presentDate = allDate.get(count);
						date.setText(presentDate);
						listViewRefresh(presentDate);
						previous.setVisibility(View.VISIBLE);
						if (count == 0) {
							next.setVisibility(View.INVISIBLE);
						}
						Log.i("kumar" + this.getClass(), "next previous count:"
								+ count);
					}
				} catch (Exception e) {
					Log.e("kumar" + this.getClass(), "next" + e.getMessage(), e);

				}

			}
		});

	}

	protected void listViewRefresh(String presentDate) {
		try {
			Log.i("kumar" + this.getClass(), "date" + presentDate);
			Uri name = Uri.withAppendedPath(
					FoodTrackerProvider.FOOD_BY_DATE_URI, presentDate);

			cursorDetails = managedQuery(name, null, null, null, null);

			Log.i("kumar" + this.getClass(), "count of cursor list view"
					+ cursorDetails.getCount());
			cursorDetails.moveToFirst();

			ListAdapter adapter = new DietTrackerAdapter(this, cursorDetails,
					new String[] { FoodTrackerTable.Column.DATE },
					new int[] { R.id.listexcerisetype });
			setListAdapter(adapter);
		} catch (Exception e1) {
			Log.e("kumar" + this.getClass(), "cursor error" + e1.getMessage(),
					e1);

		}
	}
}