package com.example.hojasdeservicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AbrirServicio extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ConstraintLayout _cnsLContenedor;
    private EditText _edtTPersona, _edtTCorreo, _edtTMDescripcion, _edtTOtro;
    private TextView _txtVPersona;
    private Button _btnCrear;
    private Spinner _spnLugares;
    private int _itemSpinnerSeleccionado;

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


        _btnCrear.setOnClickListener(view -> {
            try {
                boolean bandera = !String.valueOf(_edtTPersona.getText()).equals("") && !String.valueOf(_edtTMDescripcion.getText()).equals("") && (_itemSpinnerSeleccionado != 0);
                if(_edtTOtro.getVisibility() == View.VISIBLE){
                    bandera = bandera && !String.valueOf(_edtTOtro.getText()).equals("");
                }
                if(bandera){
                    Intent intent = new Intent(this, Captura.class);
                    startActivity(intent);
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
        //String item = String.valueOf(adapterView.getSelectedItem());
        if(_itemSpinnerSeleccionado == 86){
            _edtTOtro.setVisibility(View.VISIBLE);
        } else {
            _edtTOtro.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}