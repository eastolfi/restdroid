package es.edu.android.restdroid.helpers;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import es.edu.android.restdroid.R;
import es.edu.android.restdroid.activities.RestDroidActivity;
import es.edu.android.restdroid.handlers.MyOnClickHandler;
import es.edu.android.restdroid.interfaces.Constants;

public class MyDialogHelper extends DialogFragment {
	MyOnClickHandler clickHandler;
	RestDroidActivity parent;
	String tipoDialogo;

	public MyDialogHelper() {
		this.parent = new RestDroidActivity();
		this.clickHandler = new MyOnClickHandler(this.parent);
		this.tipoDialogo = Constants.DIALOG_DEFAULT;
	}

	public MyDialogHelper(RestDroidActivity parent, String tipoDialogo) {
		this.parent = parent;
		this.clickHandler = new MyOnClickHandler(this.parent);
		this.tipoDialogo = tipoDialogo;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		if (tipoDialogo.equals(Constants.DIALOG_LIMPIAR_CAMPOS)) {
			builder = crearDialogoLimpiar(builder);
		}
		else if (tipoDialogo.equals(Constants.DIALOG_GUARDAR_SERVIDOR)) {
			builder = crearDialogoGuardar(builder);
		}
		else if (tipoDialogo.equals(Constants.DIALOG_CARGAR_SERVIDOR)) {
			builder = crearDialogoCargar(builder);
		}
		else if (tipoDialogo.equals(Constants.DIALOG_EDITAR_SERVIDOR)) {
			builder = crearDialogoEditar(builder);
		}
		else if (tipoDialogo.equals(Constants.DIALOG_BORRAR_SERVIDOR)) {
			builder	= crearDialogoBorrar(builder);
		}

		// Create the AlertDialog object and return it
		return builder.create();
	}

	private Builder crearDialogoLimpiar(Builder builder) {
		builder.setMessage("¿Desea limpiar los campos?");
		builder.setPositiveButton("Borrar Campos", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				clickHandler.cleanFields(parent.tableCampos);
			}
		});
		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				MyDialogHelper.this.getDialog().cancel();
			}
		});
		
		return builder;
	}
	private Builder crearDialogoGuardar(Builder builder) {
		builder.setTitle("Guardar el Servidor");
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_new_element, null);
		builder.setView(view);
		builder.setPositiveButton("Guardar Servidor", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				EditText t = (EditText) view.findViewById(R.id.dialog_server_name);
				if (t.getText().toString().isEmpty()) {
					t.setError("Este campo es obligatorio.");
				}
				else {
					clickHandler.guardarServidor(t.getText().toString(), false);
				}
			}
		});
		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				MyDialogHelper.this.getDialog().cancel();
			}
		});
		
		return builder;
	}
	private Builder crearDialogoCargar(Builder builder) {
		MySQLiteHelper sqlHelper = new MySQLiteHelper(parent);

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
					if (servidor.size() > 0)
						clickHandler.cargarServidor(servidor.get(0));
				}
			});
		}
		else {
			builder.setMessage("No se ha encontrado nigún servidor que cargar");
		}

		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				MyDialogHelper.this.getDialog().cancel();
			}
		});
		
		return builder;
	}
	private Builder crearDialogoEditar(Builder builder) {
		
		
		
		return builder;
	}
	private Builder crearDialogoBorrar(Builder builder) {

		builder.setMessage("Está a punto de borrar el servidor '" + parent.activeServer.getNombre() +  "'. ¿Desea continuar?");
		
		builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MySQLiteHelper sqlHelper = new MySQLiteHelper(parent);
				MyOnClickHandler onClick = new  MyOnClickHandler(parent);
				sqlHelper.borrarServidor(parent.activeServer);
				onClick.cleanFields(parent.tableCampos);
			}
		});
		
		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				MyDialogHelper.this.getDialog().cancel();
			}
		});
		
		return builder;
	}


}
