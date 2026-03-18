package com.piedrazul.Presentation.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;

public class AppointmentView extends JFrame {

    private JTextField txtPatientID;
    private JTextField txtPhoneNumber;
    private JTextField txtMedID;
    private JComboBox<String> cmbSpeciality;
    private JSpinner dateTimeSpinner;
    
    private JButton btnAgendar;
    private JButton btnLimpiar;

    public AppointmentView() {
        setTitle("PiedraAzul - Agendamiento de Citas");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("AGENDAR NUEVA CITA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        this.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 15));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        panelFormulario.add(new JLabel("Documento del Paciente:"));
        txtPatientID = new JTextField();
        panelFormulario.add(txtPatientID);

        panelFormulario.add(new JLabel("Numero Telefonico:"));
        txtPhoneNumber = new JTextField();
        panelFormulario.add(txtPhoneNumber);

        panelFormulario.add(new JLabel("Identificacion del personal medico:"));
        txtMedID = new JTextField();
        panelFormulario.add(txtMedID);

        panelFormulario.add(new JLabel("Especialidad:"));
        String[] especialidades = {"Neuroterapia", "Quiropractico", "Fisioterapia"};
        cmbSpeciality = new JComboBox<>(especialidades);
        panelFormulario.add(cmbSpeciality);

        panelFormulario.add(new JLabel("Fecha y Hora:"));
        SpinnerDateModel model = new SpinnerDateModel();
        dateTimeSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateTimeSpinner, "yyyy-MM-dd HH:mm");
        dateTimeSpinner.setEditor(editor);
        panelFormulario.add(dateTimeSpinner);

        this.add(panelFormulario, BorderLayout.CENTER);

        // Panel de Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 30));

        btnLimpiar = new JButton("Limpiar");
        btnAgendar = new JButton("Agendar Cita");
        
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnAgendar);

        this.add(panelBotones, BorderLayout.SOUTH);
    }

    public String getPatientID() {
        return txtPatientID.getText();
    }

    public String getPhoneNumber() {
        return txtPhoneNumber.getText();
    }

    public String getMedID() {
        return txtMedID.getText();
    }

    public String getEspecialidad() {
        return (String) cmbSpeciality.getSelectedItem();
    }

    public Date getDate() {
        return (Date) dateTimeSpinner.getValue();
    }

    public void setPatientID(String id) {
    txtPatientID.setText(id);
    }

    public void setPhoneNumber(String phone) {
        txtPhoneNumber.setText(phone);
    }

    public void setMedID(String id) {
        txtMedID.setText(id);
    }

    public void setSpeciality(String speciality) {
        cmbSpeciality.setSelectedItem(speciality);
    }

    public void setDate(Date date) {
        dateTimeSpinner.setValue(date);
    }

    public void addAgendarListener(ActionListener listener) {
        btnAgendar.addActionListener(listener);
    }
    
    public void addLimpiarListener(ActionListener listener) {
        btnLimpiar.addActionListener(listener);
    }

    public void showDialog (String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
}