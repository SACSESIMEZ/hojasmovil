package com.movil.hojasdeservicio;

import static java.lang.System.in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.movil.models.Json;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    private int idUsuario;
    private String json;
    private Button btnCrearServicio;
    private TableLayout tblLListaServicios;
    private SQLiteDatabase _db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tblLListaServicios = findViewById(R.id.TbLListaServicios);
        btnCrearServicio = findViewById(R.id.BtnCrearServicio);
        btnCrearServicio.setOnClickListener(new View.OnClickListener() {
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
            tblLListaServicios.removeAllViews();
            SharedPreferences sp = getSharedPreferences("HojasServicio", Context.MODE_PRIVATE);
            json = sp.getString("JSON", "");

            if(json == null || json.equalsIgnoreCase("")){
                Intent intent = new Intent(this, Bienvenida.class);
                startActivity(intent);
                finish();
            }

            InputStream inputStream = new ByteArrayInputStream(json.getBytes());
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            reader.beginArray();
            reader.hasNext();
            Json json = gson.fromJson(reader, Json.class);


            while(c.moveToNext()){
                int numServicio = c.getInt(0);
                String fechaFin = c.getString(1);
                String fechaIni = c.getString(2);
                String lugar = c.getString(3);
                TableRow fila = new TableRow(getApplicationContext());
                fila.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), Captura.class);
                        intent.putExtra("numServicio", numServicio);
                        if(fechaFin != null){
                            intent.putExtra("terminado", true);
                        }
                        startActivity(intent);
                    }
                });

                TextView txtVlugar = new TextView(getApplicationContext());
                float densidadPixeles = getApplicationContext().getResources().getDisplayMetrics().density;
                float valorPaddingP = 10*densidadPixeles;
                float valorHeight = 45*densidadPixeles;
                txtVlugar.setPadding((int) valorPaddingP, (int) valorPaddingP, (int) valorPaddingP, (int) valorPaddingP);
                txtVlugar.setHeight((int) valorHeight);
                txtVlugar.setText(lugar);
                txtVlugar.setTextSize(18);
                txtVlugar.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                txtVlugar.setBackgroundResource(R.drawable.top_bottom_border);
                txtVlugar.setTextColor(Color.WHITE);

                TextView txtVFecha = new TextView(getApplicationContext());
                txtVFecha.setPadding((int) valorPaddingP, (int) valorPaddingP, (int) valorPaddingP, (int) valorPaddingP);
                txtVFecha.setHeight((int) valorHeight);
                txtVFecha.setText(fechaIni);
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

                if(fechaFin == null){
                    fila.setBackgroundColor(Color.parseColor("#FFCC80"));
                } else{
                    fila.setBackgroundColor(Color.parseColor("#92FD70"));
                }

                tblLListaServicios.addView(fila, params);
            }
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}