package es.edu.android.restdroid.handlers;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import es.edu.android.restdroid.activities.RestDroidActivity;
import es.edu.android.restdroid.helpers.RestHelper;
import es.edu.android.restdroid.utils.Utils;

public class myOnClickHandler implements OnClickListener {
	RestDroidActivity parent;
	
	public myOnClickHandler(RestDroidActivity parent) {
		this.parent = parent;
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
	
	private void enviar() {
		String URI = "";
		if (parent.txtHost.getText() != null && !parent.txtHost.getText().toString().isEmpty()) {
			URI = Utils.formarURI(parent.tableCampos, parent.PROTOCOL, parent.txtHost.getText().toString());
//			URI += PROTOCOL.toLowerCase() + "://";
//			URI += txtHost.getText().toString();
//			Toast.makeText(RestDroidActivity.this, URI, Toast.LENGTH_SHORT).show();
			Log.d("URI", URI);
			
			RestHelper restHelper = new RestHelper(parent);
			restHelper.executeREST(URI);
		}
		else {
			parent.txtHost.setError("Este campo es obligatorio");
		}
	}

}
