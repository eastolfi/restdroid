package es.edu.android.restdroid.builder;

import java.util.ArrayList;

import android.database.Cursor;
import es.edu.android.restdroid.beans.CampoBean;
import es.edu.android.restdroid.beans.ServidorBean;

public class ServidorBuilder {
	
	public static ServidorBean buildFromQuery(Cursor cursor) {
		ServidorBean bean = new ServidorBean();
		
		ArrayList<CampoBean> campos = new ArrayList<CampoBean>();
		int i = 1;
		while (cursor.moveToNext()) {
			try {
				if (bean.getNombre() == null) {
					bean.setNombre(cursor.getString(cursor.getColumnIndex("Nombre")));
				}
				if (bean.getHost() == null) {
					bean.setHost(cursor.getString(cursor.getColumnIndex("Host")));
				}
				CampoBean campo = new CampoBean();
				campo.setCampo(cursor.getString(cursor.getColumnIndex("Campo")));
				campo.setValor(cursor.getString(cursor.getColumnIndex("Valor")));
				campo.setOrden(i);
				
				campos.add(campo);
				
				i++;
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			bean.setCampos(campos);
		}
		
		
		return bean;
	}
	
}
