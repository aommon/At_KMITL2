package com.aommon.ar_navigator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;

public class SearchLocation  extends Activity {
	public static final String TAG = "SearchLocation.java";

	final static int[] to = new int[] { android.R.id.text1 };
	final static String[] from = new String[] { "name" };

	private AutoCompleteTextView mLNameView;
	private Database mDbHelper;
	String mode = " ";
	String r_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchview);
		mDbHelper = new Database(this);

		mLNameView = (AutoCompleteTextView) findViewById(R.id.l_name);

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_dropdown_item_1line, null, from, to);
		Log.e(TAG, "Create Adapter");
		mLNameView.setAdapter(adapter);

		mLNameView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);
				r_name = cursor.getString(cursor.getColumnIndexOrThrow(Database.COL_DISPLAY_NAME));
				double r_lat = cursor.getDouble(cursor.getColumnIndex("latitude"));
                double r_long = cursor.getDouble(cursor.getColumnIndex("longitude"));
                final Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("mydName", r_name);
                i.putExtra("mydLat", r_lat);
				i.putExtra("mydLong", r_long);
				Log.e(TAG,"Item Click : "+ r_name + " " + r_lat + "," + r_long);
				AlertDialog.Builder builder = new AlertDialog.Builder(SearchLocation.this);
                builder.setTitle("เลือกรูปแบบการนำทาง");
                builder.setPositiveButton("Driving", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mode = "driving";
						i.putExtra("mydMode", mode);
						Log.e(TAG,"Mode : "+ mode);
						setResult(RESULT_OK, i);
						finish();
					}
				});
                builder.setNegativeButton("Walking", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mode = "walking";
						i.putExtra("mydMode", mode);
						Log.e(TAG,"Mode : "+ mode);
						setResult(RESULT_OK, i);
						finish();
					}
				});
                builder.show();				
			}
		});
		
		// Set the CursorToStringConverter, to provide the labels for the
		// choices to be displayed in the AutoCompleteTextView.
		adapter.setCursorToStringConverter(new CursorToStringConverter() {
			public String convertToString(android.database.Cursor cursor) {
				// Get the label for this row out of the "state" column
				final int columnIndex = cursor.getColumnIndexOrThrow("name");
				final String str = cursor.getString(columnIndex);
				Log.e(TAG, "String : "+str);
				return str;
			}
		});

		// Set the FilterQueryProvider, to run queries for choices
		// that match the specified input.
		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {
				// Search for states whose names begin with the specified letters.
				Cursor cursor = mDbHelper.getMatchingLocate((constraint != null ? constraint.toString() : null));
				Log.e(TAG, "constraint : "+ constraint);
				return cursor;
			}
		});

	}


}
