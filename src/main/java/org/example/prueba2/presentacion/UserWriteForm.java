package org.example.prueba2.presentacion;

import org.example.prueba2.dominio.User;
import org.example.prueba2.persistencia.UserDAO;
import org.example.prueba2.utils.CBOption;
import org.example.prueba2.utils.CUD;

import javax.swing.*;

public class UserWriteForm extends JDialog {
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox cboStatus;
    private JButton btnOk;
    private JButton btnCancelar;
    private JPanel frmWriteForm;
    private JLabel lbPassword;

    private UserDAO userDAO;
    private MainForm mainForm;
    private CUD cud;
    private User en;


    public UserWriteForm(MainForm mainForm, CUD cud, User user)
    {
        this.cud = cud;
        this.en = user;
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        setContentPane(frmWriteForm);
        setModal(true);
        init();
        pack();
        setLocationRelativeTo(mainForm);

        btnCancelar.addActionListener(s -> this.dispose());
        btnOk.addActionListener(s -> ok());
    }

    private void init()
    {
        initCBStatus();
        switch (this.cud)
        {
            case CREATE:
                setTitle("Crear Usuario");
                btnOk.setText("Guardar");
                break;
            case UPDATE:
                setTitle("Modificar Usuario");
                btnOk.setText("Guardar");
                break;
            case DELETE:
                setTitle("Eliminar Usuario");
                btnOk.setText("Eliminar");
                break;
        }
        setValueControls(this.en);
    }

    public void initCBStatus()
    {
        DefaultComboBoxModel<CBOption> model =
                (DefaultComboBoxModel<CBOption>) cboStatus.getModel();
        model.addElement(new CBOption("ACTIVO", (byte) 1));
        model.addElement(new CBOption("INACTIVO", (byte) 2));
    }

    private void setValueControls(User user)
    {
        txtNombre.setText(user.getName());
        txtEmail.setText(user.getEmail());
        cboStatus.setSelectedItem(new CBOption(null, user.getStatus()));

        if(this.cud == CUD.CREATE)
        {
            cboStatus.setSelectedItem(new CBOption(null, 1));
        }

        if(this.cud == CUD.DELETE)
        {
            txtNombre.setEditable(false);
            txtEmail.setEditable(false);
            cboStatus.setEnabled(false);
        }

        if(this.cud != CUD.CREATE)
        {
            txtPassword.setVisible(false);
            lbPassword.setVisible(false);
        }
    }

    private boolean getValuesControls()
    {
        boolean res = false;
        CBOption selectOption = (CBOption) cboStatus.getSelectedItem();

        byte status = selectOption != null ? (byte) (selectOption.getValue()) : (byte) 0;

        if(txtNombre.getText().trim().isEmpty())
        {
            return res;
        }
        else
            if(txtEmail.getText().trim().isEmpty())
            {
                return res;
            }
            else
                if(status == (byte) 0)
                {
                    return res;
                }
                else
                if(this.cud != CUD.CREATE && this.en.getId() == 0)
                {
                    return res;
                }

        res = true;

        this.en.setName(txtNombre.getText());
        this.en.setEmail(txtEmail.getText());
        this.en.setStatus(status);
        if(this.cud == CUD.CREATE)
        {
            this.en.setPasswordHash(new String(txtPassword.getPassword()));
            if(this.en.getPasswordHash().trim().isEmpty())
            {
                return false;
            }
        }
       return res;
    }

    private void ok()
    {
        try
        {
            boolean res = getValuesControls();
            if(res)
            {
                boolean r = false;
                switch (this.cud)
                {
                    case CREATE:
                        User user = userDAO.create(this.en);
                        if(user.getId() > 0)
                        {
                            r = true;
                        }
                        break;
                    case UPDATE:
                        r = userDAO.update(this.en);
                        break;
                    case DELETE:
                        r = userDAO.delete(this.en);
                        break;
                }
                if(r)
                {
                    JOptionPane.showMessageDialog(null,
                            "Transaccion realizada exitosamente", "Informacion",
                            JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(null,
                            "No se logro realizar ninguna accion", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null,
                        "Los campos con * son obligatorios", "Validacion",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
}
