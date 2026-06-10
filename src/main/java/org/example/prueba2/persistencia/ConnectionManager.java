package org.example.prueba2.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;//Gestionar los driver JDBC
import java.sql.SQLException;

public class ConnectionManager {
    private static final String STR_CONNECTION = "jdbc:sqlserver://localhost:1433;"
            + "encrypt = true;"
            + "database = SecurityDB2026;"
            + "trustServerCertificate = true;"
            + "user = Victor2G02;"
            + "password = Vict13";


    private Connection connection;

    private static ConnectionManager instance;

    private ConnectionManager()
    {
        this.connection = null;
        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }
        catch (ClassNotFoundException ex)
        {
            throw new RuntimeException("Error al cargar el driver JDBC de Sql Server");
        }
    }

    public synchronized Connection connect() throws SQLException
    {
        if(this.connection == null || this.connection.isClosed())
        {
            try
            {
               this.connection = DriverManager.getConnection(STR_CONNECTION);
            }
            catch (SQLException ex)
            {
                throw new SQLException("Error al conectar con la base de datos:"
                        + ex.getMessage(), ex);
            }
        }
        return this.connection;
    }

    public void disconnect() throws SQLException
    {
        if(this.connection != null)
        {
            try
            {
                this.connection.close();
            }
            catch (SQLException ex)
            {
                throw new SQLException("Error al cerrar la conexion: "
                        + ex.getMessage(), ex);
            }
            finally {
                this.connection = null;
            }
        }
    }

    public static synchronized ConnectionManager getInstance()
    {
        if(instance == null)
        {
            instance = new ConnectionManager();
        }
        return instance;
    }
}
