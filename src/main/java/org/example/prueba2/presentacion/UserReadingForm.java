package org.example.prueba2.presentacion;

import org.example.prueba2.dominio.User;
import org.example.prueba2.persistencia.UserDAO;
import org.example.prueba2.utils.CUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class UserReadingForm extends JDialog {
    private JTextField txtNombre;
    private JButton btnCrear;
    private JTable tableUsers;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JPanel mainMantteUser;

    private UserDAO userDAO;
    private MainForm mainForm;

    public UserReadingForm(MainForm mainForm)
    {
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        setContentPane(mainMantteUser);
        setModal(true);
        setTitle("Buscar Usuario");
        pack();
        setLocationRelativeTo(mainForm);

        txtNombre.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(!txtNombre.getText().trim().isEmpty())
                {
                    search(txtNombre.getText());
                }
                else
                {
                    DefaultTableModel emptyModel = new DefaultTableModel();
                    tableUsers.setModel(emptyModel);
                }
            }
        });

        btnCrear.addActionListener(s -> {
            UserWriteForm userWriteForm = new UserWriteForm(this.mainForm, CUD.CREATE, new User());
            userWriteForm.setVisible(true);

            DefaultTableModel emptyTable = new DefaultTableModel();
            tableUsers.setModel(emptyTable);
        });

        btnModificar.addActionListener(s -> {
            User user = getUserFromTableRow();
            UserWriteForm userWriteForm = new UserWriteForm(this.mainForm, CUD.UPDATE, user);
            userWriteForm.setVisible(true);

            DefaultTableModel emptyTable = new DefaultTableModel();
            tableUsers.setModel(emptyTable);
        });

        btnEliminar.addActionListener(s -> {
            User user = getUserFromTableRow();
            UserWriteForm userWriteForm = new UserWriteForm(this.mainForm, CUD.DELETE, user);
            userWriteForm.setVisible(true);

            DefaultTableModel emptyTable = new DefaultTableModel();
            tableUsers.setModel(emptyTable);
        });

    }

    private void search(String query)
    {
        try
        {
            User user = new User();
            user.setName(query);
           ArrayList<User> users = userDAO.search(user);
           createTable(users);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTable(ArrayList<User> users)
    {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Id");
        model.addColumn("Nombre");
        model.addColumn("Email");
        model.addColumn("Status");

        this.tableUsers.setModel(model);

        Object row[] = null;

        for(int i = 0; i < users.size(); i++)
        {
            User user = users.get(i);
            model.addRow(row);

            model.setValueAt(user.getId(), i, 0);
            model.setValueAt(user.getName(), i, 1);
            model.setValueAt(user.getEmail(), i, 2);
            model.setValueAt(user.getStrEstatus(), i, 3);

        }
        hideCol(0);
    }

    private void hideCol(int pColumn)
    {
        //valores
        this.tableUsers.getColumnModel().getColumn(pColumn).setMaxWidth(0);
        this.tableUsers.getColumnModel().getColumn(pColumn).setMinWidth(0);

        //encabezados
        this.tableUsers.getTableHeader().getColumnModel().getColumn(pColumn).setMaxWidth(0);
        this.tableUsers.getTableHeader().getColumnModel().getColumn(pColumn).setMinWidth(0);
    }

    private User getUserFromTableRow()
    {
        User user = null;
        try {
            int filaselect = this.tableUsers.getSelectedRow();
            int id = 0;

            if (filaselect != -1) {
                id = (int) this.tableUsers.getValueAt(filaselect, 0);
            }
            else
            {
                JOptionPane.showMessageDialog(null,
                        "Seleccione una fila de la tabla", "Validacion",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            user = userDAO.getById(id);
            if(user.getId() == 0)
            {
                JOptionPane.showMessageDialog(null,
                        "No se encontro el usuario seleccionado", "Validacion",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }
            return user;
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
