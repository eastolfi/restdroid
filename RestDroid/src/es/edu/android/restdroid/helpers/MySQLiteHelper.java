package es.edu.android.restdroid.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import es.edu.android.restdroid.interfaces.TableScripts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
	private static final int DDBB_VERSION = 6;
	
	public MySQLiteHelper(Context context) {
		super(context, "servidores", null, DDBB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TableScripts.CREAR_TABLA_SERVIDORES);
		db.execSQL(TableScripts.CREAR_TABLA_CAMPOS_SERVIDOR);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(TableScripts.DROP_TABLA_CAMPO_SERVIDOR);
		db.execSQL(TableScripts.DROP_TABLA_SERVIDORES);
		db.execSQL(TableScripts.CREAR_TABLA_SERVIDORES);
		db.execSQL(TableScripts.CREAR_TABLA_CAMPOS_SERVIDOR);
	}
	
	public ArrayList<String> obtenerServidores() {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<String> result = new ArrayList<String>();
		
		String sql = "" +
				" SELECT DISTINCT nombre " +
				" FROM servidores ";
		
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			try {
				result.add(c.getString(0));
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public HashMap<String,Object> obtenerServidorPorNombre(String nombre) {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<String, Object> queryResult = new HashMap<String, Object>();
		LinkedHashMap<String, String> campos = new LinkedHashMap<String, String>();
		String sql = "" +
				" SELECT serv.nombre AS Nombre, serv.host AS Host, c_s.campo AS Campo, c_s.valor AS Valor " +
				" FROM servidores AS serv" +
				" LEFT JOIN campos_servidor AS c_s" +
				" ON c_s.idxcampo = serv.idxservidor " +
				" WHERE serv.nombre like '" + nombre + "' " +
				" ORDER BY c_s.orden ";
		
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			try {
				if (!queryResult.containsKey("nombre") && !queryResult.containsKey("host")) {
					queryResult.put("nombre", c.getString(c.getColumnIndexOrThrow("Nombre")));
					queryResult.put("host", c.getString(c.getColumnIndexOrThrow("Host")));
				}
				campos.put(c.getString(c.getColumnIndexOrThrow("Campo")), c.getString(c.getColumnIndexOrThrow("Valor")));
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		queryResult.put("campos", campos);
		
		return queryResult;
	}
	
	public void guardarServidor(String nombre, String host, LinkedHashMap<String, String> campos) {
		SQLiteDatabase db = getWritableDatabase();
		
		String sql = "";
		sql = "" +
			" INSERT INTO servidores (nombre, host) " +
			" VALUES ( " +
			"	null, '" + 
				nombre + "', '" +
				host + "'); ";
		db.execSQL(sql);
		
		String storedSql = " SELECT idxservidor FROM servidores WHERE nombre = '" + nombre + "'; ";
		Cursor storedCursor = db.rawQuery(storedSql, null);
		
		int storedId = -1;
		while (storedCursor.moveToNext()) {
			storedId = storedCursor.getInt(0);
		}
		
		if (campos != null && campos.size() > 0) {
			int i = 1;
			for (String campo : campos.keySet()) {
				String valor = campos.get(campo);
				sql = "" +
					" INSERT INTO campos_servidor (idservidor, campo, valor, orden) " +
					" VALUES ( " +
						storedId + ", " +
						campo + "', '" +
						valor + "', " +
						i +	" ); ";
				db.execSQL(sql);
				i++;
			}
		}
	}
	
	public void actualizarServidor(String nombre, String host, LinkedHashMap<String, String> campos) {
		SQLiteDatabase db = getWritableDatabase();
		
//		String sql = "";
//		if (campos != null && campos.size() > 0) {
//			int i = 1;
//			for (String campo : campos.keySet()) {
//				String valor = campos.get(campo);
//				sql = "" +
//					" UPDATE TABLE servidores " +
//					" SET " +
//						"nombre = '" + nombre + "', '" +
//						host + "', '" +
//						campo + "', '" +
//						valor +	"', " +
//						i + " ) ";
//				db.execSQL(sql);
//				i++;
//			}
//		}
//		else {
//			sql = "" +
//				" UPDATE TABLE servidores " +
//				" SET " +
//					"host = '" + host + "' ";
//			db.execSQL(sql);
//		}
	}

}
