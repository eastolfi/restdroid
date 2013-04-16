package es.edu.android.restdroid.activities;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import es.edu.android.restdroid.R;
import es.edu.android.restdroid.beans.CampoBean;
import es.edu.android.restdroid.beans.ServidorBean;
import es.edu.android.restdroid.handlers.MyAdMobListener;
import es.edu.android.restdroid.handlers.MyOnClickHandler;
import es.edu.android.restdroid.helpers.MyDialogHelper;
import es.edu.android.restdroid.interfaces.Constants;

public class RestDroidActivity extends FragmentActivity implements OnItemSelectedListener {
	public int ADD_FIELD_ID;
	public int REMOVE_FIELD_ID;
	public String PROTOCOL = "HTTP";
	private AdView adView;
	final String[] HOSTS = {"project-livec948f4df9f63.rhcloud.com/noticias", "eu.battle.net/api/wow/character/colinas-pardas/riverwindd"};
	Button btnEnviar;
	public EditText txtHost;
	ListView list;
	public ImageButton imgAddCampos;
	Spinner spnHTTP;
	public TableLayout tableCampos;
	MyOnClickHandler myClickHandler;
	public LinearLayout mainLayout;
	public Menu menu; 
	public ServidorBean activeServer;
	SharedPreferences mPrefs;
	final String SPLASHPREF = "SplashScrennNeededPreference";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

	    // second argument is the default to use if the preference can't be found
	    Boolean needSplashScreen = mPrefs.getBoolean(SPLASHPREF, true);

	    if (needSplashScreen) {
	    	Intent i = new Intent(this, SplashScreen.class);
	    	startActivity(i);
	    }
		
		myClickHandler = new MyOnClickHandler(this);
		activeServer = new ServidorBean();
		activeServer.setCampos(new ArrayList<CampoBean>());
		
		/*********ADMOB***********/
		adView = (AdView) findViewById(R.id.adMob);
		adView.setAdListener(new MyAdMobListener());
		AdRequest req = new AdRequest();
		req.addTestDevice(AdRequest.TEST_EMULATOR);
		req.addTestDevice("3F252345D0CD0DC7EB1C7BC94EE1E1C6");
		adView.loadAd(req);
		/*************************/
		
		mainLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
		mainLayout.setTag(Constants.TAG_NUEVO);
		
		tableCampos = (TableLayout) findViewById(R.id.tableCampos);
		
		txtHost = (EditText) findViewById(R.id.txtHostUrl);
//		txtHost.setText("http://socialmusic.eastolfi.c9.io/noticias");
//		txtHost.setText("socialmusic.eastolfi.c9.io");
//		txtHost.setText("project-livec948f4df9f63.rhcloud.com/noticias");
		txtHost.setText("eu.battle.net/api/wow");
		
		spnHTTP = (Spinner) findViewById(R.id.spnHTTP);
		ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(this, R.array.protocolArray, android.R.layout.simple_spinner_item);
		spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnHTTP.setAdapter(spnAdapter);
		spnHTTP.setOnItemSelectedListener(this);
		
		imgAddCampos = (ImageButton) findViewById(R.id.imgAddCampos);
		imgAddCampos.setOnClickListener(myClickHandler);
		
		list = (ListView) findViewById(R.id.lstNoticias);
		
		btnEnviar = (Button) findViewById(R.id.btnSend);
		btnEnviar.setOnClickListener(myClickHandler);
		btnEnviar.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
//				if (mainLayout.getTag().equals(Constants.TAG_NUEVO)) {
//					mainLayout.sett
//				}
//				menu.getItem(0).setVisible(false);
//				menu.getItem(1).setVisible(true);
//				startActivity(new Intent(RestDroidActivity.this, FieldActivity.class));
//				setContentView(R.layout.boceto);
				return true;
			}
		});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_screen, menu);
		
		this.menu = menu;
		
		return true;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		PROTOCOL = parent.getItemAtPosition(pos).toString();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		MyDialogHelper dialog;
		switch (item.getItemId()) {
		case R.id.menu_save_server:
			dialog = new MyDialogHelper(this, Constants.DIALOG_GUARDAR_SERVIDOR);
			dialog.show(this.getSupportFragmentManager(), "");
			break;
		case R.id.menu_modif_server_save:
			dialog = new MyDialogHelper(this, Constants.DIALOG_EDITAR_SERVIDOR);
			dialog.show(this.getSupportFragmentManager(), "");
			break;
		case R.id.menu_modif_server_delete:
			dialog = new MyDialogHelper(this, Constants.DIALOG_BORRAR_SERVIDOR);
			dialog.show(this.getSupportFragmentManager(), "");
			break;
		case R.id.menu_load_server:
			dialog = new MyDialogHelper(this, Constants.DIALOG_CARGAR_SERVIDOR);
			dialog.show(this.getSupportFragmentManager(), "");
			break;
		case R.id.menu_clear_server:
			dialog = new MyDialogHelper(this, Constants.DIALOG_LIMPIAR_CAMPOS);
			dialog.show(this.getSupportFragmentManager(), "");
			break;
		case R.id.menu_settings:
			Toast.makeText(this, "Ajustes", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if (menu != null) {
			if (mainLayout.getTag().equals(Constants.TAG_EDITANDO)) {
				menu.getItem(0).setVisible(false);
				menu.getItem(1).setVisible(true);
			}
			else if (mainLayout.getTag().equals(Constants.TAG_NUEVO)) {
				menu.getItem(0).setVisible(true);
				menu.getItem(1).setVisible(false);
			}
		}
		super.onResume();
	}
	
}
