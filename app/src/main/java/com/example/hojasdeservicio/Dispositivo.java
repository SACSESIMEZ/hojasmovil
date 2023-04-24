package com.example.hojasdeservicio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

public class Dispositivo {
    private int _idDispositivo, _idElemento, _idComputadora, _idTipo, _idRam, _idDD, _idSO;
    private boolean _institucional, _conectado;
    private String _marca, _modelo, _numSerie, _ip, _mac, _cambs;
    private SQLiteDatabase _db;
    private DataBase _dbHelper;

    public Dispositivo(Context context){
        _dbHelper = new DataBase(context);
        _idDispositivo = 0;
        _idElemento = 0;
        _idComputadora = 0;
        _idTipo = 0;
        _idRam = 0;
        _idDD = 0;
        _idSO = 0;
    }

    public void setIdDispositivo(int idDispositivo){
        _idDispositivo = idDispositivo;
    }

    public int getIdDispositivo(){
        return _idDispositivo;
    }

    public int getIdTipo() {
        return _idTipo;
    }

    private void setIdTipo(int idTipo) {
        this._idTipo = idTipo;
    }

    public int getIdComputadora(){
        return _idComputadora;
    }

    public int getIdRam() {
        return _idRam;
    }

    private void setIdRam(int idRam) {
        this._idRam = idRam;
    }

    public int getIdDD() {
        return _idDD;
    }

    private void setIdDD(int idDD) {
        this._idDD = idDD;
    }

    public int getIdSO() {
        return _idSO;
    }

    private void setIdSO(int idSO) {
        this._idSO = idSO;
    }

    public boolean isInstitucional() {
        return _institucional;
    }

    private void setInstitucional(boolean institucional) {
        this._institucional = institucional;
    }

    public boolean isConectado() {
        return _conectado;
    }

    private void setConectado(boolean conectado) {
        this._conectado = conectado;
    }

    public String getMarca() {
        return _marca;
    }

    private void setMarca(String marca) {
        this._marca = marca;
    }

    public String getModelo() {
        return _modelo;
    }

    private void setModelo(String modelo) {
        this._modelo = modelo;
    }

    public String getNumSerie() {
        return _numSerie;
    }

    private void setNumSerie(String numSerie) {
        this._numSerie = numSerie;
    }

    public String getIp() {
        return _ip;
    }

    private void setIp(String ip) {
        this._ip = ip;
    }

    public String getMac() {
        return _mac;
    }

    private void setMac(String mac) {
        this._mac = mac;
    }

    public String getCambs() {
        return _cambs;
    }

    private void setCambs(String cambs) {
        this._cambs = cambs;
    }

    public void setInformacion(){
        _db = _dbHelper.getReadableDatabase();
        if(_db != null){
            Cursor c = _db.query("dispositivos", new String[]{"id_elemento, id_tipo"}, "id_dispositivo = ?", new String[]{_idDispositivo + ""}, null, null, null);
            if(c != null){
                while (c.moveToNext()){
                    _idElemento = c.getInt(0);
                    _idTipo = c.getInt(1);
                }
            }
            c = _db.query("inventario", null, "id_elemento = ?", new String[]{_idElemento + ""}, null, null, null);
            if(c != null){
                while(c.moveToNext()){
                    _marca = c.getString(1);
                    _modelo = c.getString(2);
                    _numSerie = c.getString(3);
                }
            }
            c = _db.query("computadoras", null, "id_elemento = ?", new String[]{_idElemento + ""}, null, null, null);
            if(c != null){
                while(c.moveToNext()){
                    _idComputadora = c.getInt(0);
                    _idRam = c.getInt(2);
                    _idDD = c.getInt(3);
                    _idSO = c.getInt(4);
                }
            }
            c = _db.query("equipos_institucionales", new String[]{"cambs"}, "id_dispositivo = ?", new String[]{_idDispositivo + ""}, null, null, null);
            if(c != null){
                while (c.moveToNext()){
                    _institucional = true;
                    _cambs = c.getString(0);
                }
            }
            c = _db.query("conexiones", new String[]{"ip, mac"}, "id_dispositivo = ?", new String[]{_idDispositivo + ""}, null, null, null);
            if(c != null){
                while (c.moveToNext()){
                    _conectado = true;
                    _ip = c.getString(0);
                    _mac = c.getString(1);
                }
            }
        }
    }
}
