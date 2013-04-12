package es.edu.android.restdroid.interfaces;

public interface TableScripts {
	public static final String CREAR_TABLA_SERVIDORES = "" +
									" CREATE TABLE IF NOT EXISTS servidores ( " +
									" idxservidor INTEGER PRIMARY KEY AUTOINCREMENT, " +
									" nombre TEXT NOT NULL, " +
									" host TEXT NOT NULL " +
								" ); ";
	
	public static final String CREAR_TABLA_CAMPOS_SERVIDOR = "" +
									" CREATE TABLE IF NOT EXISTS campos_servidor ( " +
									" idxcampo INTEGER PRIMARY KEY AUTOINCREMENT, " +
									" idservidor INTEGER NOT NULL, " +
									" campo TEXT, " +
									" valor TEXT, " +
									" orden INTEGER, " +
									" FOREIGN KEY(idservidor) REFERENCES servidores(idxservidor) " +
								" ); ";
	
	public static final String DROP_TABLA_SERVIDORES = " DROP TABLE servidores; ";
	public static final String DROP_TABLA_CAMPO_SERVIDOR = " DROP TABLE campos_servidor; ";
}
