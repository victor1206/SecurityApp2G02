package org.example.prueba2.persistencia;

import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;//List de c#

import org.example.prueba2.dominio.User;
import org.example.prueba2.utils.PasswordHasher;

public class UserDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public UserDAO() {conn = ConnectionManager.getInstance();}

    public User create(User user) throws SQLException
    {
        User result = null;
        try
        {
            PreparedStatement ps = conn.connect().prepareStatement(
                    "Insert Into Users (name, passwordHash, email, status) " +
                            "Values(?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, user.getName());
            ps.setString(2, PasswordHasher.hashPassword(user.getPasswordHash()));
            ps.setString(3, user.getEmail());
            ps.setByte(4, user.getStatus());

            int affectRows = ps.executeUpdate();

            if(affectRows != 0)
            {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if(generatedKeys.next())
                {
                    int idGenerado = generatedKeys.getInt(1);

                    result = getById(idGenerado);
                }
                else
                {
                    throw new SQLException("Error al crear el usuario");
                }
            }
            ps.close();
        }
        catch (SQLException ex)
        {
            throw new SQLException("Error al crear el usuario:"
                    + ex.getMessage(), ex);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        finally {
            ps = null;
            conn.disconnect();
        }
        return result;
    }

    public boolean update(User user) throws SQLException
    {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "Update Users " +
                            "Set name = ?, email = ?, status = ? " +
                            "Where id = ?"
            );

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setByte(3, user.getStatus());
            ps.setInt(4, user.getId());

            if(ps.executeUpdate() > 0)
            {
                res = true;
            }
            ps.close();
        }
        catch (SQLException ex)
        {
            throw new SQLException("Error al modidicar el usuario: "
                    + ex.getMessage(), ex);
        }
        finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean delete(User user) throws SQLException
    {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
              "Delete From Users Where id = ?"
            );

            ps.setInt(1, user.getId());

            if(ps.executeUpdate() > 0)
            {
                res = true;
            }
            ps.close();
        }
        catch (Exception ex)
        {
            throw new SQLException("Eror al eliminar el usuario: "
                    + ex.getMessage(), ex);
        }
        finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public ArrayList<User> search(User user) throws SQLException
    {
        ArrayList<User> usuarios = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
              "Select id, name, email, status" +
                  " From Users" +
                  " Where name Like ?"
            );

            ps.setString(1, "%" + user.getName() + "%");

            rs = ps.executeQuery();

            while (rs.next())
            {
                User us = new User();
                us.setId(rs.getInt(1));
                us.setName(rs.getString(2));
                us.setEmail(rs.getString(3));
                us.setStatus(rs.getByte(4));

                usuarios.add(us);
            }
            ps.close();
            rs.close();
        }
        catch (SQLException ex)
        {
            throw new SQLException("Error al buscar usuario: "
                    + ex.getMessage(), ex);
        }
        finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return usuarios;
    }

    public User authenticate(User user) throws SQLException
    {
        User userauthenticate = new User();

        try {
            ps = conn.connect().prepareStatement(
              "Select id, name, email, status "
              + "From Users"
              + " Where email = ? And passwordHash = ? And status = 1"
            );

            ps.setString(1, user.getEmail());
            ps.setString(2, PasswordHasher.hashPassword(user.getPasswordHash()));
            rs = ps.executeQuery();

            if(rs.next())
            {
                userauthenticate.setId(rs.getInt(1));
                userauthenticate.setName(rs.getString(2));
                userauthenticate.setEmail(rs.getString(3));
                userauthenticate.setStatus(rs.getByte(4));
            }
            else
            {
                userauthenticate = null;
            }

            ps.close();
            rs.close();
        }
        catch (SQLException ex)
        {
            throw new SQLException("Error al iniciar sesion del usuario: "
                    + ex.getMessage(), ex);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return userauthenticate;
    }

    public boolean updatePassword(User user) throws SQLException
    {
        boolean res = false;
        try
        {
            ps = conn.connect().prepareStatement(
                    "Update Users " +
                            "set passwordHash = ? " +
                            "Where id = ?"
            );

            ps.setString(1, PasswordHasher.hashPassword(user.getPasswordHash()));
            ps.setInt(2, user.getId());

            if(ps.executeUpdate() > 0)
            {
                res = true;
            }
            ps.close();
        }
        catch (Exception ex)
        {
            throw new SQLException("Error al modificar el password del usuario: "
                    + ex.getMessage(), ex);
        }
        finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public User getById(int pId) throws SQLException
    {
        User user = new User();
        try {

            ps = conn.connect().prepareStatement(
                    "Select id, name, email, status " +
                            "From Users" +
                            " Where id = ?"
            );

            ps.setInt(1, pId);

            rs = ps.executeQuery();

            if(rs.next())
            {
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setStatus(rs.getByte(4));
            }
            else
            {
                user = null;
            }
            ps.close();
            rs.close();
        }
        catch (SQLException ex)
        {
            throw new SQLException("Error al obtener un usuario por id: " + ex.getMessage(), ex);
        }
        finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return user;
    }
}
