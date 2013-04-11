package es.edu.android.restdroid.handlers;

import java.lang.reflect.Method;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import es.edu.android.restdroid.R;
import es.edu.android.restdroid.R.drawable;
import es.edu.android.restdroid.activities.RestDroidActivity;
import es.edu.android.restdroid.helpers.RestHelper;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class RestHandler extends AsyncTask<Object, Integer, String> {	
	HttpClient client;
	RestDroidActivity parent;
	String callbackStr;
	Method callbackMethod;
	ProgressDialog dialog;
	
	public RestHandler(RestDroidActivity parent) {
		this.parent = parent;
		client = new DefaultHttpClient();

	}
	
//	private String doPost(String host) {
//		try {
//			HttpClient client = new DefaultHttpClient();
//			HttpPost post = new HttpPost(host);
//			post.setHeader("Accept", "application/json");
//			post.setHeader("Content-Type", "application/json");
//			
//			JSONObject obj = new JSONObject();
//			obj.put("nombre", "usuClient");
//			obj.put("apellidos", "apellidosClient");
//			obj.put("correo", "correoClient");
//			obj.put("pswd", "qqq");
//			
//			post.setEntity(new StringEntity(obj.toString()));
//			
//			HttpResponse response = client.execute(post);
//			
//			return response.toString();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	@Override
	protected String doInBackground(Object... params) {
		String response = "";
		callbackStr = (String) params[1];
		try {
			HttpUriRequest request = (HttpUriRequest) params[0];
			HttpResponse execute = client.execute(request);
			response = EntityUtils.toString(execute.getEntity());
			
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response = e.getLocalizedMessage();
		}
		
		return response;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void onPostExecute(String result) {
		Class methodClass = RestHelper.class;
		Class methodParamsType[] = {String.class};
		Object methodParams[] = {result};
		Object classInstance;
		try {
			classInstance = methodClass.getConstructor(RestDroidActivity.class).newInstance(parent);
			callbackMethod = methodClass.getDeclaredMethod(callbackStr, methodParamsType);
			callbackMethod.invoke(classInstance, methodParams);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.cancel();
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new ProgressDialog(parent);
		dialog.setIcon(R.drawable.ic_launcher);
		dialog.setMessage("Cargando...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.show();
	}
	
	
}
