package com.example.hojasdeservicio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

public class Servicio implements Serializable {
    private int _numServicio, _contadorEvidencias;
    private byte[][] _evidencias;
    private byte[] _firma;
    private boolean _dispositivoServicio;
    private String _descripcionServicio, _fechaIni, _fechaFin, _lugar;
    private SQLiteDatabase _db;
    private DataBase _dbHelper;
    private Dispositivo _dispositivo;

    public Servicio(Context context){
        _dbHelper = new DataBase(context);
        _dispositivo = new Dispositivo(context);
        _dispositivoServicio = false;
        _numServicio = 0;
        _contadorEvidencias = 0;
    }

    public int getNumServicio(){
        return _numServicio;
    }

    public String getDescripcionServicio() {
        return _descripcionServicio;
    }

    public byte[] getEvidencias(int evidencia){
        return _evidencias[evidencia];
    }

    public byte[] getFirma(){
        return _firma;
    }

    public void setNumServicio(int numServicio){
        _numServicio = numServicio;
    }

    public void setInformacion(){
        _db = _dbHelper.getReadableDatabase();
        if(_db != null){
            Cursor c = _db.query("servicios", new String[]{"descripcion_servicio", "fecha_ini", "fecha_fin", "firma"}, "num_servicio = ?", new String[]{_numServicio + ""}, null, null, null);
            if(c != null){
                while(c.moveToNext()){
                    _descripcionServicio = c.getString(0);
                    _fechaIni = c.getString(1);
                    _fechaFin = c.getString(2);
                    _firma = c.getBlob(3);
                }
            }
            c = _db.rawQuery("SELECT lugares.lugar FROM lugares INNER JOIN servicios ON lugares.id_lugar = servicios.id_lugar WHERE num_servicio = ?", new String[]{_numServicio + ""});
            if(c != null){
                while (c.moveToNext()){
                    _lugar = c.getString(0);
                }
            }
            c = _db.query("servicio_inventario_dispositivos", new String[]{"id_dispositivo"}, "num_servicio = ?", new String[]{_numServicio + ""}, null, null, null);
            if(c != null){
                while(c.moveToNext()){
                    _dispositivo.setIdDispositivo(c.getInt(0));
                    _dispositivo.setInformacion();
                    _dispositivoServicio = true;
                }
            }
            c = _db.query("evidencias", new String[]{"evidencia"}, "num_servicio = ?", new String[]{_numServicio + ""}, null, null, null);
            if(c != null){
                while(c.moveToNext()){
                    _evidencias[_contadorEvidencias] = c.getBlob(0);
                    _contadorEvidencias++;
                }
            }
        }
    }

    public int getContadorEvidencias() {
        return _contadorEvidencias;
    }

    public String getFechaIni(){
        return _fechaIni;
    }

    public String getFechaFin(){
        return _fechaFin;
    }

    public String getLugar(){
        return _lugar;
    }

    public boolean isDispositivoServicio(){
        return _dispositivoServicio;
    }

    public Dispositivo getDispositivo(){
        return _dispositivo;
    }

    public void saveDispositivoServicio(){

    }
}