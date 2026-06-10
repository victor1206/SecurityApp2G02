package org.example.prueba2.persistencia;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionManagerTest {
    ConnectionManager connectionManager;

    @BeforeEach
    void setUp() throws SQLException
    {
        connectionManager = ConnectionManager.getInstance();
    }

    @AfterEach
    void tearDown() throws SQLException
    {
        if(connectionManager != null) {
            connectionManager.disconnect();
            connectionManager = null;
        }
    }

    @Test
    void connect() throws SQLException
    {
        Connection conn = connectionManager.connect();

        assertNotNull(conn, "La conexion no debe der nula");

        assertFalse(conn.isClosed(), "La conexion debe estar abierta");

        if(conn != null)
        {
            conn.close();
        }
    }
}