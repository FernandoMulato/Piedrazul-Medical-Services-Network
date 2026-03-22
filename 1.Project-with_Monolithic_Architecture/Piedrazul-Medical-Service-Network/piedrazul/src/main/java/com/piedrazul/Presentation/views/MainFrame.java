package com.piedrazul.Presentation.views;

import javax.swing.*;

import com.piedrazul.Domain.core.events.ClsClinicEvent;
import com.piedrazul.Domain.core.events.IClinicObserver;
import com.piedrazul.Domain.core.events.ClinicEventType;
import com.piedrazul.Presentation.controller.UserController;

import java.awt.*;

public class MainFrame extends JFrame implements IClinicObserver {
  private final UserController userController;

  private final ClinicTableModel tableModel = new ClinicTableModel();
  private final JTable table = new JTable(tableModel);
  private final ClinicFormPanel form = new ClinicFormPanel();
  private final JLabel lblStatus = new JLabel("Listo.");

  private long selectedId = 0;

  public MainFrame(UserController controller) {
    super("Piedrazul - User CRUD");
    this.userController = controller;

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(980, 520);
    setLocationRelativeTo(null);

    setLayout(new BorderLayout(8, 8));
    add(buildLeft(), BorderLayout.CENTER);
    add(buildRight(), BorderLayout.EAST);
    add(buildBottom(), BorderLayout.SOUTH);

    refreshTable();
  }
  private void openRegisterUserView() {
    RegisterUserView registerView = new RegisterUserView();

      registerView.addSaveListener(e -> {
      JOptionPane.showMessageDialog(registerView,"Vista de registro creada correctamente.");
  });

  registerView.addCancelListener(e -> registerView.dispose());

  registerView.setVisible(true);
  }

  private JComponent buildLeft() {
    JPanel p = new JPanel(new BorderLayout(6, 6));
    p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
        var b = tableModel.getAt(table.getSelectedRow());
        selectedId = b.getAttId();
        form.setForm(
            b.getAttRole().name(),
            b.getAttUsername(),
            b.getAttFullname(),
            b.getAttState().name());
      }
    });

    p.add(new JScrollPane(table), BorderLayout.CENTER);

    JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
    

    JButton btnRegister = new JButton("Registrar");
    btnRegister.addActionListener(e -> openRegisterUserView());

    JButton btnUpdate = new JButton("Actualizar");

    JButton btnDelete = new JButton("Eliminar");
    btnDelete.addActionListener(e -> onDelete());

    top.add(btnRegister);
    top.add(btnUpdate);
    top.add(btnDelete);
   

    p.add(top, BorderLayout.NORTH);
    return p;
  }

  private JComponent buildRight() {
    JPanel p = new JPanel(new BorderLayout(6, 6));
    p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    p.add(form, BorderLayout.CENTER);

    JPanel actions = new JPanel(new GridLayout(0, 1, 6, 6));
    JButton btnSave = new JButton("Guardar");
    btnSave.addActionListener(e -> onSave());

    actions.add(btnSave);

    p.add(actions, BorderLayout.SOUTH);
    return p;
  }

  private JComponent buildBottom() {
    JPanel p = new JPanel(new BorderLayout());
    p.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
    p.add(lblStatus, BorderLayout.CENTER);
    return p;
  }

  private void onSave() {
    try {
      String role = form.role();
      String username = form.username();
      String fullname = form.fullname();
      String state = form.state();

      if (role.isBlank() || username.isBlank() || fullname.isBlank() || state.isBlank()) {
        JOptionPane.showMessageDialog(this, "Role, username, fullname and state are mandatory.");
        return;
      }

      if (selectedId == 0) {
        userController.opCreateUser(null);
      } else {
        userController.opCreateUser(null);
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void onDelete() {
    if (selectedId == 0) {
      JOptionPane.showMessageDialog(this, "Select an appointment to delete.");
      return;
    }
    int ok = JOptionPane.showConfirmDialog(this, "Remove ID " + selectedId + "?", "Ok",
        JOptionPane.YES_NO_OPTION);
    if (ok == JOptionPane.YES_OPTION) {
      try {
        // userController.delete(selectedId);
        selectedId = 0;
        form.clear();
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void refreshTable() {
    tableModel.setData(userController.opList());
  }

  @Override
  public void onEvent(ClsClinicEvent event) {

    // modify
    if (event.getType() == ClinicEventType.USER_CHANGE) {
      refreshTable();
    }
    lblStatus.setText(event.getMessage());
  }
}