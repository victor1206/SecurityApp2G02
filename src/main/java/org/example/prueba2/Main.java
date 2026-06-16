package org.example.prueba2;

import org.example.prueba2.presentacion.LoginForm;
import org.example.prueba2.presentacion.MainForm;
import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = new MainForm();
            mainForm.setVisible(true);
            LoginForm loginForm = new LoginForm(mainForm);
            loginForm.setVisible(true);
        });
    }
}