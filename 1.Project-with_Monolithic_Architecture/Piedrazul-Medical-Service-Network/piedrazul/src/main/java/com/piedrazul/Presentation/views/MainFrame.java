package com.piedrazul.Presentation.views;

import javax.swing.*;

import com.piedrazul.Domain.core.events.ClsClinicEvent;
import com.piedrazul.Domain.core.events.IClinicObserver;
import com.piedrazul.Domain.core.events.ClinicEventType;
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
 
  private void openRegisterUserView() {
  RegisterUserView registerView = new RegisterUserView();

    registerView.addSaveListener(e -> {
    try {
      validateRoleSpecificFields(registerView);

      ClsUser user = buildUserFromRegisterView(registerView);
      userController.opCreateUser(user);

      JOptionPane.showMessageDialog(registerView, "Usuario registrado correctamente.");
      registerView.dispose();
      refreshTable();

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
      String citizenCardText = view.getCitizenCard();
      String phoneNumber = view.getPhoneNumber();

      long citizenCard = Long.parseLong(citizenCardText);

      return new ClsPatient(
        username,
        fullname,
        password,
        role,
        state,
        citizenCard,
        phoneNumber
      );
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
        specialty
      );
    }

    return new ClsUser(
      username,
      fullname,
      password,
      role,
      state
    );
  }


  private void validateRoleSpecificFields(RegisterUserView view) {
    Role role = mapRole(view.getSelectedRole());

    if (role == Role.PATIENT) {
      if (view.getCitizenCard().isBlank() || view.getPhoneNumber().isBlank()) {
        throw new IllegalArgumentException("Para registrar un paciente debe ingresar cédula y teléfono.");
      }
    }

    if (role == Role.CLINICALSTAFF) {
      if (view.getSelectedProfession() == null || view.getSelectedSpecialty() == null) {
        throw new IllegalArgumentException("Para registrar personal médico debe seleccionar profesión y especialidad.");
      }
    }
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

    JButton btnDeactivate= new JButton("Desactivar");
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

  
  private void onDeactivate() {

    if (selectedId == 0) {
        JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "¿Desea desactivar este usuario?",
        "Confirmar",
        JOptionPane.YES_NO_OPTION
    );

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