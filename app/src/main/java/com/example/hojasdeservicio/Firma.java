package com.example.hojasdeservicio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;

public class Firma extends AppCompatActivity {

    private LinearLayout _lnrLFirma;
    private Button _btnAceptarFirma, _btnCancelarFirma, _btnBorrarFirma;
    private CapturarFirma _capturarFirma;
    private int _numServicio;
    private String _descripcion;
    private boolean _creado;
    private SQLiteDatabase _db;
    private DataBase _dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma);

        _dbHelper = new DataBase(getApplicationContext());

        _numServicio = getIntent().getIntExtra("numServicio", 0);
        _descripcion = getIntent().getStringExtra("descripcion");
        _creado = false;

        _lnrLFirma = findViewById(R.id.LnrLFirma);

        _btnAceptarFirma = findViewById(R.id.BtnAceptarFirma);
        _btnBorrarFirma = findViewById(R.id.BtnBorrarFirma);
        _btnCancelarFirma = findViewById(R.id.BtnCancelarFirma);

        _capturarFirma = new CapturarFirma(this, null);
        _lnrLFirma.addView(_capturarFirma, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        _btnBorrarFirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _capturarFirma.clearCanvas();
            }
        });

        _btnCancelarFirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Captura.class);
                intent.putExtra("numServicio", _numServicio);
                intent.putExtra("descripcion", _descripcion);
                intent.putExtra("creado", _creado);
                //intent.c
                startActivity(intent);
                finish();
            }
        });

        _btnAceptarFirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _db = _dbHelper.getWritableDatabase();
                if(_db != null){
                    ContentValues cv = new ContentValues();
                    cv.put("firma", _capturarFirma.getBytes());
                    Calendar cal = Calendar.getInstance();
                    Date date = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    String fecha = DateFormat.format("yyyy-MM-dd", date).toString();
                    cv.put("fecha_fin", fecha);
                    _db.update("servicios", cv, "num_servicio = ?", new String[]{_numServicio + ""});
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}