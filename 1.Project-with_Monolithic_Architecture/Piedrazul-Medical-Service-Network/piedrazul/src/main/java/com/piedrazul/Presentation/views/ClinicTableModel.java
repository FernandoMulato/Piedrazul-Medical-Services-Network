package com.piedrazul.Presentation.views;

import javax.swing.table.AbstractTableModel;

import com.piedrazul.Domain.entities.ClsUser;

import java.util.ArrayList;
import java.util.List;

/**
 * View-model para JTable (Swing).
 */
public class ClinicTableModel extends AbstractTableModel {
  private final String[] columns = { "Role", "Fullname", "Username", "State" };
  private List<ClsUser> data = new ArrayList<>();

  public void setData(List<ClsUser> data) {
    this.data = new ArrayList<>(data);
    fireTableDataChanged();
  }

  public ClsUser getAt(int row) {
    return data.get(row);
  }

  @Override
  public int getRowCount() {
    return data.size();
  }

  @Override
  public int getColumnCount() {
    return columns.length;
  }

  @Override
  public String getColumnName(int column) {
    return columns[column];
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    ClsUser b = data.get(rowIndex);
    return switch (columnIndex) {
      case 0 -> b.getAttRole();
      case 1 -> b.getAttFullname();
      case 2 -> b.getAttUsername();
      case 3 -> b.getAttState();
      default -> "";
    };
  }
}
