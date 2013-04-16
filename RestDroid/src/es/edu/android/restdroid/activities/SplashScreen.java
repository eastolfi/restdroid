package es.edu.android.restdroid.activities;

import es.edu.android.restdroid.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class SplashScreen extends FragmentActivity implements OnClickListener {
	Button btPrev, btNext, btClose;
	final String SPLASHPREF = "SplashScrennNeededPreference";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_1);
		setFinishOnTouchOutside(false);
		
		btPrev = (Button) findViewById(R.id.btPrevious);
		btNext = (Button) findViewById(R.id.btNext);
		btClose = (Button) findViewById(R.id.btClose);
		
		btPrev.setOnClickListener(this);
		btNext.setOnClickListener(this);
		btClose.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = mPrefs.edit();
		edit.putBoolean(SPLASHPREF, false);
		edit.commit();
		finish();
	}
	
	@Override
	public void onBackPressed() {
	    // your code.
	}
	
}
