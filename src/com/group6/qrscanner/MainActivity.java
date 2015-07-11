package com.group6.qrscanner;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	/**
	 * @var history
	 * 
	 * Holds an Array of previously scanned QR codes (resolved URLs).
	 */
	private SharedPreferences history;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Button listeners
		this.setButtonListeners();		
		
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(intent, 0);
		
		this.history = getSharedPreferences("history", MODE_PRIVATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	   if (requestCode == 0) {
	      if (resultCode == RESULT_OK) {
	         final String contents = intent.getStringExtra("SCAN_RESULT");
	         //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	         
	         SharedPreferences.Editor spEditor = this.history.edit();
	         spEditor.putString( contents , contents );
	         spEditor.commit();
	         
	     // Display alert dialog - do we want to open the URL in the browser?
	         AlertDialog.Builder adBuilder = new AlertDialog.Builder( this );
	         adBuilder.setTitle("Confirm");
	         adBuilder.setMessage("Do you want to open this in the browser?\nURL:" + contents );
	         adBuilder.setCancelable( false );
	         
	     // Click yes => open browser
	         adBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// We want to go to our history, regardless
					Intent iHistory = new Intent( MainActivity.this, History.class );
			        startActivity( iHistory );
					
					Intent iBrowser = new Intent( Intent.ACTION_VIEW );
					iBrowser.setData( Uri.parse( contents ) );
					startActivity( iBrowser );
				}
				
			});
	         
	     // Click no => go to history
	         adBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent iHistory = new Intent( MainActivity.this, History.class );
						startActivity( iHistory );
					}
					
				});
	         
	         AlertDialog aDialog = adBuilder.create();
	         aDialog.show();
	         
	         // Handle successful scan
	      } else if (resultCode == RESULT_CANCELED) {
	         // Handle cancel
	      }
	   }
	}
	
	public void setButtonListeners() {
		// TODO Auto-generated method stub
		
		// Button references
		Button gotoFavorites= (Button)this.findViewById(R.id.gotoFavorites);
		Button gotoHistory 	= (Button)this.findViewById(R.id.gotoHistory);
		Button gotoScanner 	= (Button)this.findViewById(R.id.gotoScanner);
		
		
		// Go to history
		gotoHistory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				
				Intent iHistory = new Intent( MainActivity.this, History.class );
				MainActivity.this.startActivity( iHistory );
				
			}
			
		});
		
		// Go to QR Scanner
		gotoScanner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				
				Intent iScanner = new Intent( MainActivity.this, MainActivity.class );
				MainActivity.this.startActivity( iScanner );
				
			}
			
		});
		
		// Go to favorites
		gotoFavorites.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				
				Intent iFavorites = new Intent( MainActivity.this, Favorites.class );
				MainActivity.this.startActivity( iFavorites );
				
			}
			
		});
		
	}

}
