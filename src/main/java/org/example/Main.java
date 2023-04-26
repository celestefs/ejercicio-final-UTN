package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in).useDelimiter("\n");
    private static Conexion conexion = new Conexion("localhost", "root", "root", "ejercicio_final");
    private static HashMap<String, ArrayList<String>> hmMaterias = new HashMap<>();
    private static HashMap<String, ArrayList<String>> hmMateriasAprobadas = new HashMap<>();
    public static void main(String[] args) throws SQLException, JsonProcessingException {

        boolean salir = true;

        while (salir) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Crear materia");
            System.out.println("2. Agregar alumno");
            System.out.println("3. Traer datos");
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
                    traerDatos();
                    break;
                case 4:
                    salir = false;
                    break;
            }
        }
            // si quiero validar la inscripcion debo hacerlo por aca ya que el metodo es booleano y no me permite
            // agregarlo al menu de opciones
        try {
            traerDatos();
            boolean inscripcionValida = validarInscripcion("cele", "fisica II");
            if (inscripcionValida) {
                System.out.println("cele puede inscribirse en fisica II.");
            } else {
                System.out.println("cele no puede inscribirse en fisica II.");
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
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
        String correlativasJson = new Gson().toJson(correlativas);
        try {
            conexion.conectar();
            System.out.println("Conexión exitosa a la base de datos");
            PreparedStatement stmt = conexion.ejecutarUpdate("INSERT INTO materias (nombre, correlativas) VALUES ('" + nombre + "', '" + correlativasJson + "');");
            System.out.println("Materia ingresada correctamente");
            conexion.desconectar();
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public static void agregarAlumno() throws SQLException {
        Alumno alumno = new Alumno();
        System.out.println("Cuál es el nombre del alumno?");
        String nombre = sc.next();
        alumno.setNombre(nombre);
        Integer legajo = null;
        while (legajo == null || !Integer.toString(legajo).matches("^[0-9]{5}$")) {
            System.out.println("Cuál es el número de legajo?");
            try {
                legajo = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Debe ingresar un número de legajo válido.");
                sc.next();
                continue;
            }
            if (!Integer.toString(legajo).matches("^[0-9]{5}$")) {
                System.out.println("El número de legajo debe tener 5 dígitos numéricos.");
            }
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
        String materiasAprobadasJson = new Gson().toJson(materiasAprobadas);
        try {
            conexion.conectar();
            System.out.println("Conexión exitosa a la base de datos");
            PreparedStatement stmt = conexion.ejecutarUpdate("INSERT INTO alumnos (nombre, legajo, materias_aprobadas) VALUES ('" + nombre + "', '" + legajo + "', '" + materiasAprobadasJson + "');");
            System.out.println("Alumno ingresado correctamente");
            conexion.desconectar();
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public static boolean validarInscripcion(String nombreAlumno, String nombreMateria) {
        ArrayList<String> materiasAprobadas = hmMateriasAprobadas.get(nombreAlumno);
        ArrayList<String> correlativasMateria = hmMaterias.get(nombreMateria);
        if (correlativasMateria != null) {
            for (String correlativa : correlativasMateria) {
                if (!materiasAprobadas.contains(correlativa)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void traerDatos() throws SQLException, JsonProcessingException {
        Materia materia = new Materia();
        Alumno alumno = new Alumno();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        try {
            conexion.conectar();
            System.out.println("Conexión exitosa a la base de datos");
            ResultSet rs = conexion.ejecutarConsulta("SELECT * FROM materias");
            while (rs.next()) {
                materia = new Materia(rs.getString("nombre"));
                String jsonText = objectMapper.writeValueAsString(rs.getString("correlativas"));
                ArrayList<String> nombreCorrelativas = objectMapper.readValue(jsonText, ArrayList.class);
                materia.setCorrelativas(nombreCorrelativas);
                hmMaterias.put(materia.getNombre(), materia.getCorrelativas());
            }
            ResultSet rs2 = conexion.ejecutarConsulta("SELECT * FROM alumnos");
            while (rs2.next()) {
                alumno = new Alumno(rs2.getString("nombre"));
                String jsonText = objectMapper.writeValueAsString(rs2.getString("materias_aprobadas"));
                ArrayList<String> nombreAprobadas = objectMapper.readValue(jsonText, ArrayList.class);
                alumno.setMateriasAprobadas(nombreAprobadas);
                hmMateriasAprobadas.put(alumno.getNombre(), alumno.getMateriasAprobadas());
            }
            conexion.desconectar();
            System.out.println(hmMaterias);
            System.out.println(hmMateriasAprobadas);
            } catch (SQLException e) {

        }
    }
}

