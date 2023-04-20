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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String _nombre;
    private Button _btnCrearServicio;
    private TableLayout _tblLListaS;
    private SQLiteDatabase _db;

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
        try {
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
                Cursor c = _db.query("servicios", new String[]{"num_servicio"}, null, null, null, null, "num_servicio DESC");
                try{
                    if(c != null){
                        while(c.moveToNext()){
                            int numServicio = c.getInt(0);
                            Servicio servicio = new Servicio(getApplicationContext());
                            servicio.setNumServicio(numServicio);
                            servicio.setInformacion();
                            TableRow fila = new TableRow(getApplicationContext());
                            fila.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), Captura.class);
                                    intent.putExtra("servicio", servicio);
                                    if(servicio.getFechaFin() != null){
                                        intent.putExtra("terminado", true);
                                    }
                                    //intent.c
                                    startActivity(intent);
                                }
                            });

                            TextView txtVlugar = new TextView(getApplicationContext());
                            float densidadPixeles = getApplicationContext().getResources().getDisplayMetrics().density;
                            float valorPaddingP = 10*densidadPixeles;
                            float valorHeight = 45*densidadPixeles;
                            txtVlugar.setPadding((int) valorPaddingP, (int) valorPaddingP, (int) valorPaddingP, (int) valorPaddingP);
                            txtVlugar.setHeight((int) valorHeight);
                            txtVlugar.setText(servicio.getLugar());
                            txtVlugar.setTextSize(18);
                            txtVlugar.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            txtVlugar.setBackgroundResource(R.drawable.top_bottom_border);
                            txtVlugar.setTextColor(Color.WHITE);

                            TextView txtVFecha = new TextView(getApplicationContext());
                            txtVFecha.setPadding((int) valorPaddingP, (int) valorPaddingP, (int) valorPaddingP, (int) valorPaddingP);
                            txtVFecha.setHeight((int) valorHeight);
                            txtVFecha.setText(servicio.getFechaIni());
                            txtVFecha.setTextSize(18);
                            txtVFecha.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            txtVFecha.setBackgroundResource(R.drawable.top_bottom_border);
                            txtVFecha.setTextColor(Color.WHITE);

                            fila.addView(txtVFecha);
                            fila.addView(txtVlugar);

                            TableRow.LayoutParams params = new TableRow.LayoutParams(
                                    TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, (int) 100, 0, (int) valorPaddingP);

                            //fila.setLayoutParams(params);

                            if(servicio.getFechaFin() == null){
                                fila.setBackgroundColor(Color.parseColor("#FFCC80"));
                            } else{
                                fila.setBackgroundColor(Color.parseColor("#92FD70"));
                            }

                            _tblLListaS.addView(fila, params);
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}