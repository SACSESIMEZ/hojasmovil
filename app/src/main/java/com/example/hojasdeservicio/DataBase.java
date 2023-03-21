package com.example.hojasdeservicio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {

    private static final String _baseDatos = "servicio_movil";

    private static final int _dbVersion = 1;

    private static final String _tablaLugares = "CREATE TABLE lugares(id_lugar INTEGER PRIMARY KEY AUTOINCREMENT, lugar TEXT NOT NULL)";
    private static final String _tablaTipoElementos = "CREATE TABLE tipo_elementos(id_tipo INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT NOT NULL)";
    private static final String _tablaServicios = "CREATE TABLE servicios(num_servicio INTEGER PRIMARY KEY AUTOINCREMENT, persona_reporta TEXT NOT NULL, correo_electronico TEXT, descripcion TEXT NOT NULL, fecha_ini TEXT NOT NULL, fecha_fin TEXT, id_lugar INTEGER NOT NULL, firma BLOB, evidencia_uno BLOB, evidencia_dos BLOB, evidencia_tres BLOB, FOREIGN KEY(id_lugar) REFERENCES lugares(id_lugar) ON DELETE CASCADE)";
    private static final String _tablaInventario = "CREATE TABLE inventario(id_elemento INTEGER PRIMARY KEY AUTOINCREMENT, marca TEXT NOT NULL, modelo TEXT NOT NULL, num_serie TEXT NOT NULL, id_tipo INTEGER NOT NULL, FOREIGN KEY (id_tipo) REFERENCES tipo_elementos(id_tipo) ON DELETE CASCADE)";
    private static final String _tablaComputadoras = "CREATE TABLE computadoras(id_computadora INTEGER PRIMARY KEY AUTOINCREMENT, id_elemento INTEGER NOT NULL, ram TEXT NOT NULL, disco_duro TEXT NOT NULL, so TEXT NOT NULL, FOREIGN KEY (id_elemento) REFERENCES inventario(id_elemento) ON DELETE CASCADE)";
    private static final String _tablaConexiones = "CREATE TABLE conexiones(id_conexion INTEGER PRIMARY KEY AUTOINCREMENT, id_elemento INTEGER NOT NULL, ip TEXT NOT NULL, mac TEXT NOT NULL, FOREIGN KEY (id_elemento) REFERENCES inventario(id_elemento) ON DELETE CASCADE)";
    private static final String _tablaEquiposInstitucionales = "CREATE TABLE equipos_institucionales(id_equipo INTEGER PRIMARY KEY AUTOINCREMENT, id_elemento INTEGER NOT NULL, cambs TEXT NOT NULL, FOREIGN KEY (id_elemento) REFERENCES inventario(id_elemento) ON DELETE CASCADE)";
    private static final String _tablaServicioInventario = "CREATE TABLE servicio_inventario(id_servicio_inventario INTEGER PRIMARY KEY AUTOINCREMENT, num_servicio INTEGER NOT NULL, id_servicio INTEGER NOT NULL, id_elemento INTEGER NOT NULL, FOREIGN KEY (id_elemento) REFERENCES inventario(id_elemento) ON DELETE CASCADE, FOREIGN KEY (num_servicio, id_servicio) REFERENCES serviciOS(num_servicio, id_servicio) ON DELETE CASCADE)";

    public DataBase(@Nullable Context context) {
        super(context, _baseDatos, null, _dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(_tablaLugares);
        sqLiteDatabase.execSQL(_tablaTipoElementos);
        sqLiteDatabase.execSQL(_tablaServicios);
        sqLiteDatabase.execSQL(_tablaInventario);
        sqLiteDatabase.execSQL(_tablaComputadoras);
        sqLiteDatabase.execSQL(_tablaConexiones);
        sqLiteDatabase.execSQL(_tablaEquiposInstitucionales);
        sqLiteDatabase.execSQL(_tablaServicioInventario);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
