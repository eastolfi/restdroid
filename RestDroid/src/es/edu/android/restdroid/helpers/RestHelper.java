package es.edu.android.restdroid.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.edu.android.restdroid.R;
import es.edu.android.restdroid.R.id;
import es.edu.android.restdroid.activities.RestDroidActivity;
import es.edu.android.restdroid.adpters.MyAdapter;
import es.edu.android.restdroid.handlers.RestHandler;
import es.edu.android.restdroid.interfaces.UriConstants;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class RestHelper {
	RestHandler restHandler;
	RestDroidActivity parent;
	
//	public RestHelper() {}
	
	public RestHelper(RestDroidActivity parent) {
		this.restHandler = new RestHandler(parent);
		this.parent = parent;
	}
	
	public void cargarNoticias(String usuario) {
		HttpUriRequest get = new HttpGet(UriConstants.SOCIAL_MUSIC_NOTICIAS + "/" + usuario);
		get.setHeader("Accept", "application/json");
		get.setHeader("Content-Type", "application/json");
		
		String callback = "onResultCargarNoticias";
		restHandler.execute(get, callback);
	}
	
	public void logIn(String correo, String pswd) {
		
	}
	
	public void registrarse(String correo, String pswd) {
		
	}

	public void onResultCargarNoticias(String result) {
		Log.d("onResultCargarNoticias", result);
		
		try {
			JSONArray jArray = new JSONArray(result);
			
			ArrayList<String> lst = new ArrayList<String>();
			for (int i = 0; i < jArray.length(); i++) {
//				JSONObject jObj = jArray.getJSONObject(i);
//				lst.add(jObj.getString("fecha"));
				lst.add(jArray.getJSONObject(i).toString());
			}
			ArrayAdapter<String> adapter =
					new ArrayAdapter<String>(parent, R.id.lstNoticias);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void executeREST(String uri) {
		HttpUriRequest req = new HttpGet(uri);
		
		String callback = "onResultREST";
		restHandler.execute(req, callback);
	}
	
	public void onResultREST(String result) {
		Log.d("onResultREST", result);
		JSONArray jArray = null;
		JSONObject jObject = null;
		ArrayList<HashMap<String, String>> lista = new ArrayList<HashMap<String, String>>();
		ArrayList<String> lst = new ArrayList<String>();
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			jObject = new JSONObject(result);
			
			jArray = new JSONArray();
			jArray.put(jObject);
			
		} catch (JSONException e) {
			Log.d(e.getLocalizedMessage(), "Error Controlado; Continua la ejecucion");
			//Do nothing
		}
		
		try {
			if (jObject == null) jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jObj = jArray.getJSONObject(i);
				for (Iterator iterator = jObj.keys(); iterator.hasNext();) {
					String string = (String) iterator.next();
					lst.add(jObj.get(string).toString());
					map.put(string, jObj.getString(string).toString());
				}
				lista.add(map);
//				lst.add(jObj.getString("fecha"));
//				lst.add(jArray.getJSONObject(i).toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			map.put("Error", result);
			lista.add(map);
		}
//		ArrayAdapter<String> adapter =
//				new ArrayAdapter<String>(parent, android.R.layout.simple_list_item_1, lst);
		ListAdapter adapter = new MyAdapter(parent, lista);
		
		ListView lstResult = (ListView) parent.findViewById(R.id.lstNoticias);
		lstResult.setAdapter(adapter);
	}
	
}
