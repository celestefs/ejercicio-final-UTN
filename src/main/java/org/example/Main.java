package org.example;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in).useDelimiter("\n");
    private static Conexion conexion = new Conexion();

    public static void main(String[] args) throws SQLException {

        boolean salir = true;

        while (salir) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Crear materia");
            System.out.println("2. Agregar alumno");
            System.out.println("3. Validar la inscripción");
            System.out.println("4. Salir");

            int opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    crearMateria();
                    break;
                case 2:
                    agregarAlumno();
                    break;
                case 3:
                    validarInscripcion();
                    break;
                case 4:
                    salir = false;
                    break;
            }
        }


    }

    public static void crearMateria() throws SQLException {
        Materia materia = new Materia();
        System.out.println("Qué nombre quieres que tenga la materia?");
        String nombre = sc.next();
        materia.setNombre(nombre);
        System.out.println("Cuantas correlativas tiene?");
        int numero = sc.nextInt();
        System.out.println("Qué materias desea agregar a las correlativas?");
        ArrayList<String> correlativas = new ArrayList<>();
        String input;
        for (int i = 0; i < numero; i++) {
            input = sc.next();
            correlativas.add(input);
        }
        conexion.estableceConexion();
        PreparedStatement stmt = conexion.conectar.prepareStatement("INSERT INTO materias (nombre, correlativas) VALUES (?, ?)");
        stmt.setString(1, nombre);
        stmt.setString(2, String.join(",", correlativas));
        stmt.executeUpdate();
        conexion.cerrarConexion();
    }

    public static void agregarAlumno() throws SQLException {
        Alumno alumno = new Alumno();
        System.out.println("Cuál es el nombre del alumno?");
        String nombre = sc.next();
        alumno.setNombre(nombre);
        System.out.println("Cuál es el número de legajo?");
        Integer legajo = sc.nextInt();
        if (!legajo.equals("^[0-9]{5}$")) {
            System.out.println("El número de legajo debe tener 5 dígitos numéricos.");
            return;
        }
        alumno.setLegajo(legajo);
        System.out.println("Cuantas materias aprobadas tiene?");
        int numero = sc.nextInt();
        System.out.println("Cuales son sus materias aprobadas?");
        ArrayList<String> materiasAprobadas = new ArrayList<>();
        String input;
        for (int i = 0; i < numero; i++) {
            input = sc.next();
            materiasAprobadas.add(input);
        }
        conexion.estableceConexion();
        PreparedStatement stmt = conexion.conectar.prepareStatement("INSERT INTO alumnos (nombre, legajo, materias aprobadas) VALUES (?, ?, ?)");
        stmt.setString(1, nombre);
        stmt.setInt(2, legajo);
        stmt.setString(3, String.join(",", materiasAprobadas));
        stmt.executeUpdate();
        conexion.cerrarConexion();
    }

    public static boolean cumpleCorrelativas(ArrayList<String> materiasAprobadas, ArrayList<String> correlativas) {
        for (String correlativa : correlativas) {
            if (!materiasAprobadas.contains(correlativa)) {
                return false;
            }
        }
        return true;
    }

    public static void validarInscripcion() throws SQLException {
        System.out.println("A qué materia desea inscribirse?");
        String nombreMateria = sc.next();
        Materia materia = buscarMateria(nombreMateria);
        if (materia == null) {
            System.out.println("No se encontró la materia " + nombreMateria);
            return;
        }
        System.out.println("Coloque el legajo del alumno que desea inscribir a la materia");
        int legajo = sc.nextInt();
        Alumno alumno = buscarAlumno(legajo);
        if (alumno == null) {
            System.out.println("No se encontró el alumno con legajo " + legajo);
            return;
        }
        if (cumpleCorrelativas(alumno.getMateriasAprobadas(), materia.getCorrelativas())) {
            System.out.println("El alumno " + alumno.getNombre() + " puede inscribirse a la materia " + nombreMateria);
        } else {
            System.out.println("El alumno " + alumno.getNombre() + " no puede inscribirse a la materia " + nombreMateria + " por no cumplir con las correlativas necesarias");
        }
    }

    static ArrayList<Alumno> alumnos = new ArrayList<>();

    public static Alumno buscarAlumno(int legajo) {
        for (Alumno alumno : alumnos) {
            if (alumno.getLegajo() == legajo) {
                return alumno;
            }
        }
        return null;
    }

    static ArrayList<Materia> materias = new ArrayList<>();

    public static Materia buscarMateria(String nombre) {
        for (Materia materia : materias) {
            if (materia.getNombre().equalsIgnoreCase(nombre)) {
                return materia;
            }
        }
        return null;
    }
}

    /*
    con el numero del legajo preguntar a que materia desea inscribirse el alumno,
    y corroborar con el legajo si puede o no segun sus materias aprobadas

    tengo que envolver el menu en establecer conexion y cerrar conexion para que guarde los datos en la db?

    falta json y gson: crear objetos materias y alumnos y hacerlo manualmente
    */

