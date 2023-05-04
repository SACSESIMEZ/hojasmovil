package com.example.hojasdeservicio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class DataBase extends SQLiteOpenHelper implements Serializable{

    private static final int _dbVersion = 1;

    private static final String _baseDatos = "servicio_movil";
    private static final String _tablaLugares = "CREATE TABLE lugares(id_lugar INTEGER PRIMARY KEY AUTOINCREMENT, lugar TEXT NOT NULL UNIQUE)";
    private static final String _tablaTipoElementos = "CREATE TABLE tipo_elementos(id_tipo INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT NOT NULL)";
    private static final String _tablaServicios = "CREATE TABLE servicios(num_servicio INTEGER PRIMARY KEY AUTOINCREMENT, id_servicio INTEGER, id_lugar INTEGER NOT NULL, persona_reporta TEXT NOT NULL, correo_electronico TEXT NOT NULL, descripcion_reporte TEXT NOT NULL, descripcion_servicio TEXT, fecha_ini TEXT NOT NULL, fecha_fin TEXT, firma BLOB, FOREIGN KEY(id_lugar) REFERENCES lugares(id_lugar) ON DELETE CASCADE)";
    private static final String _tablaInventario = "CREATE TABLE inventario(id_elemento INTEGER PRIMARY KEY AUTOINCREMENT, marca TEXT, modelo TEXT, num_serie TEXT UNIQUE)";
    private static final String _tablaComputadoras = "CREATE TABLE computadoras(id_computadora INTEGER PRIMARY KEY AUTOINCREMENT, id_elemento INTEGER NOT NULL UNIQUE, id_ram INTEGER NOT NULL, id_disco_duro INTEGER NOT NULL, id_so INTEGER NOT NULL, FOREIGN KEY (id_elemento) REFERENCES inventario(id_elemento) ON DELETE CASCADE, FOREIGN KEY (id_ram) REFERENCES catalogo_ram(id_ram) ON DELETE CASCADE, FOREIGN KEY (id_disco_duro) REFERENCES catalogo_disco_duro(id_disco_duro) ON DELETE CASCADE, FOREIGN KEY (id_so) REFERENCES catalogo_so(id_so) ON DELETE CASCADE)";
    private static final String _tablaConexiones = "CREATE TABLE conexiones(id_conexion INTEGER PRIMARY KEY AUTOINCREMENT, id_dispositivo INTEGER NOT NULL, ip TEXT, mac TEXT, FOREIGN KEY (id_dispositivo) REFERENCES dispositivos(id_dispositivo) ON DELETE CASCADE)";
    private static final String _tablaEquiposInstitucionales = "CREATE TABLE equipos_institucionales(id_equipo INTEGER PRIMARY KEY AUTOINCREMENT, id_dispositivo INTEGER NOT NULL, cambs TEXT, FOREIGN KEY (id_dispositivo) REFERENCES dispositivos(id_dispositivo))";
    private static final String _tablaServicioInventarioDispositivos = "CREATE TABLE servicio_inventario_dispositivos(id_servicio_inventario INTEGER PRIMARY KEY AUTOINCREMENT, num_servicio INTEGER NOT NULL, id_dispositivo INTEGER NOT NULL, FOREIGN KEY (id_dispositivo) REFERENCES dispositivos(id_dispositivo), FOREIGN KEY (num_servicio) REFERENCES servicios(num_servicio))";
    private static final String _tablaCatalogoRAM = "CREATE TABLE catalogo_ram(id_ram INTEGER PRIMARY KEY AUTOINCREMENT, ram TEXT NOT NULL)";
    private static final String _tablaCatalogoDD = "CREATE TABLE catalogo_disco_duro(id_disco_duro INTEGER PRIMARY KEY AUTOINCREMENT, disco_duro TEXT NOT NULL)";
    private static final String _tablaCatalogoSO = "CREATE TABLE catalogo_so(id_so INTEGER PRIMARY KEY AUTOINCREMENT, so TEXT NOT NULL)";
    private static final String _tablaDispositivos = "CREATE TABLE dispositivos(id_dispositivo INTEGER PRIMARY KEY AUTOINCREMENT, id_elemento INTEGER, id_tipo INTEGER, FOREIGN KEY (id_elemento) REFERENCES inventario(id_elemento) ON DELETE CASCADE, FOREIGN KEY (id_tipo) REFERENCES tipo_elementos(id_tipo) ON DELETE CASCADE)";
    private static final String _tablaRefacciones = "CREATE TABLE refacciones(id_refaccion INTEGER PRIMARY KEY AUTOINCREMENT, id_elemento INTEGER, descripcion TEXT, FOREIGN KEY (id_elemento) REFERENCES inventario(id_elemento) ON DELETE CASCADE)";
    private static final String _tablaServicioInventarioRefacciones = "CREATE TABLE servicio_inventario_refacciones(id_servicio_refaccion INTEGER PRIMARY KEY AUTOINCREMENT, num_servicio INTEGER, id_refaccion INTEGER, FOREIGN KEY (num_servicio) REFERENCES servicios(num_servicio) ON DELETE CASCADE, FOREIGN KEY (id_refaccion) REFERENCES refacciones(id_refaccion) ON DELETE CASCADE)";
    private static final String _tablaEvidencias = "CREATE TABLE evidencias(id_servicio_evidencias INTEGER PRIMARY KEY AUTOINCREMENT, num_servicio INTEGER NOT NULL, evidencia BLOB NOT NULL, FOREIGN KEY (num_servicio) REFERENCES servicios(num_servicio))";

    public DataBase(@Nullable Context context) {
        super(context, _baseDatos, null, _dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(_tablaLugares);
        sqLiteDatabase.execSQL(_tablaTipoElementos);
        sqLiteDatabase.execSQL(_tablaServicios);
        sqLiteDatabase.execSQL(_tablaInventario);
        sqLiteDatabase.execSQL(_tablaDispositivos);
        sqLiteDatabase.execSQL(_tablaComputadoras);
        sqLiteDatabase.execSQL(_tablaConexiones);
        sqLiteDatabase.execSQL(_tablaEquiposInstitucionales);
        sqLiteDatabase.execSQL(_tablaServicioInventarioDispositivos);
        sqLiteDatabase.execSQL(_tablaCatalogoRAM);
        sqLiteDatabase.execSQL(_tablaCatalogoDD);
        sqLiteDatabase.execSQL(_tablaCatalogoSO);
        sqLiteDatabase.execSQL(_tablaRefacciones);
        sqLiteDatabase.execSQL(_tablaServicioInventarioRefacciones);
        sqLiteDatabase.execSQL(_tablaEvidencias);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
