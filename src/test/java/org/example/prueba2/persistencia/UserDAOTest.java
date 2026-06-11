package org.example.prueba2.persistencia;

import org.example.prueba2.dominio.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp()
    {
        userDAO = new UserDAO();
    }

    @Test
    void createUser() throws SQLException
    {
        User user = new User(0, "victor", "123456",
                "victorduran@gmail.com", (byte) 1);
        User res = userDAO.create(user);
        assertNotEquals(res, null);
    }

    @Test
    void updateUser() throws SQLException
    {
        User user = new User(3, "victor rivas", "",
                "victorduran45@gmail.com", (byte) 1);
        boolean res = userDAO.update(user);
        assertNotEquals(res, null);
    }

    @Test
    void deleteUser() throws SQLException
    {
        User user = new User(3, "victor rivas", "",
                "victorduran45@gmail.com", (byte) 1);
        boolean res = userDAO.delete(user);
        assertNotEquals(res, null);
    }
}