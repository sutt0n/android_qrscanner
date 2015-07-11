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
import android.widget.ListView;
import android.widget.TextView;

public class Favorites extends Activity {

	private SharedPreferences history;
	private SharedPreferences favorites;
	
	public ListView lvList;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		
		// Button listeners
		this.setButtonListeners();		
		
		// Shared preferences references
		this.history = getSharedPreferences("history", MODE_PRIVATE);
		this.favorites = getSharedPreferences("favorites", MODE_PRIVATE);
		
		@SuppressWarnings("unchecked")
		Map<String, String> favoritesMap = (Map<String, String>) this.favorites.getAll(); 
		
		// listView
		final ListView lvList = (ListView)findViewById(R.id.favoritesListView);
		
		// ArrayAdapter for listView
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1 );
		adapter.addAll( favoritesMap.values() );
		lvList.setAdapter( adapter );
		
		lvList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick( AdapterView<?> adapterView, View parentView, int pos, long rowId ) {
				// TODO Auto-generated method stub
				final TextView text = (TextView)parentView.findViewById(android.R.id.text1);
				final String contents = text.getText().toString();
				
				AlertDialog.Builder adBuilder = new AlertDialog.Builder( Favorites.this );
				adBuilder.setTitle("Choose Action");
				adBuilder.setMessage("Which of the following would you like to do with this history item? " + contents);
				adBuilder.setCancelable( true );
				
				
				
				adBuilder.setPositiveButton("Open In Browser", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// open in browser code
						Intent iBrowser = new Intent( Intent.ACTION_VIEW );
						iBrowser.setData( Uri.parse( contents ) );
						Favorites.this.startActivity( iBrowser );
					}
				});
				
				adBuilder.setNegativeButton("Remove From Favorites", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// save to favorites
						// find in history sharedPref, and delete
						SharedPreferences.Editor spEditor = Favorites.this.favorites.edit();
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
		Button gotoHistory 	= (Button)this.findViewById(R.id.gotoHistory);
		Button gotoScanner 	= (Button)this.findViewById(R.id.goToScanner);
		
		// Go to favorites listener
		gotoHistory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				
				Intent iFavorites = new Intent( Favorites.this, History.class );
				Favorites.this.startActivity( iFavorites );
				
			}
			
		});
		
		// Go to QR Scanner
		gotoScanner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				
				Intent iScanner = new Intent( Favorites.this, MainActivity.class );
				Favorites.this.startActivity( iScanner );
				
			}
			
		});
		
	}

}
