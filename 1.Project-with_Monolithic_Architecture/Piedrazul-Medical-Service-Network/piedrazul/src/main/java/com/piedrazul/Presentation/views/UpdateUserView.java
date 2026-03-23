package com.piedrazul.Presentation.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import com.piedrazul.Domain.entities.ClsUser;

/**
 * Vista para actualizar un usuario existente.
 * Se pre-popula con los datos del usuario seleccionado.
 * <p>
 * Cubre los escenarios de E1-HU2:
 * <ul>
 *   <li>Escenario 1: Actualización exitosa con datos válidos.</li>
 *   <li>Escenario 2: Actualización denegada por datos inválidos o inconsistentes.</li>
 *   <li>Escenario 3: Bloqueo si se cambia rol a médico sin asociación a profesional.</li>
 * </ul>
 */
public class UpdateUserView extends JFrame {

    private long userId; // ID del usuario que se está editando

    private final JComboBox<String> cmbRole = new JComboBox<>(
            new String[] { "ADMINISTRADOR", "PACIENTE", "PERSONAL MEDICO", "AGENDADOR DE CITAS" });

    private final JTextField txtUsername = new JTextField(20);
    private final JTextField txtFullname = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JComboBox<String> cmbState = new JComboBox<>(new String[] { "Activo", "Inactivo" });

    // Panel dinámico según rol
    private final JPanel dynamicPanel = new JPanel(new CardLayout());
    private final JPanel emptyPanel = new JPanel();

    // PATIENT
    private final JTextField txtCitizenCard = new JTextField(20);
    private final JTextField txtPhoneNumber = new JTextField(20);
    private final JPanel panelPatientFields = buildPatientPanel();

    // CLINICALSTAFF
    private final JComboBox<String> cmbProfession = new JComboBox<>(new String[] { "Medico", "Terapista" });
    private final JComboBox<String> cmbSpecialty = new JComboBox<>(
            new String[] { "Neurologia", "Quiropráctica", "Fisioterapia" });
    private final JPanel panelClinicalFields = buildClinicalStaffPanel();

    private final JButton btnSave = new JButton("Guardar cambios");
    private final JButton btnCancel = new JButton("Cancelar");

    /**
     * Crea la vista de actualización y la pre-popula con los datos del usuario.
     *
     * @param user usuario cuyos datos se van a editar
     */
    public UpdateUserView(ClsUser user) {
        setTitle("Actualizar Usuario - Clínica Piedra Azul");
        setSize(560, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        initDynamicBehavior();
        populate(user);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Actualizar Usuario", JLabel.CENTER);
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

        JLabel lblPwdHint = new JLabel("(mínimo 8 caracteres)");
        lblPwdHint.setFont(new Font("Arial", Font.ITALIC, 11));
        lblPwdHint.setForeground(Color.GRAY);
        c.gridx = 1; c.gridy = row++;
        formPanel.add(lblPwdHint, c);

        addField(formPanel, c, row++, "Estado:", cmbState);

        dynamicPanel.add(emptyPanel, "ADMINISTRADOR");
        dynamicPanel.add(panelPatientFields, "PACIENTE");
        dynamicPanel.add(panelClinicalFields, "PERSONAL MEDICO");
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
        addField(panel, c, 1, "Número de teléfono:", txtPhoneNumber);
        return panel;
    }

    private JPanel buildClinicalStaffPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del personal médico"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        addField(panel, c, 0, "Profesión:", cmbProfession);
        addField(panel, c, 1, "Especialidad:", cmbSpecialty);
        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints c, int row, String label, JComponent component) {
        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(new JLabel(label), c);

        c.gridx = 1;
        c.weightx = 1;
        panel.add(component, c);
    }

    private void initDynamicBehavior() {
        cmbRole.addActionListener(e -> {
            String role = (String) cmbRole.getSelectedItem();
            CardLayout cl = (CardLayout) dynamicPanel.getLayout();
            cl.show(dynamicPanel, role);
        });
    }

    /**
     * Pre-popula el formulario con los datos del usuario existente.
     * <p>
     * La contraseña NO se pre-popula por seguridad; debe ingresarse nuevamente.
     */
    private void populate(ClsUser user) {
        this.userId = user.getAttId();

        // Mapear rol enum → texto del combo
        String roleText = switch (user.getAttRole()) {
            case ADMINISTRATOR -> "ADMINISTRADOR";
            case PATIENT -> "PACIENTE";
            case CLINICALSTAFF -> "PERSONAL MEDICO";
            case APPOINTMENTMANAGER -> "AGENDADOR DE CITAS";
        };
        cmbRole.setSelectedItem(roleText);

        txtUsername.setText(user.getAttUsername());
        txtFullname.setText(user.getAttFullname());
        // La contraseña no se pre-popula: el admin debe ingresarla de nuevo
        txtPassword.setText("");

        // Mapear estado enum → texto del combo
        cmbState.setSelectedItem(user.getAttState().name().equals("ACTIVE") ? "Activo" : "Inactivo");

        // Actualizar el panel dinámico según el rol cargado
        CardLayout cl = (CardLayout) dynamicPanel.getLayout();
        cl.show(dynamicPanel, roleText);
    }

    // ===== Listeners =====

    public void addSaveListener(ActionListener listener) {
        btnSave.addActionListener(listener);
    }

    public void addCancelListener(ActionListener listener) {
        btnCancel.addActionListener(listener);
    }

    // ===== Getters =====

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getFullname() {
        return txtFullname.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public String getSelectedRole() {
        return (String) cmbRole.getSelectedItem();
    }

    public String getSelectedState() {
        return (String) cmbState.getSelectedItem();
    }

    public String getCitizenCard() {
        return txtCitizenCard.getText().trim();
    }

    public String getPhoneNumber() {
        return txtPhoneNumber.getText().trim();
    }

    public String getSelectedProfession() {
        return (String) cmbProfession.getSelectedItem();
    }

    public String getSelectedSpecialty() {
        return (String) cmbSpecialty.getSelectedItem();
    }
}