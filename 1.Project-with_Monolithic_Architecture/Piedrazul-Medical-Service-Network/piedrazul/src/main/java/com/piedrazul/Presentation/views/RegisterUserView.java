package com.piedrazul.Presentation.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterUserView extends JFrame {

  private final JComboBox<String> cmbRole = new JComboBox<>(
      new String[] { "ADMINISTRADOR", "PACIENTE", "PERSONAL MEDICO", "AGENDADOR DE CITAS" });

  private final JTextField txtUsername = new JTextField(20);
  private final JTextField txtFullname = new JTextField(20);
  private final JPasswordField txtPassword = new JPasswordField(20);
  private final JComboBox<String> cmbState = new JComboBox<>(new String[] { "Activo", "Inactivo" });

  // Panel dinámico
  private final JPanel dynamicPanel = new JPanel(new CardLayout());

  // ADMINISTRATOR / APPOINTMENTMANAGER
  private final JPanel emptyPanel = new JPanel();

  // PATIENT
  private final JTextField txtCitizenCard = new JTextField(20);
  private final JTextField txtPhoneNumber = new JTextField(20);

  // CLINICALSTAFF
  private final JComboBox<String> cmbProfession = new JComboBox<>(new String[] { "Medico", "Terapista" });
  private final JComboBox<String> cmbSpecialty = new JComboBox<>(new String[] { "Neurologia", "Quiropráctica", "Fisioterapia" });

  private final JButton btnSave = new JButton("Guardar");
  private final JButton btnCancel = new JButton("Cancelar");

  public RegisterUserView() {
    setTitle("Registrar Usuario - Clínica Piedra Azul");
    setSize(560, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    initComponents();
    initDynamicBehavior();
  }

  private void initComponents() {
    JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    JLabel title = new JLabel("Registrar Usuario", JLabel.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 20));
    title.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

    JPanel formPanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(6, 6, 6, 6);
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;

    int row = 0;

    addField(formPanel, c, row++, "Rol:", cmbRole);
    addField(formPanel, c, row++, "Usuario:", txtUsername);
    addField(formPanel, c, row++, "Nombre completo:", txtFullname);
    addField(formPanel, c, row++, "Contraseña:", txtPassword);
    addField(formPanel, c, row++, "Estado:", cmbState);

    dynamicPanel.add(emptyPanel, "ADMINISTRADOR");
    dynamicPanel.add(buildPatientPanel(), "PACIENTE");
    dynamicPanel.add(buildClinicalStaffPanel(), "PERSONAL MEDICO");
    dynamicPanel.add(new JPanel(), "AGENDADOR DE CITAS");

    c.gridx = 0;
    c.gridy = row;
    c.gridwidth = 2;
    c.weightx = 1;
    formPanel.add(dynamicPanel, c);

    JPanel actionsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
    actionsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
    actionsPanel.add(btnSave);
    actionsPanel.add(btnCancel);

    mainPanel.add(title, BorderLayout.NORTH);
    mainPanel.add(formPanel, BorderLayout.CENTER);
    mainPanel.add(actionsPanel, BorderLayout.SOUTH);

    add(mainPanel);
  }

  private JPanel buildPatientPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Datos del paciente"));

    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(6, 6, 6, 6);
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;

    addField(panel, c, 0, "Documento de identidad:", txtCitizenCard);
    addField(panel, c, 1, "Numero de teléfono:", txtPhoneNumber);

    return panel;
  }

  private JPanel buildClinicalStaffPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Datos del personal medico"));

    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(6, 6, 6, 6);
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;

    addField(panel, c, 0, "Profesion:", cmbProfession);
    addField(panel, c, 1, "Especialidad:", cmbSpecialty);

    return panel;
  }

  private void addField(JPanel panel, GridBagConstraints c, int row, String label, JComponent component) {
    c.gridx = 0;
    c.gridy = row;
    c.weightx = 0;
    panel.add(new JLabel(label), c);

    c.gridx = 1;
    c.weightx = 1;
    panel.add(component, c);
  }

  private void initDynamicBehavior() {
    cmbRole.addActionListener(e -> updateDynamicPanel());
    updateDynamicPanel();
  }

  private void updateDynamicPanel() {
    String role = (String) cmbRole.getSelectedItem();
    CardLayout cl = (CardLayout) dynamicPanel.getLayout();
    cl.show(dynamicPanel, role);
  }

  public void addSaveListener(ActionListener listener) {
    btnSave.addActionListener(listener);
  }

  public void addCancelListener(ActionListener listener) {
    btnCancel.addActionListener(listener);
  }
}