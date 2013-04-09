package es.edu.android.restdroid.helpers;

import java.util.HashMap;
import java.util.SortedMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public MySQLiteHelper(Context context) {
		super(context, "servidores", null, 1);
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
		// TODO Auto-generated method stub
	}
	
	public void obtenerServidores() {
		
	}
	
	public HashMap<String,Object> obtenerServidorPorNombre(String nombre) {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<String, Object> queryResult = new HashMap<String, Object>();
		HashMap<String, String> campos = new HashMap<String, String>();
		String sql = "" +
				" SELECT nombre, host, campo, valor " +
				" FROM servidores " +
				" WHERE nombre like '" + nombre + "' ";
		
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			try {
				if (!campos.containsKey("nombre") && !campos.containsKey("host")) {
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
	
	public void guardarServidor(String nombre, String host, SortedMap<String, String> campos) {
		SQLiteDatabase db = getWritableDatabase();
		
		String sql = "";
		if (campos != null && campos.size() > 0) {
			for (String campo : campos.keySet()) {
				String valor = campos.get(campo);
				sql = "" +
					" INSERT INTO servidores " +
					" VALUES ( " +
					"	null, '" + 
						nombre + "', '" +
						host + "', '" +
						campo + "', '" +
						valor +	"' ) ";
				db.execSQL(sql);
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
