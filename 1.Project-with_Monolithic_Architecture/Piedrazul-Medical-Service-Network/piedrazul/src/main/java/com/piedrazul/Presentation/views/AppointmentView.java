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
    private JSpinner dateTimeSpinnerSchedule;
    private JTextField txtReason;

    private JTextField txtAppointmentId;
    private JTable tabScheduleInfo;
    private JSpinner dateTimeSpinnerReSchedule;
    
    private JButton btnSchedule;
    private JButton btnReSchedule;
    private JButton btnExportSchedule;
    private JButton btnExportReSchedule;
    private JButton btnClearSchedule;
    private JButton btnClearReSchedule;
    private JButton btnSearchAppointment;

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
        dateTimeSpinnerSchedule = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateTimeSpinnerSchedule, "yyyy-MM-dd HH:mm");
        dateTimeSpinnerSchedule.setEditor(editor);
        panelSchedule.add(dateTimeSpinnerSchedule);

        panelSchedule.add(new JLabel("Motivo de la cita:"));
        txtReason = new JTextField();
        panelSchedule.add(txtReason);

        JPanel panelButtonsSchedule = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtonsSchedule.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 30));

        btnClearSchedule = new JButton("Limpiar");
        btnSchedule = new JButton("Agendar Cita");
        btnExportSchedule = new JButton("Exportar Citas");
        
        panelButtonsSchedule.add(btnClearSchedule);
        panelButtonsSchedule.add(btnSchedule);

        panelSchedule.add(panelButtonsSchedule, BorderLayout.SOUTH);

        tabMain.add(panelSchedule, "Agendar cita");
        
        //  PANEL REAGENDAR CITAS
        JPanel panelReSchedule = new JPanel(new GridLayout(0, 2, 10, 15));
        panelReSchedule.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        
        panelReSchedule.add(new JLabel("ID Cita (para reagendar):"));
        txtAppointmentId = new JTextField();
        panelReSchedule.add(txtAppointmentId);

        btnSearchAppointment = new JButton("Buscar cita");
        tabScheduleInfo = new JTable();
        panelReSchedule.add(btnSearchAppointment);
        panelReSchedule.add(new JScrollPane(tabScheduleInfo));

        panelReSchedule.add(new JLabel("Fecha y Hora nueva para reagendar:"));
        SpinnerDateModel model2 = new SpinnerDateModel();
        dateTimeSpinnerReSchedule = new JSpinner(model2);
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(dateTimeSpinnerReSchedule, "yyyy-MM-dd HH:mm");
        dateTimeSpinnerReSchedule.setEditor(editor2);
        panelReSchedule.add(dateTimeSpinnerReSchedule);

        JPanel panelButtonsReSchedule = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtonsReSchedule.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 30));

        btnClearReSchedule = new JButton("Limpiar");
        btnReSchedule = new JButton("Reagendar Cita");
        btnExportReSchedule = new JButton("Exportar Citas");
        
        panelButtonsReSchedule.add(btnClearReSchedule);
        panelButtonsReSchedule.add(btnReSchedule);

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

    public Date getScheduleDate() {
        return (Date) dateTimeSpinnerSchedule.getValue();
    }

    public Date getReScheduleDate() {
        return (Date) dateTimeSpinnerReSchedule.getValue();
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

    public void setScheduleDate(Date date) {
        dateTimeSpinnerSchedule.setValue(date);
    }

    public void setReScheduleDate(Date date) {
        dateTimeSpinnerReSchedule.setValue(date);
    }
    
    public void setReason(String reason) {
        txtReason.setText(reason);
    }

    public void setScheduleInfoTableModel(javax.swing.table.DefaultTableModel model) {
        tabScheduleInfo.setModel(model);
    }

    public JPanel getSchedulePanel() {
        return (JPanel) ((JTabbedPane) getContentPane().getComponent(0)).getComponentAt(0);
    }

    public JPanel getReSchedulePanel() {
        return (JPanel) ((JTabbedPane) getContentPane().getComponent(0)).getComponentAt(1);
    }

    // ===== Listeners =====

    public void addScheduleListener(ActionListener listener) {
        btnSchedule.addActionListener(listener);
    }

    public void addReScheduleListener(ActionListener listener) {
        btnReSchedule.addActionListener(listener);
    }

    public void addExportListener(ActionListener listener) {
        btnExportSchedule.addActionListener(listener);
        btnExportReSchedule.addActionListener(listener);
    }
    
    public void addClearScheduleListener(ActionListener listener) {
        btnClearSchedule.addActionListener(listener);
    }

    public void addClearReScheduleListener(ActionListener listener) {
        btnClearReSchedule.addActionListener(listener);
    }

    public void addSearchAppointmentListener(ActionListener listener) {
        btnSearchAppointment.addActionListener(listener);
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
}
