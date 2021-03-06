package com.maveric.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.maveric.database.MaverickHelper;
import com.maveric.database.model.WorkOutTrackerTable;

public class WorkoutProvider extends ContentProvider {
	public static final String WORKOUT_DB = "WorkoutDB";
	private MaverickHelper database;
	private static final int GET_WORKOUT_DETAILS = 1;
	private static final int INSERT_WORKOUT_DETAILS = 2;
	private static final int WORKOUT_BY_DATE = 3;
	private static final int WORKOUT_BY_DATE_SUM_VALUE = 4;

	public static final String PROVIDER_NAME = "com.maveric.WorkoutProvider";
	public static final Uri BASE_URI = Uri.parse("content://" + PROVIDER_NAME);

	public static final Uri INSERT_WORKOUT_DETAILS_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/insertOrUpdateWorkout");

	public static final Uri WORKOUT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/takevalueworkout");

	public static final Uri WORKOUT_BY_DATE_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/takevaluebydate");
	public static final Uri WORKOUT_BY_DATE_SOMEVALUE_URI = Uri
			.parse("content://" + PROVIDER_NAME + "/takesumbydate");

	private static final UriMatcher sURIMatcher;

	static {
		sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sURIMatcher.addURI(PROVIDER_NAME, "insertOrUpdateWorkout",
				INSERT_WORKOUT_DETAILS);
		sURIMatcher.addURI(PROVIDER_NAME, "takevalueworkout",
				GET_WORKOUT_DETAILS);
		sURIMatcher.addURI(PROVIDER_NAME, "takevaluebydate/*", WORKOUT_BY_DATE);
		sURIMatcher.addURI(PROVIDER_NAME, "takesumbydate/*",
				WORKOUT_BY_DATE_SUM_VALUE);

	}

	@Override
	public boolean onCreate() {
		database = new MaverickHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		checkColumns(projection);
		int uriType = sURIMatcher.match(url);
		SQLiteDatabase db = database.getWritableDatabase();
		String groupBy = null;
		switch (uriType) {

		case GET_WORKOUT_DETAILS:
			queryBuilder.setTables(WorkOutTrackerTable.TABLE);
			sortOrder = WorkOutTrackerTable.Column.ID + " DESC ";
			selection = "1) GROUP BY (" + WorkOutTrackerTable.Column.DATE;
			// queryBuilder.appendWhere(WorkOutTrackerTable.Column.DATE + "="
			// + url.getPathSegments().get(1));
			break;
		case WORKOUT_BY_DATE:
			queryBuilder.setTables(WorkOutTrackerTable.TABLE);
			sortOrder = WorkOutTrackerTable.Column.ID + " DESC ";
			queryBuilder.appendWhere(WorkOutTrackerTable.Column.DATE + "='"
					+ url.getPathSegments().get(1) + "'");
			break;

		case WORKOUT_BY_DATE_SUM_VALUE:
			queryBuilder.setTables(WorkOutTrackerTable.TABLE);
			sortOrder = WorkOutTrackerTable.Column.ID + " DESC ";
			projection = new String[] { "sum(workout) as "
					+ WorkOutTrackerTable.Column.WORKOUT };
			queryBuilder.appendWhere(WorkOutTrackerTable.Column.DATE + "='"
					+ url.getPathSegments().get(1) + "'");
			break;

		default:
			throw new IllegalArgumentException("Unknown URI: " + url);
		}
		Log.d("kumar", "DB select query :" + url + "; projection :"
				+ projection + "; table :" + queryBuilder.getTables()
				+ "; selection :" + selection + "; sortOrder :" + sortOrder);
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, groupBy, null, sortOrder);

		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), url);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return String.valueOf(sURIMatcher.match(uri));
	}

	@Override
	public synchronized Uri insert(Uri uri, ContentValues values) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case INSERT_WORKOUT_DETAILS:
			id = sqlDB.replaceOrThrow(WorkOutTrackerTable.TABLE, null, values);
			break;
		default:
			throw new IllegalArgumentException("Insert Unknown URI: " + uri);
		}
		Log.d("kumar", "DB insert query :" + uri + "; generated Id :" + id
				+ "; values :" + values);
		return Uri.parse(BASE_URI + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		return 0;
	}

	@Override
	public synchronized int update(Uri uri, ContentValues values,
			String selection, String[] selectionArgs) {

		return 0;
	}

	private void checkColumns(String[] projection) {
		String[] available = WorkOutTrackerTable.getColumns();
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}
}
