package org.example;

import java.util.ArrayList;
import java.util.List;

public class Materia {
    String nombre;
    ArrayList<String> correlativas = new ArrayList<>();

    public Materia(){}

    public Materia(String nombre){
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getCorrelativas() {
        return correlativas;
    }

    public void setCorrelativas(ArrayList<String> correlativas) {
        this.correlativas = correlativas;
    }

    public boolean puedeCursar(Alumno alumno){
        return alumno.getMateriasAprobadas().containsAll(correlativas);
    }

    @Override
    public String toString() {
        return "Materia{" +
                "nombre='" + nombre + '\'' +
                ", correlativas=" + correlativas +
                '}';
    }

}
