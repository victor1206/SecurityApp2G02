package org.example.prueba2.presentacion;

import javax.swing.*;
import org.example.prueba2.dominio.User;

public class MainForm extends JFrame {
    private User userAutenticate;

    public User getUserAutenticate() {return userAutenticate; }
    public void setUserAutenticate(User userAutenticate)
    { this.userAutenticate = userAutenticate; }

    private void createMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuPerfil = new JMenu();
        menuBar.add(menuPerfil);

        JMenuItem itemChangePassword = new JMenuItem("Cambiar Contraseña");
        menuPerfil.add(itemChangePassword);
        itemChangePassword.addActionListener(e -> {
            ChangePasswordForm changePassword = new ChangePasswordForm(this);
            changePassword.setVisible(true);
        });

        JMenuItem itemChangeUser = new JMenuItem("Cambiar Usuario");
        menuPerfil.add(itemChangeUser);
        itemChangeUser.addActionListener(e -> {
            LoginForm loginForm = new LoginForm(this);
            loginForm.setVisible(true);
        });

        JMenuItem itemSalir = new JMenuItem("Salid");
        menuPerfil.add(itemSalir);
        itemSalir.addActionListener(e -> {
            System.exit(0);
        });

        //Menu mantenimiento
        JMenu manuMantenimiento = new JMenu("Mantenimiento");
        menuBar.add(manuMantenimiento);

        JMenuItem itemUsers = new JMenuItem("Usuarios");
        manuMantenimiento.add(itemUsers);
        itemUsers.addActionListener(e -> {
            UserReadingForm userReadingForm = new UserReadingForm(this);
            userReadingForm.setVisible(true);
        });
    }
}
