package com.example.hojasdeservicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AbrirServicio extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ConstraintLayout _cnsLContenedor;
    private EditText _edtTPersona, _edtTCorreo, _edtTMDescripcion, _edtTOtro;
    private TextView _txtVPersona;
    private Button _btnCrear;
    private Spinner _spnLugares;
    private int _itemSpinnerSeleccionado;
    private DataBase _dbHelper;
    private SQLiteDatabase _db;
    private String _lugarSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_servicio);

        _cnsLContenedor = findViewById(R.id.CnsLContenedor);
        _edtTCorreo = findViewById(R.id.EdtTCorreo);
        _edtTPersona = findViewById(R.id.EdtTPersona);
        _edtTMDescripcion = findViewById(R.id.EdtTMDescripcion);
        _btnCrear = findViewById(R.id.BtnCrear);
        _spnLugares = findViewById(R.id.SpnLugares);
        _txtVPersona = findViewById(R.id.TxtVQuienReporta);

        ConstraintSet set = new ConstraintSet();
        _edtTOtro = new EditText(getApplicationContext());
        _edtTOtro.setId(View.generateViewId());
        _edtTOtro.setBackgroundResource(R.drawable.edit_text_rounded);
        _edtTOtro.setHint("Cúbiculo x");
        _edtTOtro.setHintTextColor(Color.argb(50, 100, 100, 100));
        _edtTOtro.setTextColor(Color.BLACK);
        _edtTOtro.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
        float densidadPixeles = getApplicationContext().getResources().getDisplayMetrics().density;
        //float densidadEscalada = getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        float valorPaddingP = 5*densidadPixeles;
        float valorMarginTopP = 20*densidadPixeles;
        float valorMarginStartP = 40*densidadPixeles;
        //float valorTextSizeP = 20*densidadEscalada;
        float valorWidthP = 309*densidadPixeles;
        _edtTOtro.setPadding((int) valorPaddingP, 0, (int) valorPaddingP, 0);
        _edtTOtro.setWidth((int) valorWidthP);
        _edtTOtro.setTextSize(20);
        //edtTOtro.layout(40, 20, 0, 0);
        _cnsLContenedor.addView(_edtTOtro);
        set.clone(_cnsLContenedor);
        set.connect(_edtTOtro.getId(), ConstraintSet.TOP, _spnLugares.getId(), ConstraintSet.BOTTOM, (int) valorMarginTopP);
        set.connect(_edtTOtro.getId(), ConstraintSet.START, _cnsLContenedor.getId(), ConstraintSet.START, (int) valorMarginStartP);
        set.connect(_txtVPersona.getId(), ConstraintSet.TOP, _edtTOtro.getId(), ConstraintSet.BOTTOM, (int) valorMarginTopP);
        set.applyTo(_cnsLContenedor);
        _edtTOtro.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lugares, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spnLugares.setAdapter(adapter);
        _spnLugares.setOnItemSelectedListener(this);

        _dbHelper = new DataBase(this);

        _btnCrear.setOnClickListener(view -> {
            try {
                int idLugar = 0;
                boolean bandera = !String.valueOf(_edtTPersona.getText()).equals("") && !String.valueOf(_edtTMDescripcion.getText()).equals("") && (_itemSpinnerSeleccionado != 0);
                if(_edtTOtro.getVisibility() == View.VISIBLE){
                    bandera = bandera && !String.valueOf(_edtTOtro.getText()).equals("");
                    _lugarSeleccionado = String.valueOf(_edtTOtro.getText());
                    insertDB("lugar", _lugarSeleccionado.toUpperCase(), "lugares");
                    Cursor c = selectDB("SELECT id_lugar FROM lugares WHERE lugar = ?", new String[]{_lugarSeleccionado.toUpperCase()});
                    c.moveToNext();
                    idLugar = c.getInt(0);
                } else{
                    idLugar = _itemSpinnerSeleccionado + 1;
                }
                if(bandera){
                    long id = 0;
                    _db = _dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    if(_db != null){
                        cv.put("persona_reporta", String.valueOf(_edtTPersona.getText()));
                        cv.put("correo_electronico", String.valueOf(_edtTCorreo.getText()));
                        cv.put("descripcion_reporte", String.valueOf(_edtTMDescripcion.getText()));
                        Calendar cal = Calendar.getInstance();
                        Date date = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        String fecha = DateFormat.format("yyyy-MM-dd", date).toString();
                        cv.put("fecha_ini", fecha);
                        cv.put("id_lugar", idLugar);

                        id = _db.insert("servicios", null, cv);
                    }

                    Intent intent = new Intent(this, Captura.class);
                    intent.putExtra("creado", true);
                    intent.putExtra("numServicio", id);
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(this, "Complete todos los campos.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex){
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        _itemSpinnerSeleccionado = adapterView.getSelectedItemPosition();
        int max = _spnLugares.getCount();
        //String item = String.valueOf(adapterView.getSelectedItem());
        if(_itemSpinnerSeleccionado == 87){
            _edtTOtro.setVisibility(View.VISIBLE);
        } else {
            _edtTOtro.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private Cursor selectDB(String query, String[] valores){
        Cursor c = _db.rawQuery(query, valores);
        return c;
    }

    private void insertDB(String columna, String valor, String tabla){
        _db = _dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(_db != null){
            cv.put(columna, valor);
        }
        _db.insert(tabla, null, cv);
        cv.clear();
    }

    private void insertDB(String columna, int valor, String tabla){
        _db = _dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(_db != null){
            cv.put(columna, valor);
        }
        _db.insert(tabla, null, cv);
        cv.clear();
    }

    private void insertDB(String columna, String[] valores, String tabla){
        _db = _dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(_db != null){
            for(String valor : valores){
                cv.put(columna, valor);
            }
        }
        _db.insert(tabla, null, cv);
        cv.clear();
    }

    private void llenarSpinnerLugares(){
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.add("Seleccione una opción");
        Cursor c = selectDB("SELECT lugar from lugares", null);
        if(c != null){
            while(c.moveToNext()){
                listSpinner.add(c.getString(0));
            }
            listSpinner.add("OTRO");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            _spnLugares.setAdapter(adapter);
        }
    }
}