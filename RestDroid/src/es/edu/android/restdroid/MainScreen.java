package es.edu.android.restdroid;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainScreen extends Activity implements OnItemSelectedListener, OnClickListener {
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	private int ADD_FIELD_ID, REMOVE_FIELD_ID;
	String PROTOCOL = "HTTP";
//	private AdView adView;
	final String[] HOSTS = {"project-livec948f4df9f63.rhcloud.com/noticias", "eu.battle.net/api/wow/character/colinas-pardas/riverwindd"};
	Button btnEnviar;
	EditText txtHost;
	ListView list;
	ImageButton imgAddCampos;
	Spinner spnHTTP;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		getSupportActionBar().setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_main_screen);
		
		
		/*********ADMOB***********/
//		adView = (AdView) findViewById(R.id.adMob);		
//		adView.loadAd(new AdRequest());
		/*************************/
		
		
		txtHost = (EditText) findViewById(R.id.txtHostUrl);
//		txtHost.setText("http://socialmusic.eastolfi.c9.io/noticias");
//		txtHost.setText("socialmusic.eastolfi.c9.io");
//		txtHost.setText("project-livec948f4df9f63.rhcloud.com/noticias");
		txtHost.setText("eu.battle.net/api/wow");
		
		spnHTTP = (Spinner) findViewById(R.id.spnHTTP);
		ArrayAdapter<CharSequence> spnAdapter = 
				ArrayAdapter.createFromResource(this, R.array.protocolArray, android.R.layout.simple_spinner_item);
		spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnHTTP.setAdapter(spnAdapter);
		spnHTTP.setOnItemSelectedListener(this);
		
		imgAddCampos = (ImageButton) findViewById(R.id.imgAddCampos);
		imgAddCampos.setOnClickListener(this);
		
		list = (ListView) findViewById(R.id.lstNoticias);
		
		btnEnviar = (Button) findViewById(R.id.btnSend);
		btnEnviar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String URI = "";
				if (txtHost.getText() != null && !txtHost.getText().toString().isEmpty()) {
					URI = formarURI();
//					URI += PROTOCOL.toLowerCase() + "://";
//					URI += txtHost.getText().toString();
//					Toast.makeText(MainScreen.this, URI, Toast.LENGTH_SHORT).show();
					Log.d("URI", URI);
					
					RestHelper restHelper = new RestHelper(MainScreen.this);
					restHelper.executeREST(URI);
				}
				else {
					txtHost.setError("Este campo es obligatorio");
				}
//				restHelper.cargarNoticias("user1");
//				RestHandler rest = new RestHandler(MainScreen.this);
//				rest.execute(txtHost.getText().toString());
			}
		});
		btnEnviar.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				startActivity(new Intent(MainScreen.this, FieldActivity.class));
