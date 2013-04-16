package es.edu.android.restdroid.helpers;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import es.edu.android.restdroid.beans.CampoBean;
import es.edu.android.restdroid.beans.ServidorBean;
import es.edu.android.restdroid.builder.ServidorBuilder;
import es.edu.android.restdroid.interfaces.TableScripts;

public class MySQLiteHelper extends SQLiteOpenHelper {
	private static final int DDBB_VERSION = 7;
	
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
	
	public ServidorBean obtenerServidorPorNombre(String nombre) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "" +
				" SELECT serv.nombre AS Nombre, serv.host AS Host, campos.campo AS Campo, campos.valor AS Valor " +
				" FROM servidores serv" +
				" LEFT JOIN campos_servidor campos" +
				" ON campos.idservidor = serv.idxservidor " +
				" WHERE serv.nombre like '" + nombre + "' " +
				" ORDER BY campos.orden ";
		
		Cursor c = db.rawQuery(sql, null);
		ServidorBean bean = ServidorBuilder.buildFromQuery(c);
		return bean;
	}
	
	public void guardarServidor(String nombre, String host, ArrayList<CampoBean> campos) {
		SQLiteDatabase db = getWritableDatabase();
		
		String sql = "";
		sql = "" +
			" INSERT INTO servidores (nombre, host) " +
			" VALUES ( '" +
				nombre + "', '" +
				host + "'); ";
		db.execSQL(sql);
		
		if (campos != null && campos.size() > 0) {
			String storedSql = " SELECT idxservidor FROM servidores WHERE nombre = '" + nombre + "'; ";
			Cursor storedCursor = db.rawQuery(storedSql, null);
			
			int storedId = -1;
			while (storedCursor.moveToNext()) {
				storedId = Integer.parseInt(storedCursor.getString(0));
			}
			
			int i = 1;
			for (CampoBean campo : campos) {
				sql = "" +
					" INSERT INTO campos_servidor (idservidor, campo, valor, orden) " +
					" VALUES ( " +
						storedId + ", '" +
						campo.getCampo() + "', '" +
						campo.getValor() + "', " +
						i +	" ); "; //Manejar el orden desde el objeto
				db.execSQL(sql);
				i++;
			}
		}
	}
	
	public void actualizarServidor(String nombre, String host, ArrayList<CampoBean> campos) {
		SQLiteDatabase db = getWritableDatabase();
		
		String sql = "";
		sql = "" +
			" UPDATE TABLE servidores " +
			" SET " +
				" nombre = '" + nombre + "', " +
				" host = '" + host + "' " +
			" WHERE nombre = '" + nombre + "'; ";
		db.execSQL(sql);
		
		if (campos != null && campos.size() > 0) {
			String updatedSql = " SELECT idxservidor FROM servidores WHERE nombre = '" + nombre + "'; ";
			Cursor updatedCursor = db.rawQuery(updatedSql, null);
			
			int updatedId = -1;
			while (updatedCursor.moveToNext()) {
				updatedId = Integer.parseInt(updatedCursor.getString(0));
			}
			
			for (CampoBean campo : campos) {
				sql = "" +
					" UPDATE TABLE campos_servidor " +
					" SET " +
						" campo = '" + campo.getCampo() + "', " +
						" valor = '" + campo.getValor() + "' " +
					" WHERE idsevidor = " + updatedId + "; ";
				db.execSQL(sql);
			}
		}
	}
	
	public void borrarServidor(ServidorBean servidor) {
		SQLiteDatabase db = getWritableDatabase();
		
		String nombre = servidor.getNombre();
		
		String sql = " SELECT idxservidor FROM servidores WHERE nombre = '" + nombre + "'; ";
		Cursor c = db.rawQuery(sql, null);
		
		int deleteId = -1;
		while (c.moveToNext()) {
			deleteId = Integer.parseInt(c.getString(0));
		}
		
		String sqlCampos = " DELETE FROM campos_servidor WHERE idservidor = " + deleteId + "; ";
		db.execSQL(sqlCampos);
		
		String sqlServidor = " DELETE FROM servidores where nombre = '" + nombre + "'; ";
		
		db.execSQL(sqlServidor);
	}

}
