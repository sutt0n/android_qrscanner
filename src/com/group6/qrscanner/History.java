package com.group6.qrscanner;

import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class History extends Activity {

	private SharedPreferences history;
	private SharedPreferences favorites;
	
	public ListView lvList;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		// Button listeners
		this.setButtonListeners();
		
		// Shared preferences references
		this.history = getSharedPreferences("history", MODE_PRIVATE);
		this.favorites = getSharedPreferences("favorites", MODE_PRIVATE);
		
		@SuppressWarnings("unchecked")
		Map<String, String> historyMap = (Map<String, String>) this.history.getAll(); 
		
		// listView
		final ListView lvList = (ListView)findViewById(R.id.historyListView);
		
		// ArrayAdapter for listView
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1 );
		adapter.addAll( historyMap.values() );
		lvList.setAdapter( adapter );
		
		lvList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick( AdapterView<?> adapterView, View parentView, int pos, long rowId ) {
				// TODO Auto-generated method stub
				final TextView text = (TextView)parentView.findViewById(android.R.id.text1);
				final String contents = text.getText().toString();
				
				AlertDialog.Builder adBuilder = new AlertDialog.Builder( History.this );
				adBuilder.setTitle("Choose Action");
				adBuilder.setMessage("Which of the following would you like to do with this history item? " + contents);
				adBuilder.setCancelable( true );
				
				
				
				adBuilder.setPositiveButton("Open In Browser", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// open in browser code
						Intent iBrowser = new Intent( Intent.ACTION_VIEW );
						iBrowser.setData( Uri.parse( contents ) );
						History.this.startActivity( iBrowser );
					}
				});
				
				adBuilder.setNeutralButton("Add To Favorites", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// save to favorites
						SharedPreferences.Editor spEditor = History.this.favorites.edit();
						spEditor.putString( contents, contents );
						spEditor.commit();
						
						dialog.dismiss();
					}
				});
				
				adBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// find in history sharedPref, and delete
						SharedPreferences.Editor spEditor = History.this.history.edit();
						spEditor.remove( contents );
						spEditor.commit();
						
						adapter.remove( contents );
						adapter.notifyDataSetChanged();
						
						lvList.setAdapter( adapter );
						
						dialog.dismiss();
					}
				});
				
				AlertDialog aDialog = adBuilder.create();
		        aDialog.show();
				
			}
			
		});
		
	}

	public void setButtonListeners() {
		// TODO Auto-generated method stub
		
		// Button references
		Button clearHistory 	= (Button)this.findViewById(R.id.clearTagsButton);
		Button gotoFavorites 	= (Button)this.findViewById(R.id.goToFavorites);
		Button gotoScanner 		= (Button)this.findViewById(R.id.goToScanner);
		
		final ListView lvList = (ListView)findViewById(R.id.historyListView);
		//@SuppressWarnings("unchecked")
		//final ArrayAdapter<String> adapter = (ArrayAdapter<String>)lvList.getAdapter();
		
		// Clear history listener
		clearHistory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				
				AlertDialog.Builder adBuilder = new AlertDialog.Builder( History.this );
				adBuilder.setTitle("Confirm");
				adBuilder.setMessage( "Are you sure you want to clear ALL of your history? (Are you hiding something?)" );
				adBuilder.setCancelable( false );
				
				adBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// find in history sharedPref, and delete
						SharedPreferences.Editor spEditor = History.this.history.edit();
						spEditor.clear();
						spEditor.commit();
						
						@SuppressWarnings("unchecked")
						ArrayAdapter<String> adapter = (ArrayAdapter<String>) lvList.getAdapter();
						
						//if( adapter != null ) {						
							adapter.clear();
							adapter.notifyDataSetChanged();
							
							lvList.setAdapter( adapter );
						//}
						
						//dialog.dismiss();
					}
				});
				
				adBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// just dismiss this
						//dialog.dismiss();
					}
				});
				
				AlertDialog aDialog = adBuilder.create();
		        aDialog.show();
				
			}
			
		});
		
		// Go to favorites listener
		gotoFavorites.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				
				Intent iFavorites = new Intent( History.this, Favorites.class );
				History.this.startActivity( iFavorites );
				
			}
			
		});
		
		// Go to QR Scanner
		gotoScanner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				
				Intent iScanner = new Intent( History.this, MainActivity.class );
				History.this.startActivity( iScanner );
				
			}
			
		});
		
	}

}
