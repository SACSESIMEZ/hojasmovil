package com.example.hojasdeservicio;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Bienvenida extends AppCompatActivity {

    private EditText _edtTNombre;
    private Button _btnComenzar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        _edtTNombre = findViewById(R.id.EdtTNombre);
        _btnComenzar = findViewById(R.id.BtnComenzar);

        _btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = String.valueOf(_edtTNombre.getText());
                try{
                    if(!nombre.equalsIgnoreCase("") && nombre != null){
                        Context context = Bienvenida.this;
                        SharedPreferences sharedPref = context.getSharedPreferences("HojasServicio", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("Nombre", nombre);
                        editor.apply();

                        DataBase dbHelper = new DataBase(getApplicationContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        if(db != null){
                            String[] lugares = getResources().getStringArray(R.array.lugares);
                            ContentValues cv = new ContentValues();
                            for(String lugar : lugares){
                                if(!lugar.equalsIgnoreCase("Seleccione una opción") || !lugar.equalsIgnoreCase("OTRO")){
                                    cv.put("lugar", lugar);
                                    db.insert("lugares", null, cv);
                                    cv.clear();
                                }
                            }

                            String[] dispositivos = getResources().getStringArray(R.array.dispositivos);
                            for(String dispositivo : dispositivos){
                                if(!dispositivo.equalsIgnoreCase("Seleccione una opción")) {
                                    cv.put("tipo", dispositivo);
                                    db.insert("tipo_elementos", null, cv);
                                    cv.clear();
                                }
                            }

                            String[] memoriasRAM = getResources().getStringArray(R.array.ram);
                            for(String ram : memoriasRAM){
                                if(!ram.equalsIgnoreCase("Seleccione una opción")) {
                                    cv.put("ram", ram);
                                    db.insert("catalogo_ram", null, cv);
                                    cv.clear();
                                }
                            }

                            String[] discosDuros = getResources().getStringArray(R.array.dd);
                            for(String disco : discosDuros){
                                if(!disco.equalsIgnoreCase("Seleccione una opción")) {
                                    cv.put("disco_duro", disco);
                                    db.insert("catalogo_disco_duro", null, cv);
                                    cv.clear();
                                }
                            }

                            String[] sistemasOperativos = getResources().getStringArray(R.array.so);
                            for(String so : sistemasOperativos){
                                if(!so.equalsIgnoreCase("Seleccione una opción")) {
                                    cv.put("so", so);
                                    db.insert("catalogo_so", null, cv);
                                    cv.clear();
                                }
                            }

                            db.close();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else{
                        Toast.makeText(Bienvenida.this, "Complete el campo.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    Toast.makeText(Bienvenida.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}