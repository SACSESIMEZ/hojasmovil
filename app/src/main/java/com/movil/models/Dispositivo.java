package com.movil.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.movil.hojasdeservicio.DataBase;

public class Dispositivo extends Elemento{

    private String cambs, ip, mac;
    private int idTipo;

    public String getCambs() {
        return cambs;
    }

    public void setCambs(String cambs) {
        this.cambs = cambs;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }
}