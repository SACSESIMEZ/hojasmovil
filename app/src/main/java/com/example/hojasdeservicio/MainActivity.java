package com.example.hojasdeservicio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String _nombre;
    private Button _btnCrearServicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("AppHojasServicio", Context.MODE_PRIVATE);
        _nombre = sp.getString("Nombre", null);

        if(_nombre == null){
            Intent intent = new Intent(this, Bienvenida.class);
            startActivity(intent);
        }

        _btnCrearServicio = findViewById(R.id.BtnCrearServicio);
        _btnCrearServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Firma.class);
                startActivity(intent);
            }
        });

    }
}