package es.edu.android.restdroid.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
	private static final int DDBB_VERSION = 4;
	
	public MySQLiteHelper(Context context) {
		super(context, "servidores", null, DDBB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "" +
				" CREATE TABLE servidores ( " +
					" idxservidor INTEGER PRIMARY KEY AUTOINCREMENT, " +
					" nombre TEXT NOT NULL, " +
					" host TEXT NOT NULL, " +
					" campo TEXT, " +
					" valor TEXT " +
				" ) ";
				
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = " ALTER TABLE servidores ADD COLUMN orden INTEGER ";
				
		db.execSQL(sql);
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
				" SELECT nombre, host, campo, valor " +
				" FROM servidores " +
				" WHERE nombre like '" + nombre + "' " +
				" ORDER BY orden ";
		
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			try {
				if (!queryResult.containsKey("nombre") && !queryResult.containsKey("host")) {
					queryResult.put("nombre", c.getString(c.getColumnIndexOrThrow("nombre")));
					queryResult.put("host", c.getString(c.getColumnIndexOrThrow("host")));
				}
				campos.put(c.getString(c.getColumnIndexOrThrow("campo")), c.getString(c.getColumnIndexOrThrow("valor")));
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
		if (campos != null && campos.size() > 0) {
			int i = 1;
			for (String campo : campos.keySet()) {
				String valor = campos.get(campo);
				sql = "" +
					" INSERT INTO servidores " +
					" VALUES ( " +
					"	null, '" + 
						nombre + "', '" +
						host + "', '" +
						campo + "', '" +
						valor +	"', " +
						i + " ) ";
				db.execSQL(sql);
				i++;
			}
		}
		else {
			sql = "" +
				" INSERT INTO servidores " +
				" VALUES ( " +
				"	null, '" + 
					nombre + "', '" +
					host + "', " +
					" null ) ";
			db.execSQL(sql);
		}
		
		
	}

}
