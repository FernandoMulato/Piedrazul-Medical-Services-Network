package com.piedrazul.Presentation.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminPanelView extends JFrame {

  private JButton btnUsers;
  private JButton btnAppointments;
  private JButton btnDoctors;
  private JButton btnLogout;

  public AdminPanelView() {

    setTitle("Panel Administrador - Clínica Piedra Azul");
    setSize(600, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initComponents();
  }

  private void initComponents() {

    JPanel mainPanel = new JPanel(new BorderLayout());

    JLabel title = new JLabel("Panel de Administración", JLabel.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 20));
    title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(2, 2, 20, 20));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

    btnUsers = new JButton("Gestionar Usuarios");
    btnAppointments = new JButton("Gestionar Citas");
    btnDoctors = new JButton("Gestionar Médicos");
    btnLogout = new JButton("Cerrar Sesión");

    buttonPanel.add(btnUsers);
    buttonPanel.add(btnAppointments);
    buttonPanel.add(btnDoctors);
    buttonPanel.add(btnLogout);

    mainPanel.add(title, BorderLayout.NORTH);
    mainPanel.add(buttonPanel, BorderLayout.CENTER);

    add(mainPanel);
  }

  // ===== Eventos para controller =====

  public void addUsersListener(ActionListener listener) {
    btnUsers.addActionListener(listener);
  }

  public void addAppointmentsListener(ActionListener listener) {
    btnAppointments.addActionListener(listener);
  }

  public void addDoctorsListener(ActionListener listener) {
    btnDoctors.addActionListener(listener);
  }

  public void addLogoutListener(ActionListener listener) {
    btnLogout.addActionListener(listener);
  }
}