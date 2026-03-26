package com.piedrazul.Presentation.views;

import javax.swing.*;

import com.piedrazul.Domain.core.events.ClsClinicEvent;
import com.piedrazul.Domain.core.events.IClinicObserver;
import com.piedrazul.Domain.core.events.ClinicEventType;
import com.piedrazul.Presentation.controller.AdminPanelController;
import com.piedrazul.Presentation.controller.UserController;

import com.piedrazul.Domain.entities.ClsClinicalStaff;
import com.piedrazul.Domain.entities.ClsPatient;
import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Domain.enums.Profession;
import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Domain.enums.Specialty;
import com.piedrazul.Domain.enums.State;


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

  private void openUpdateUserView() {
    if (selectedId == 0) {
      JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para actualizar.");
      return;
    }

    ClsUser user = userController.opGetUser(selectedId);
    if (user == null) {
      JOptionPane.showMessageDialog(this, "No se pudo obtener el usuario seleccionado.", "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    UpdateUserView updateView = new UpdateUserView(user);

    updateView.addSaveListener(e -> {
      try {
        ClsUser updated = buildUserFromUpdateView(updateView);
        userController.opUpdateUser(updated);

        refreshTable();
        updateView.dispose();

        JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.");
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(updateView, ex.getMessage(), "Error de actualización",
            JOptionPane.ERROR_MESSAGE);
      }
    });

    updateView.addCancelListener(e -> updateView.dispose());
    updateView.setVisible(true);
  }

  private ClsUser buildUserFromUpdateView(UpdateUserView view) {
    String username = view.getUsername();
    String fullname = view.getFullname();
    String password = view.getPassword();
    Role role = mapRole(view.getSelectedRole());
    State state = mapState(view.getSelectedState());
    long id = view.getUserId();

    ClsUser updated;

    if (role == Role.PATIENT) {
      long citizenCard = Long.parseLong(view.getCitizenCard());
      String phoneNumber = view.getPhoneNumber();
      updated = new com.piedrazul.Domain.entities.ClsPatient(
          username, fullname, password, role, state, citizenCard, phoneNumber);
    } else if (role == Role.CLINICALSTAFF) {
      com.piedrazul.Domain.enums.Profession profession = mapProfession(view.getSelectedProfession());
      com.piedrazul.Domain.enums.Specialty specialty = mapSpecialty(view.getSelectedSpecialty());
      updated = new com.piedrazul.Domain.entities.ClsClinicalStaff(
          username, fullname, password, role, state, profession, specialty);
    } else {
      updated = new ClsUser(username, fullname, password, role, state);
    }

    updated.setAttId(id);
    return updated;
  }

  private void openRegisterUserView() {
    RegisterUserView registerView = new RegisterUserView();

    registerView.addSaveListener(e -> {
      try {
        ClsUser user = buildUserFromRegisterView(registerView);
        userController.opCreateUser(user);

        refreshTable();
        registerView.dispose();

        JOptionPane.showMessageDialog(
            this,
            "Usuario registrado correctamente.");
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(
            registerView,
            ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    });

    registerView.addCancelListener(e -> registerView.dispose());

    registerView.setVisible(true);
  }

  private ClsUser buildUserFromRegisterView(RegisterUserView view) {
  String username = view.getUsername();
  String fullname = view.getFullname();
  String password = view.getPassword();

  Role role = mapRole(view.getSelectedRole());
  State state = mapState(view.getSelectedState());

  if (role == Role.PATIENT) {
    long citizenCard = Long.parseLong(view.getCitizenCard());
    String phoneNumber = view.getPhoneNumber();

    return new ClsPatient(
        username,
        fullname,
        password,
        role,
        state,
        citizenCard,
        phoneNumber);
  }

  if (role == Role.CLINICALSTAFF) {
    Profession profession = mapProfession(view.getSelectedProfession());
    Specialty specialty = mapSpecialty(view.getSelectedSpecialty());

    return new ClsClinicalStaff(
        username,
        fullname,
        password,
        role,
        state,
        profession,
        specialty);
  }

  return new ClsUser(
      username,
      fullname,
      password,
      role,
      state);
  }

  private Role mapRole(String roleText) {
    return switch (roleText) {
      case "ADMINISTRADOR" -> Role.ADMINISTRATOR;
      case "PACIENTE" -> Role.PATIENT;
      case "PERSONAL MEDICO" -> Role.CLINICALSTAFF;
      case "AGENDADOR DE CITAS" -> Role.APPOINTMENTMANAGER;
      default -> throw new IllegalArgumentException("Rol no válido");
    };
  }

  private State mapState(String stateText) {
    return switch (stateText) {
      case "Activo" -> State.ACTIVE;
      case "Inactivo" -> State.INACTIVE;
      default -> throw new IllegalArgumentException("Estado no válido");
    };
  }

  private Profession mapProfession(String professionText) {
    return switch (professionText) {
      case "Medico" -> Profession.MEDIC;
      case "Terapista" -> Profession.THERAPIST;
      default -> throw new IllegalArgumentException("Profesión no válida");
    };
  }

  private Specialty mapSpecialty(String specialtyText) {
    return switch (specialtyText) {
      case "Neurologia" -> Specialty.NEURAL;
      case "Quiropráctica" -> Specialty.CHIROPRACTIC;
      case "Fisioterapia" -> Specialty.PHYSIO;
      default -> throw new IllegalArgumentException("Especialidad no válida");
    };
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
    btnUpdate.addActionListener(e -> openUpdateUserView());

    JButton btnDeactivate = new JButton("Desactivar");
    btnDeactivate.addActionListener(e -> onDeactivate());

    top.add(btnRegister);
    top.add(btnUpdate);
    top.add(btnDeactivate);

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

    JButton btnBack = new JButton("Volver");

    btnBack.addActionListener(e -> goBack());

    p.add(lblStatus, BorderLayout.CENTER);
    p.add(btnBack, BorderLayout.EAST); 

    return p;
  }

  private void goBack() {
    this.dispose();

    AdminPanelView adminPanel = new AdminPanelView();

    new AdminPanelController(
        adminPanel,
        null,           
        userController  
    );

    adminPanel.setVisible(true);
  }
  private void onSave() {
    // El panel derecho (ClinicFormPanel) es solo para visualizar el usuario seleccionado.
    // Para registrar un nuevo usuario use el botón "Registrar".
    // Para editar un usuario existente use el botón "Actualizar".
    if (selectedId > 0) {
      openUpdateUserView();
    } else {
      JOptionPane.showMessageDialog(this,
          "Para registrar un usuario use el botón 'Registrar'.\nPara editar, seleccione un usuario y use 'Actualizar'.");
    }
  }

  private void onDeactivate() {

    if (selectedId == 0) {
      JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "¿Desea desactivar este usuario?",
        "Confirmar",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      try {
        userController.opDeactivateUser(selectedId);
        refreshTable();
        JOptionPane.showMessageDialog(this, "Usuario desactivado correctamente.");
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
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