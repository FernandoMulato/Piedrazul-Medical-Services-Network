package com.piedrazul.Presentation.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

  private JTextField txtUser;
  private JPasswordField txtPassword;
  private JButton btnLogin;

  public LoginView() {

    setTitle("Clínica Piedra Azul - Login");
    setSize(400, 250);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initComponents();
  }

  private void initComponents() {

    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.setLayout(new GridLayout(3, 2, 10, 10));

    JLabel lblUser = new JLabel("Usuario:");
    JLabel lblPassword = new JLabel("Contraseña:");

    txtUser = new JTextField();
    txtPassword = new JPasswordField();

    btnLogin = new JButton("Iniciar Sesión");

    panel.add(lblUser);
    panel.add(txtUser);

    panel.add(lblPassword);
    panel.add(txtPassword);

    panel.add(new JLabel());
    panel.add(btnLogin);

    add(panel);
  }

  // ====== Getters para el Controller ======

  public String getUsername() {
    return txtUser.getText();
  }

  public String getPassword() {
    return new String(txtPassword.getPassword());
  }

  // ====== Registrar eventos ======

  public void addLoginListener(ActionListener listener) {
    btnLogin.addActionListener(listener);
  }

  // ====== Utilidades ======

  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }
}