//				setContentView(R.layout.boceto);
				return true;
			}
		});
	}
	
	protected String formarURI() {
		String uri = "";
		
		uri += PROTOCOL.toLowerCase() + "://";
		uri += txtHost.getText().toString();
		
		TableLayout tableCampos = (TableLayout) findViewById(R.id.tableCampos);
		for (int i = 0; i < tableCampos.getChildCount(); i++) {
			TableRow row = (TableRow) tableCampos.getChildAt(i);
			EditText campo = (EditText) row.getChildAt(0);
			EditText valor = (EditText) row.getChildAt(1);
			
			uri += "/" + StringUtils.uncapitalize(
						campo.getText().toString());
		}
		
		return uri;
	}

	@Override
	public void onClick(View v) {
		Log.d("onClick", Integer.toString(v.getId()));
		
		if (v.getId() == R.id.imgAddCampos) {
			TableLayout table = (TableLayout) findViewById(R.id.tableCampos);
			if (table.getChildCount() > 0) {
				if (table.getVisibility() == View.GONE) {
					table.setVisibility(View.VISIBLE);
					imgAddCampos.setImageResource(android.R.drawable.arrow_up_float);
				}
				else {
					table.setVisibility(View.GONE);
					imgAddCampos.setImageResource(android.R.drawable.arrow_down_float);
				}
			}
			else {
				addPostFields(table);
				
				imgAddCampos.setImageResource(android.R.drawable.arrow_up_float);
			}
		}
		else if (v.getId() == ADD_FIELD_ID) {
			ImageButton addFieldsImg = (ImageButton) findViewById(ADD_FIELD_ID);
			TableRow row = (TableRow) addFieldsImg.getParent();
			row.removeView(addFieldsImg);
			
			addPostFields((TableLayout) row.getParent());
		}
		else {//if (v.getId() == REMOVE_FIELD_ID) {
			ImageButton removeFieldsImg = (ImageButton) findViewById(v.getId());
			TableRow row = (TableRow) removeFieldsImg.getParent();
			TableLayout t = (TableLayout) row.getParent();
			if (row.getChildCount() == 4 && t.getChildCount() > 1) {
				int pos = t.getChildCount() - 2; //de N elementos, el ultimo sera N-1, y el penultimo N-2
				TableRow prevRow = (TableRow) t.getChildAt(pos);
				ImageButton addFieldsImg = (ImageButton) findViewById(ADD_FIELD_ID);
				ImageButton prevRemoveFields = (ImageButton) prevRow.getChildAt(2);
				prevRow.removeViewAt(2);
				row.removeView(addFieldsImg);
				prevRow.addView(addFieldsImg);
				prevRow.addView(prevRemoveFields);
			}
			t.removeView(row);
			if (t.getChildCount() == 0) {
				imgAddCampos.setImageResource(android.R.drawable.arrow_down_float);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_screen, menu);
		return true;
	}
	
	private void addPostFields(TableLayout tableParent) {
		addPostFields(tableParent, null);
	}

	private void addPostFields(TableLayout tableParent, HashMap<String, String> campos) {
		int nCampos = 1;
		if (campos != null) {
			nCampos = campos.size();
		}
		
		for (int n=0; n<nCampos; n++) {
			TableRow row = new TableRow(MainScreen.this);
			
			String textField = "";
			String textVal = "";
			if (campos != null) {
				String key = campos.keySet().toArray()[n].toString();
				textField = key;
				if (campos.get(key) != null) textVal = campos.get(key);
			}
			
			TableRow.LayoutParams textParams = new TableRow.LayoutParams();
			textParams.width = 150;
			textParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			textParams.weight = 2.5f;
			
			EditText txtField = new EditText(MainScreen.this);
			txtField.setText(textField);
			txtField.setLayoutParams(textParams);
			row.addView(txtField);
			
			EditText txtVal = new EditText(MainScreen.this);
			txtVal.setText(textVal);
			txtVal.setLayoutParams(textParams);
			row.addView(txtVal);
			
			TableRow.LayoutParams imageParams = new TableRow.LayoutParams();
			imageParams.width = TableRow.LayoutParams.WRAP_CONTENT;
			imageParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			imageParams.weight = 1f;
			
			ImageButton addFieldsImg = new ImageButton(MainScreen.this);
			ADD_FIELD_ID = generateViewId();
			addFieldsImg.setId(ADD_FIELD_ID);
			addFieldsImg.setOnClickListener(this);
			addFieldsImg.setImageResource(android.R.drawable.ic_menu_add);
			addFieldsImg.setLayoutParams(imageParams);
			
			row.addView(addFieldsImg);
			
			ImageButton removeFieldsImg = new ImageButton(MainScreen.this);
			REMOVE_FIELD_ID = generateViewId();
			removeFieldsImg.setId(REMOVE_FIELD_ID);
			removeFieldsImg.setOnClickListener(this);
			removeFieldsImg.setImageResource(android.R.drawable.ic_menu_delete);
			removeFieldsImg.setLayoutParams(imageParams);
			
			row.addView(removeFieldsImg);
			
			tableParent.addView(row);
		}
	}
	
	public static int generateViewId() {
	    for (;;) {
	        final int result = sNextGeneratedId.get();
	        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
	        int newValue = result + 1;
	        if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
	        if (sNextGeneratedId.compareAndSet(result, newValue)) {
	            return result;
	        }
	    }
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
//		adView.destroy();
		super.onDestroy();
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_new_server:
			cargarServidor();
			break;
		case R.id.menu_save_server:
			guardarServidor();
			break;
		case R.id.menu_clear_server:
			
			break;
		case R.id.menu_settings:
			
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void cargarServidor() {
		MySQLiteHelper sqlHelper = new MySQLiteHelper(MainScreen.this);
		HashMap<String, Object> result = sqlHelper.obtenerServidorPorNombre("WOW");
		
		Toast.makeText(MainScreen.this, result.get("nombre").toString(), Toast.LENGTH_SHORT).show();
		
		txtHost.setText(result.get("host").toString());
		
		addPostFields((TableLayout) findViewById(R.id.tableCampos), (HashMap) result.get("campos"));
	}
	
	private void guardarServidor() {
		MySQLiteHelper sqlHelper = new MySQLiteHelper(MainScreen.this);
		
		String nombre = "WOW";
		String host = txtHost.getText().toString();
		SortedMap<String, String> campos = new TreeMap<String, String>();
		
		TableLayout tableCampos = (TableLayout) findViewById(R.id.tableCampos);
		if (tableCampos.getChildCount() > 0) {
			for (int i=0; i<tableCampos.getChildCount(); i++) {
				TableRow row = (TableRow) tableCampos.getChildAt(i);
				String campo = ((EditText) row.getChildAt(0)).getText().toString();
				String valor = ((EditText) row.getChildAt(1)).getText().toString();
				if (campo != null || !campo.isEmpty()) {
					if (valor.isEmpty()) valor = null;
					campos.put(campo, valor);
				}
			}
		}
		
		sqlHelper.guardarServidor(nombre, host, campos);
	}
	
}
