package es.edu.android.restdroid.handlers;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import es.edu.android.restdroid.R;
import es.edu.android.restdroid.activities.RestDroidActivity;
import es.edu.android.restdroid.helpers.MySQLiteHelper;
import es.edu.android.restdroid.helpers.RestHelper;
import es.edu.android.restdroid.interfaces.Constants;
import es.edu.android.restdroid.utils.Utils;

/**
 * Clase manejadora de los eventos de click
 * 
 * @author e.astolfi
 */
public class MyOnClickHandler implements OnClickListener {
	RestDroidActivity parent;
	
	//Se inicia la clase padre para la que interesa manejar los eventos [actualmente solo admite una]	
	public MyOnClickHandler(RestDroidActivity parent) {
		this.parent = parent;
	}
	
	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.btnSend)
			enviar();
		else if (v.getId() == R.id.imgAddCampos)
			mostrarTablaCampos();
		else if (v.getId() == parent.ADD_FIELD_ID)
			addCampoFromButton();
		else if (v.getId() == R.id.menu_modif_server_save)
			return;
		else if (v.getId() == R.id.menu_modif_server_edit)
			return;
		else if (v.getId() == R.id.menu_modif_server_delete)
			return;
		else	//Hay que dejar el boton de borrar fila en el else, ya que no hay manera de obtener su id
			removeCampoFromButton(v.getId());
			
			
	}
	
	/**
	 * Hace una llamada mediante servicio REST 
	 */
	private void enviar() {
		String URI = "";
		//Se comprueba que se ha introducido un host
		if (parent.txtHost.getText() != null && !parent.txtHost.getText().toString().isEmpty()) {
			//Se forma la URI mediante el host, el tipo de protocolo y los parametros
			URI = Utils.formarURI(parent.tableCampos, parent.PROTOCOL, parent.txtHost.getText().toString());
//			URI += PROTOCOL.toLowerCase() + "://";
//			URI += txtHost.getText().toString();
//			Toast.makeText(RestDroidActivity.this, URI, Toast.LENGTH_SHORT).show();
			Log.d("URI", URI);
			
			//Se lanza la llamada al servicio REST
			RestHelper restHelper = new RestHelper(parent);
			restHelper.executeREST(URI);
		}
		else {
			parent.txtHost.setError("Este campo es obligatorio");
		}
	}
	
	/**
	 * Muestra la tabla que contiene los campos que se envian como parametros
	 */
	private void mostrarTablaCampos() {
		TableLayout table = parent.tableCampos;
		//Si la tabla esta vacia, se añade un campo
		if (table.getChildCount() == 0) {
			addCampos(table);
			table.setVisibility(View.GONE);		//Se especifica la visibilidad para que posteriormente la cambie bien
		}
		swichTableFieldsState(null);
	}
	
	/**
	 * Añade un campo a continuacion del boton pulsado
	 */
	private void addCampoFromButton() {
		//Se obtiene el boton seleccionado y la fila en la que esta
		ImageButton addFieldsImg = (ImageButton) parent.findViewById(parent.ADD_FIELD_ID);
		TableRow row = (TableRow) addFieldsImg.getParent();
		//Se elminia el boton de añadir de esa fila
		row.removeView(addFieldsImg);
		
		//Se añade el nuevo par de campos 
		addCampos((TableLayout) row.getParent());
	}
	
	/**
	 * Elimina la fila del boton pulsado
	 */
	private void removeCampoFromButton(int idRemoveButton) {
		//Se obtiene el boton pulsado, la fila y la tabla en la que esta
		ImageButton removeFieldsImg = (ImageButton) parent.findViewById(idRemoveButton);
		TableRow row = (TableRow) removeFieldsImg.getParent();
		TableLayout t = (TableLayout) row.getParent();
		
		//Se comprueba si es la ultima fila, y si hay mas de una
		if (row.getChildCount() == 4 && t.getChildCount() > 1) {
			//Se obtiene la fila anterior [mejorar]
			int pos = t.getChildCount() - 2; //de N elementos, el ultimo sera N-1, y el penultimo N-2
			TableRow prevRow = (TableRow) t.getChildAt(pos);
			ImageButton addFieldsImg = (ImageButton) parent.findViewById(parent.ADD_FIELD_ID);
			ImageButton prevRemoveFields = (ImageButton) prevRow.getChildAt(2);
			prevRow.removeViewAt(2);
			row.removeView(addFieldsImg);
			prevRow.addView(addFieldsImg);
			prevRow.addView(prevRemoveFields);
		}
		t.removeView(row);
		
		//Si al borrar no quedan mas filas, se cambia la imagen del boton para mostrar la tabla
		if (t.getChildCount() == 0) {
			parent.imgAddCampos.setImageResource(android.R.drawable.arrow_down_float);	//cambiar a la funcion
		}
	}
	
	public void cleanFields(TableLayout tableParent) {
		swichTableFieldsState(false);
		tableParent.removeAllViews();
		
		ListAdapter adapter = ((ListView) parent.findViewById(R.id.lstNoticias)).getAdapter();
		switchMenuItems(Constants.TAG_NUEVO);
		//clear list view
	}
	
	//Añade uno a uno => no hay map de campos ni se limpia la tabla antes
	private void addCampos(TableLayout tableParent) {
		addCampos(tableParent, null, false);
	}
	
	private void addCampos(TableLayout tableParent, HashMap<String, String> campos, boolean cleanFirst) {
		int nCampos = 1;
		if (campos != null) {
			nCampos = campos.size();
		}
		
		if (cleanFirst) cleanFields(tableParent);
		
		//Se añade los campos
		for (int n=0; n<nCampos; n++) {
			//Se crea una fila por cada par de campos (campo/valor)
			TableRow row = new TableRow(parent);
			
			String textField = "";
			String textVal = "";
			//Si ha llegado algun campo, se recoge el contenido del campo y su valor (si tiene)
			if (campos != null) {
				String key = "";
				if (campos.keySet().toArray()[n] != null) {
					key = campos.keySet().toArray()[n].toString();
				}
				textField = key;
				if (campos.get(key) != null) textVal = campos.get(key);
			}
			
			//Se crean las propiedades de los campos
			TableRow.LayoutParams textParams = new TableRow.LayoutParams();
			textParams.width = 150;
			textParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			textParams.weight = 2.5f;
			
			//Se crea el campo, se le asignan las propiedades y se añade a la fila
			EditText txtField = new EditText(parent);
			txtField.setText(textField);
			txtField.setLayoutParams(textParams);
			txtField.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
			txtField.requestFocus();
			row.addView(txtField);
				
			
			//Se crea el campo de valor, se le asignan las propiedades y se añade a la fila
			EditText txtVal = new EditText(parent);
			txtVal.setText(textVal.equals("null")?"":textVal);
			txtVal.setLayoutParams(textParams);
			row.addView(txtVal);
			
			//Se crean las propiedades de los botones (añadir uno / borrar)
			TableRow.LayoutParams imageParams = new TableRow.LayoutParams();
			imageParams.width = TableRow.LayoutParams.WRAP_CONTENT;
			imageParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			imageParams.weight = 1f;
			
			//Si se esta añadiendo el ultimo par de campos, agregamos el boton de añadir
			if (n == nCampos-1) {
				ImageButton addFieldsImg = new ImageButton(parent);
				parent.ADD_FIELD_ID = Utils.generateViewId();
				addFieldsImg.setId(parent.ADD_FIELD_ID);
				addFieldsImg.setOnClickListener(this);
				addFieldsImg.setImageResource(android.R.drawable.ic_menu_add);
				addFieldsImg.setLayoutParams(imageParams);
				
				row.addView(addFieldsImg);
			}
			
			//Se añade el boton de borrar
			ImageButton removeFieldsImg = new ImageButton(parent);
			parent.REMOVE_FIELD_ID = Utils.generateViewId();
			removeFieldsImg.setId(parent.REMOVE_FIELD_ID);
			removeFieldsImg.setOnClickListener(this);
			removeFieldsImg.setImageResource(android.R.drawable.ic_menu_delete);
			removeFieldsImg.setLayoutParams(imageParams);
			
			row.addView(removeFieldsImg);
			
			//Se añade la fila a la tabla
			tableParent.addView(row);
		}
	}
	
	private void swichTableFieldsState(Boolean stateVisible) {
		TableLayout table = (TableLayout) parent.findViewById(R.id.tableCampos);
		if (stateVisible == null) {
			if (table.getVisibility() == View.GONE) {
				table.setVisibility(View.VISIBLE);
				parent.imgAddCampos.setImageResource(android.R.drawable.arrow_up_float);
			}
			else {
				table.setVisibility(View.GONE);
				parent.imgAddCampos.setImageResource(android.R.drawable.arrow_down_float);
			}
		}
		else {
			if (stateVisible) {
				table.setVisibility(View.VISIBLE);
				parent.imgAddCampos.setImageResource(android.R.drawable.arrow_up_float);
			}
			else {
				table.setVisibility(View.GONE);
				parent.imgAddCampos.setImageResource(android.R.drawable.arrow_down_float);
			}
		}
	}
	
	private void switchMenuItems(String tag) {
		if (tag.equals(Constants.TAG_NUEVO)) {
			parent.mainLayout.setTag(Constants.TAG_NUEVO);
			parent.menu.getItem(0).setVisible(true);
			parent.menu.getItem(1).setVisible(false);
		}
		else if (tag.equals(Constants.TAG_EDITANDO)) {
			parent.mainLayout.setTag(Constants.TAG_EDITANDO);
			parent.menu.getItem(0).setVisible(false);
			parent.menu.getItem(1).setVisible(true);
		}
		
	}
	
	public void cargarServidor(String nombre) {
		MySQLiteHelper sqlHelper = new MySQLiteHelper(parent);
		HashMap<String, Object> result = sqlHelper.obtenerServidorPorNombre(nombre);
		
//		Toast.makeText(parent, result.get("nombre").toString(), Toast.LENGTH_SHORT).show();
		
		parent.txtHost.setText(result.get("host").toString());
		HashMap<String, String> campos = (HashMap<String, String>) result.get("campos");
//		result.get("campos");
		if (campos != null && campos.size() > 0 && campos.get(0) != null) {
			addCampos(parent.tableCampos, campos, true);
			swichTableFieldsState(true);
		}
		
		parent.mainLayout.setTag("LOADED");
		switchMenuItems(Constants.TAG_EDITANDO);
	}
	
	public void guardarServidor(String nombre, boolean edit) {
		MySQLiteHelper sqlHelper = new MySQLiteHelper(parent);
		
		String host = parent.txtHost.getText().toString();
		LinkedHashMap<String, String> campos = new LinkedHashMap<String, String>();
		
		TableLayout tableCampos = (TableLayout) parent.findViewById(R.id.tableCampos);
		if (tableCampos.getChildCount() > 0) {
			for (int i=0; i<tableCampos.getChildCount(); i++) {
				TableRow row = (TableRow) tableCampos.getChildAt(i);
				String campo = ((EditText) row.getChildAt(0)).getText().toString();
				String valor = ((EditText) row.getChildAt(1)).getText().toString();
				if (campo != null && !campo.isEmpty()) {
					if (valor.isEmpty()) valor = null;
					campos.put(campo, valor);
				}
			}
		}
		
		if (edit)
			sqlHelper.actualizarServidor(nombre, host, campos);
		else
			sqlHelper.guardarServidor(nombre, host, campos);
			
		switchMenuItems(Constants.TAG_EDITANDO);
	}

}
