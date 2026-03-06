package com.piedrazul;

import javax.swing.SwingUtilities;

import com.piedrazul.Presentation.views.AdminPanelView;
import com.piedrazul.Presentation.views.LoginView;

public class App {

  public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {

      LoginView loginView = new LoginView();
      loginView.setVisible(true);

    });

    SwingUtilities.invokeLater(() -> {

      AdminPanelView view = new AdminPanelView();
      view.setVisible(true);

    });

  }
}