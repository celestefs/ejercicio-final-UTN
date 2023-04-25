package org.example;

import java.util.Date;

public class Inscripcion {
    Materia materia;
    Alumno alumno;
    Date fecha = new Date();

    boolean aprobada;

    public Inscripcion(){}

    public Inscripcion(Materia materia, Alumno alumno) {
        this.materia = materia;
        this.alumno = alumno;
    }

    public boolean aprobada(){
        return materia.puedeCursar(alumno);
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean verificarInscripcion(){

        boolean aprobada = true;

        if (alumno.getMateriasAprobadas().containsAll(materia.getCorrelativas())){
            System.out.println(getAlumno().getNombre() + " puede inscribirse a la materia " + getMateria().getNombre());
            aprobada = true;
        } else{
            System.out.println(getAlumno().getNombre() + " no puede inscribirse a la materia " + getMateria().getNombre() + " ya que no posee las materias correlativas aprobadas.");
            aprobada = false;
        }
        return aprobada;
    }
}
