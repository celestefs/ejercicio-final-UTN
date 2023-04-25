package org.example;

import java.util.ArrayList;
import java.util.List;

public class Alumno {
    String nombre;
    int legajo;
    ArrayList<String> materiasAprobadas = new ArrayList<>();

    public Alumno(){}

    public Alumno(String nombre, int legajo) {
        this.nombre = nombre;
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getLegajo() {
        return legajo;
    }

    public void setLegajo(int legajo) {
        this.legajo = legajo;
    }

    public ArrayList<String> getMateriasAprobadas() {
        return materiasAprobadas;
    }

    public void setMateriasAprobadas(ArrayList<String> materiasAprobadas) {
        this.materiasAprobadas = materiasAprobadas;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "nombre='" + nombre + '\'' +
                ", legajo=" + legajo +
                ", materiasAprobadas=" + materiasAprobadas +
                '}';
    }
}
