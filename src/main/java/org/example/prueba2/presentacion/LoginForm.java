package org.example.prueba2.presentacion;

import org.example.prueba2.dominio.User;
import org.example.prueba2.persistencia.UserDAO;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JDialog {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;
    private JPanel frmLogin;

    private UserDAO userDAO;
    private MainForm mainForm;


    public LoginForm(MainForm mainForm) {
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        setContentPane(frmLogin);
        setModal(true);
        setTitle("Login");
        pack();
        setLocationRelativeTo(mainForm);

        btnLogin.addActionListener( e -> {
                login();
        });
        btnSalir.addActionListener( e -> {
                System.exit(0);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
    }

    private void login()
    {
        try
        {
            User user = new User();
            user.setEmail(txtEmail.getText());
            user.setPasswordHash(new String(txtPassword.getPassword()));

            User userauth = userDAO.authenticate(user);

            if(userauth != null && userauth.getId() > 0 &&
                    userauth.getEmail().equals(user.getEmail()))
            {
                JOptionPane.showMessageDialog(null,
                        "Usuario logeado correctamente", "Login",
                        JOptionPane.WARNING_MESSAGE);//Temporal
            }
            else
            {
                JOptionPane.showMessageDialog(null,
                        "Email o Password incorrectos", "Login",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(), "System",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
