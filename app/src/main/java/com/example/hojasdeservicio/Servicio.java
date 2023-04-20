package com.example.hojasdeservicio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Servicio {
    private int _numServicio, _idElemento, idDispositivo, _contadorEvidencias;
    private byte[][] _evidencias;
    private byte[] firma;
    private String _descripcionServicio;
    private SQLiteDatabase _db;
    private DataBase _dbHelper;

    public void Servicio(Context context){
        _dbHelper = new DataBase(context);
        _contadorEvidencias = 0;
    }

    public int getNumServicio(){
        return _numServicio;
    }

    public void setNumServicio(int numServicio){
        _numServicio = numServicio;
    }

    public String getDescripcionServicio() {
        return _descripcionServicio;
    }

    public void setDescripcionServicio() {
        String descripcion = "";
        _db = _dbHelper.getReadableDatabase();
        if(_db != null){
            Cursor c = _db.query("servicios", new String[]{"descripcion_servicio"}, "num_servicio = ?", new String[]{_numServicio + ""}, null, null, null);
            if(c != null){
                while(c.moveToNext()){
                    descripcion = c.getString(0);
                }
            }
        }
        _descripcionServicio = (descripcion != null) ? descripcion : "";
    }

    public byte[] getEvidencias(){
        return _evidencias[_contadorEvidencias];
    }

    public void setEvidencias() {
        _db = _dbHelper.getReadableDatabase();
        if(_db != null){
            Cursor c = _db.query("evidencias", new String[]{"evidencia"}, "num_servicio = ?", new String[]{_numServicio + ""}, null, null, null);
            if(c != null){
                while(c.moveToNext()){
                    _evidencias[_contadorEvidencias] = c.getBlob(0);
                    _contadorEvidencias++;
                }
            }
        }
    }

    public byte[] getFirma(){

    }
}