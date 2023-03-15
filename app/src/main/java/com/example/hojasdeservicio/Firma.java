package com.example.hojasdeservicio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Firma extends AppCompatActivity {

    private LinearLayout _lnrLFirma;
    private Button _btnAceptarFirma, _btnCancelarFirma, _btnBorrarFirma;
    private CapturarFirma _capturarFirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma);

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
    }
}