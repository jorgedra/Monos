package com.example.monos.Clases;

import java.io.Serializable;
import java.util.Objects;

public class Mono implements Serializable {

    private int idMono;
    private String nombre;
    private String familia;
    private String region;

    public Mono(int idMono, String nombre, String familia, String region) {
        this.idMono = idMono;
        this.nombre = nombre;
        this.familia = familia;
        this.region = region;
    }

    public Mono() {
        this.idMono = 0;
        this.nombre = "sin nombre";
        this.familia = "sin familia";
        this.region = "sin region";
    }

    public int getIdMono() {
        return idMono;
    }

    public void setIdMono(int idMono) {
        this.idMono = idMono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mono mono = (Mono) o;
        return idMono == mono.idMono;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMono);
    }

    @Override
    public String toString() {
        return "Mono{" +
                "idMono=" + idMono +
                ", nombre='" + nombre + '\'' +
                ", familia='" + familia + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
