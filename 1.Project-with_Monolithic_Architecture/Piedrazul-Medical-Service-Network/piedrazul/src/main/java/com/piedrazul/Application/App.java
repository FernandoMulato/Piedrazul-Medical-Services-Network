package com.piedrazul.Application;

import javax.swing.SwingUtilities;

import com.piedrazul.Infrastructure.config.AppInitializer;

public class App {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new AppInitializer().start();
    });
  }
}