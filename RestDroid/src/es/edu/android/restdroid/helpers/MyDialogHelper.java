package es.edu.android.restdroid.helpers;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.style.LineBackgroundSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import es.edu.android.restdroid.R;
import es.edu.android.restdroid.activities.RestDroidActivity;
import es.edu.android.restdroid.handlers.MyOnClickHandler;
import es.edu.android.restdroid.interfaces.Constants;

public class MyDialogHelper extends DialogFragment {
	MyOnClickHandler clickHandler;
	RestDroidActivity parent;
	String tipoDialogo;
	
	public MyDialogHelper(RestDroidActivity parent, String tipoDialogo) {
		this.parent = parent;
		this.clickHandler = new MyOnClickHandler(this.parent);
		this.tipoDialogo = tipoDialogo;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		if (tipoDialogo.equals(Constants.DIALOG_LIMPIAR_CAMPOS)) {
			builder.setMessage("¿Desea limpiar los campos?");
			builder.setPositiveButton("Borrar Campos", new DialogInterface.OnClickListener() {
				@Override
	           public void onClick(DialogInterface dialog, int id) {
					clickHandler.cleanFields(parent.tableCampos);
	           }
	       });
		}
		else {
			MySQLiteHelper sqlHelper = new MySQLiteHelper(parent);
			if (tipoDialogo.equals(Constants.DIALOG_GUARDAR_SERVIDOR)) {
				builder.setTitle("Guardar el Servidor");
				LayoutInflater inflater = getActivity().getLayoutInflater();
				final View view = inflater.inflate(R.layout.dialog_new_element, null);
				builder.setView(view);
				builder.setPositiveButton("Guardar Servidor", new DialogInterface.OnClickListener() {
					@Override
		           public void onClick(DialogInterface dialog, int id) {
						String name = ((EditText) view.findViewById(R.id.dialog_server_name)).getText().toString();
						clickHandler.guardarServidor(name);
		           }
		       });
			}
			else if (tipoDialogo.equals(Constants.DIALOG_CARGAR_SERVIDOR)) {
				final ArrayList<String> servidores = sqlHelper.obtenerServidores();
				String[] nombreServidores = new String[servidores.size()];
				final ArrayList<String> servidor = new ArrayList<String>();
				builder.setTitle("Selecciona un servidor");
				if (nombreServidores.length > 0) {
					for (int i=0; i<servidores.size(); i++) {
						nombreServidores[i] = servidores.get(i);
					}
					builder.setSingleChoiceItems(nombreServidores, -1, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							servidor.add(servidores.get(which));
						}
					});
					builder.setPositiveButton("Cargar Servidor", new DialogInterface.OnClickListener() {
						@Override
			           public void onClick(DialogInterface dialog, int id) {
			        	   clickHandler.cargarServidor(servidor.get(0));
			           }
			       });
				}
				else {
					builder.setMessage("No se ha encontrado nigún servidor que cargar");
				}
			}
		}
		
	   builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		   @Override
           public void onClick(DialogInterface dialog, int id) {
			   MyDialogHelper.this.getDialog().cancel();
           }
       });
	   
	    // Create the AlertDialog object and return it
	   return builder.create();
	}
	
	
}
