package es.edu.android.restdroid.beans;

import java.util.ArrayList;

public class ServidorBean {
	
	private String nombre;
	
	private String host;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public ArrayList<CampoBean> getCampos() {
		return campos;
	}

	public void setCampos(ArrayList<CampoBean> campos) {
		this.campos = campos;
	}

	private ArrayList<CampoBean> campos;
	
}
