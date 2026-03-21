package com.piedrazul.Presentation.views;

import javax.swing.*;

import java.awt.*;

public class ClinicFormPanel extends JPanel {
  private final JTextField txtRole = new JTextField(20);
  private final JTextField txtUsername = new JTextField(20);
  private final JTextField txtFullname = new JTextField(20);
  private final JTextField txtState = new JTextField(20);

  public ClinicFormPanel() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(4, 4, 4, 4);
    c.anchor = GridBagConstraints.WEST;

    int r = 0;
    add(new JLabel("Role:"), gbc(c, 0, r));
    add(txtRole, gbc(c, 1, r++));
    add(new JLabel("Username:"), gbc(c, 0, r));
    add(txtUsername, gbc(c, 1, r++));
    add(new JLabel("Fullname:"), gbc(c, 0, r));
    add(txtFullname, gbc(c, 1, r++));
    add(new JLabel("State:"), gbc(c, 0, r));
    add(txtState, gbc(c, 1, r++));
  }

  private GridBagConstraints gbc(GridBagConstraints base, int x, int y) {
    GridBagConstraints c = (GridBagConstraints) base.clone();
    c.gridx = x;
    c.gridy = y;
    return c;
  }

  public String role() {
    return txtRole.getText().trim();
  }

  public String username() {
    return txtUsername.getText().trim();
  }

  public String fullname() {
    return txtFullname.getText().trim();
  }

  public String state() {
    return txtState.getText().trim();
  }

  public void setForm(
      String role,
      String username,
      String fullname,
      String state) {
    txtRole.setText(role);
    txtUsername.setText(username);
    txtFullname.setText(fullname);
    txtState.setText(state);

  }

  public void clear() {
    setForm("", "", "", "");
  }
}
