package com.movil.hojasdeservicio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.movil.models.Json;

public class Bienvenida extends AppCompatActivity {

    private EditText edtTIdUsuario;
    private Button btnComenzar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        edtTIdUsuario = findViewById(R.id.EdtTIdUsuario);
        btnComenzar = findViewById(R.id.BtnComenzar);

        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idUsuario = Integer.parseInt(String.valueOf(edtTIdUsuario.getText()), 0);
                try{
                    if(idUsuario != 0){
                        Context context = Bienvenida.this;
                        SharedPreferences sharedPref = context.getSharedPreferences("HojasServicio", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        Json jsonFile = new Json();
                        jsonFile.setIdUsuario(idUsuario);

                        Gson jsonParser = new Gson();
                        String jsonCadena = jsonParser.toJson(jsonFile);

                        editor.putString("JSON", jsonCadena);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else{
                        Toast.makeText(Bienvenida.this, "Complete el campo diferente de 0.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    Toast.makeText(Bienvenida.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(Bienvenida.this, "Complete el campo.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}