package org.example;

import java.sql.*;

public class Conexion {
    private Connection conexion;
    private String servidor;
    private String usuario;
    private String password;
    private String bd;

    public Conexion(String servidor, String usuario, String password, String bd) {
        this.servidor = servidor;
        this.usuario = usuario;
        this.password = password;
        this.bd = bd;
    }

    public void conectar() throws SQLException {
        String url = "jdbc:mysql://" + servidor + "/" + bd + "?useSSL=false";
        conexion = DriverManager.getConnection(url, usuario, password);
    }

    public void desconectar() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

    public ResultSet ejecutarConsulta(String consulta) throws SQLException {
        Statement statement = conexion.createStatement();
        ResultSet resultado = statement.executeQuery(consulta);
        return resultado;
    }

    public PreparedStatement ejecutarUpdate(String consulta) throws SQLException {
        Statement statement = conexion.createStatement();
        statement.executeUpdate(consulta);
        return null;
    }

}
