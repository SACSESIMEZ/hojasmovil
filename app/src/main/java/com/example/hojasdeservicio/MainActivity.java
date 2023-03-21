package com.example.hojasdeservicio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private String _nombre;
    private Button _btnCrearServicio;
    private TableLayout _tblLListaS;
    private SQLiteDatabase _db;
    private final String _queryServicios = "SELECT lugares.lugar from servicios INNER JOIN lugares ON servicios.id_lugar = lugares.id_lugar";
    //private final String _prueba = "SELECT lugar from lugares";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _tblLListaS = findViewById(R.id.TbLListaS);
        _btnCrearServicio = findViewById(R.id.BtnCrearServicio);
        _btnCrearServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AbrirServicio.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        _tblLListaS.removeAllViews();
        SharedPreferences sp = getSharedPreferences("HojasServicio", Context.MODE_PRIVATE);
        _nombre = sp.getString("Nombre", "");

        if(_nombre == null || _nombre.equalsIgnoreCase("")){
            Intent intent = new Intent(this, Bienvenida.class);
            startActivity(intent);
            finish();
        }

        DataBase dbHelper = new DataBase(getApplicationContext());
        _db = dbHelper.getReadableDatabase();

        if(_db != null){
            Cursor c = _db.rawQuery(_queryServicios, null);

            try{
                if(c != null){
                    while(c.moveToNext()){
                        System.out.println("*******************************************");
                        String lugar = c.getString(0);
                        TableRow fila = new TableRow(getApplicationContext());
                        TextView servicio = new TextView(getApplicationContext());
                        servicio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), Captura.class);
                                startActivity(intent);
                            }
                        });
                        float densidadPixeles = getApplicationContext().getResources().getDisplayMetrics().density;
                        float valorPaddingP = 10*densidadPixeles;
                        float valorHeight = 45*densidadPixeles;
                        servicio.setPadding(0, (int) valorPaddingP, 0, (int) valorPaddingP);
                        servicio.setHeight((int) valorHeight);
                        servicio.setText(lugar);
                        servicio.setTextSize(18);
                        servicio.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        servicio.setBackgroundResource(R.drawable.top_bottom_border);
                        servicio.setTextColor(Color.WHITE);

                        /*TextView texto = new TextView(getApplicationContext());
                        texto.setPadding(0, (int) valorPaddingP, 0, (int) valorPaddingP);
                        texto.setHeight((int) valorHeight);
                        texto.setText("AA");
                        texto.setTextSize(18);
                        texto.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        texto.setBackgroundResource(R.drawable.top_bottom_border);
                        texto.setTextColor(Color.WHITE);
                        fila.addView(texto);
                        */

                        fila.addView(servicio);
                        _tblLListaS.addView(fila);
                    }
                }
            } catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
                /*
                try{
                    if(c.getCount() > 0){
                        c.moveToFirst();
                        do {
                            String lugar = c.getString(1);
                            TableRow fila = new TableRow(getApplicationContext());
                            EditText servicio = new EditText(getApplicationContext());
                            servicio.setText(lugar);
                            fila.addView(servicio);
                            _tblLListaS.addView(fila);
                        } while(c.moveToNext());
                    }
                } catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }*/
            //}
        }
    }
}