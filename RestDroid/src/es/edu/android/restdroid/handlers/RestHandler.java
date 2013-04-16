package es.edu.android.restdroid.handlers;

import java.lang.reflect.Method;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import es.edu.android.restdroid.R;
import es.edu.android.restdroid.activities.RestDroidActivity;
import es.edu.android.restdroid.helpers.RestHelper;

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
	
	@Override
	protected String doInBackground(Object... params) {
		String response = "";
		callbackStr = (String) params[1];
		try {
			HttpUriRequest request = (HttpUriRequest) params[0];
			HttpResponse execute = client.execute(request);
			response = EntityUtils.toString(execute.getEntity());
			
//			return response;
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
