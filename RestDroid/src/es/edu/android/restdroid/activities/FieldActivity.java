package es.edu.android.restdroid.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import es.edu.android.restdroid.R;

public class FieldActivity extends Activity {
	Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_field);
		
		ctx = this;
		
		Button bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TableLayout tabla = (TableLayout) findViewById(R.id.tabla);
				
				TableRow row = new TableRow(ctx);
				EditText t = new EditText(ctx);
				EditText t2 = new EditText(ctx);
				ImageButton i = new ImageButton(ctx);
				
				final float scale = ctx.getResources().getDisplayMetrics().density;
				int pixels = (int) (10 * scale + 0.5f);
				TableRow.LayoutParams pR = new TableRow.LayoutParams();
				TableRow.LayoutParams ipR = new TableRow.LayoutParams();
				pR.width = 150;
				pR.height = TableRow.LayoutParams.WRAP_CONTENT;
				pR.weight = 2.5f;
				ipR.width = TableRow.LayoutParams.WRAP_CONTENT;
				ipR.height = TableRow.LayoutParams.WRAP_CONTENT;
				ipR.weight = 1f;
				
				i.setImageResource(android.R.drawable.ic_input_add);
//				t2.setLayoutParams(pR);
//				t2.setEms(10);
				
				row.addView(t, pR);
				row.addView(t2, pR);
				row.addView(i, ipR);
				tabla.addView(row);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_field, menu);
		return true;
	}

}
