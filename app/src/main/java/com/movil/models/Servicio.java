package com.movil.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.movil.hojasdeservicio.DataBase;

import java.io.Serializable;
import java.util.ArrayList;

public class Servicio implements Serializable {
    private int numServicio;
    private String lugar, personaReporta, descripcionReporte, descripcionServicio, fechaIni, fechaFin;
    private byte[] firma;
    private ArrayList<Evidencia> listaEvidencias;
    private Dispositivo dispositivo;
    private ArrayList<Refaccion> listaRefacciones;
    private Computadora computadora;

    public int getNumServicio() {
        return numServicio;
    }

    public void setNumServicio(int numServicio) {
        this.numServicio = numServicio;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getPersonaReporta() {
        return personaReporta;
    }

    public void setPersonaReporta(String personaReporta) {
        this.personaReporta = personaReporta;
    }

    public String getDescripcionReporte() {
        return descripcionReporte;
    }

    public void setDescripcionReporte(String descripcionReporte) {
        this.descripcionReporte = descripcionReporte;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public String getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(String fechaIni) {
        this.fechaIni = fechaIni;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public byte[] getFirma() {
        return firma;
    }

    public void setFirma(byte[] firma) {
        this.firma = firma;
    }

    public ArrayList<Evidencia> getListaEvidencias() {
        return listaEvidencias;
    }

    public void setListaEvidencias(ArrayList<Evidencia> listaEvidencias) {
        this.listaEvidencias = listaEvidencias;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public ArrayList<Refaccion> getListaRefacciones() {
        return listaRefacciones;
    }

    public void setListaRefacciones(ArrayList<Refaccion> listaRefacciones) {
        this.listaRefacciones = listaRefacciones;
    }

    public Computadora getComputadora() {
        return computadora;
    }

    public void setComputadora(Computadora computadora) {
        this.computadora = computadora;
    }
}