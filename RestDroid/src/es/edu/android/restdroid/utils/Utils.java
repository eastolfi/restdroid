package es.edu.android.restdroid.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Utils {
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	final String[] HOSTS = {"project-livec948f4df9f63.rhcloud.com/noticias", "eu.battle.net/api/wow/character/colinas-pardas/riverwindd"};
	
	/**
	 * Genera un id unico para asociar a una vista
	 * 
	 * @return id generado
	 */
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
	
	/**
	 * Monta la URI que se utilizara para llamar al servicio REST
	 * 
	 * @param tableCampos -> tabla que contiene los campos que se enviaran como parametros
	 * @param protocolo (http / https)
	 * @param host
	 * 
	 * @return URI
	 */
	public static String formarURI(TableLayout tableCampos, String protocolo, String host) {
		String uri = "";
		
		//Se concatena el protocolo y el host
		uri += protocolo.toLowerCase() + "://" + host;
		
		//Por cada campo en la tabla, se añade a la URI [faltan los campos con valor]
		for (int i = 0; i < tableCampos.getChildCount(); i++) {
			TableRow row = (TableRow) tableCampos.getChildAt(i);
			EditText campo = (EditText) row.getChildAt(0);
//			EditText valor = (EditText) row.getChildAt(1);
			
			uri += "/" + StringUtils.uncapitalize(campo.getText().toString());
		}
		
		return uri;
	}
}
