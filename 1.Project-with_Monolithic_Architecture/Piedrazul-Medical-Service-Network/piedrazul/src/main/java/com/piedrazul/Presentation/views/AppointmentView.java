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
    private JTextField txtReason;
    private JTextField txtAppointmentId;
    
    private JButton btnAgendar;
    private JButton btnReagendar;
    private JButton btnExportar;
    private JButton btnLimpiar;

    public AppointmentView() {
        setTitle("PiedraAzul - Agendamiento de Citas");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));

        JTabbedPane tabMain = new JTabbedPane();
        this.add(tabMain);

        //  Panel agendar citas
        JLabel lblScheduleTitle = new JLabel("Panel de gestion de citas", SwingConstants.CENTER);
        lblScheduleTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblScheduleTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        this.add(lblScheduleTitle, BorderLayout.NORTH);

        JPanel panelSchedule = new JPanel(new GridLayout(0, 2, 10, 15));
        panelSchedule.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        panelSchedule.add(new JLabel("Documento del Paciente:"));
        txtPatientID = new JTextField();
        panelSchedule.add(txtPatientID);

        panelSchedule.add(new JLabel("Numero Telefonico:"));
        txtPhoneNumber = new JTextField();
        panelSchedule.add(txtPhoneNumber);

        panelSchedule.add(new JLabel("Identificacion del personal medico:"));
        txtMedID = new JTextField();
        panelSchedule.add(txtMedID);

        panelSchedule.add(new JLabel("Especialidad:"));
        String[] especialidades = {"Neuroterapia", "Quiropractico", "Fisioterapia"};
        cmbSpeciality = new JComboBox<>(especialidades);
        panelSchedule.add(cmbSpeciality);

        panelSchedule.add(new JLabel("Fecha y Hora:"));
        SpinnerDateModel model = new SpinnerDateModel();
        dateTimeSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateTimeSpinner, "yyyy-MM-dd HH:mm");
        dateTimeSpinner.setEditor(editor);
        panelSchedule.add(dateTimeSpinner);

        panelSchedule.add(new JLabel("Motivo de la cita:"));
        txtReason = new JTextField();
        panelSchedule.add(txtReason);

        JPanel panelButtonsSchedule = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtonsSchedule.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 30));

        btnLimpiar = new JButton("Limpiar");
        btnAgendar = new JButton("Agendar Cita");
        btnExportar = new JButton("Exportar Citas");
        
        panelButtonsSchedule.add(btnLimpiar);
        panelButtonsSchedule.add(btnAgendar);

        panelSchedule.add(panelButtonsSchedule, BorderLayout.SOUTH);

        tabMain.add(panelSchedule, "Agendar cita");
        
        //  Panel reagendar citas
        JPanel panelReSchedule = new JPanel(new GridLayout(0, 2, 10, 15));
        panelReSchedule.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        
        panelReSchedule.add(new JLabel("ID Cita (para reagendar):"));
        txtAppointmentId = new JTextField();
        panelReSchedule.add(txtAppointmentId);

        JButton btnSearchAppointment = new JButton("Buscar cita");
        JTable tabScheduleInfo = new JTable();
        panelReSchedule.add(btnSearchAppointment);
        panelReSchedule.add(tabScheduleInfo);

        panelReSchedule.add(new JLabel("Fecha y Hora nueva para reagendar:"));
        SpinnerDateModel model2 = new SpinnerDateModel();
        dateTimeSpinner = new JSpinner(model2);
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(dateTimeSpinner, "yyyy-MM-dd HH:mm");
        dateTimeSpinner.setEditor(editor2);
        panelReSchedule.add(dateTimeSpinner);

        JPanel panelButtonsReSchedule = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtonsReSchedule.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 30));

        btnLimpiar = new JButton("Limpiar");
        btnReagendar = new JButton("Reagendar Cita");
        btnExportar = new JButton("Exportar Citas");
        
        panelButtonsReSchedule.add(btnLimpiar);
        panelButtonsReSchedule.add(btnReagendar);

        panelReSchedule.add(panelButtonsReSchedule, BorderLayout.SOUTH);

        tabMain.add(panelReSchedule, "Reagendar cita");
    }

    // ===== Getters =====

    public String getAppointmentId() {
        return txtAppointmentId.getText();
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

    public String getReason() {
        return txtReason.getText();
    }

    // ===== Setters =====

    public void setAppointmentId(String id) {
        txtAppointmentId.setText(id);
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

    public void setReason(String reason) {
        txtReason.setText(reason);
    }

    // ===== Listeners =====

    public void addAgendarListener(ActionListener listener) {
        btnAgendar.addActionListener(listener);
    }

    public void addReagendarListener(ActionListener listener) {
        btnReagendar.addActionListener(listener);
    }

    public void addExportarListener(ActionListener listener) {
        btnExportar.addActionListener(listener);
    }
    
    public void addLimpiarListener(ActionListener listener) {
        btnLimpiar.addActionListener(listener);
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
}