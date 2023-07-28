package com.movil.models;

public class Computadora extends Dispositivo{
    private int idRAM, idHDD, idSO;

    public int getIdRAM() {
        return idRAM;
    }

    public void setIdRAM(int idRAM) {
        this.idRAM = idRAM;
    }

    public int getIdHDD() {
        return idHDD;
    }

    public void setIdHDD(int idHDD) {
        this.idHDD = idHDD;
    }

    public int getIdSO() {
        return idSO;
    }

    public void setIdSO(int idSO) {
        this.idSO = idSO;
    }
}
