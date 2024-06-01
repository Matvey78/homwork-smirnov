
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class EmployeeApp extends JFrame {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/employees";

    // Database credentials
    static final String USER = "root";
    static final String PASS = "mATVEYvASI1";

    private JTextField tfName, tfPosition;
    private JButton btnSave, btnRetrieve, btnDelete, btnEdit;
    private JTable table;
    private DefaultTableModel model;
    private Connection conn = null;
    private Statement stmt = null;

    public EmployeeApp() {
        createUI();
        connectDatabase();
    }

    private void connectDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table Model
        model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Position");

        // Table
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel for inputs and buttons
        JPanel panel = new JPanel();
        tfName = new JTextField(20);
        tfPosition = new JTextField(20);
        btnSave = new JButton("Save");
        btnRetrieve = new JButton("Retrieve");
        btnDelete = new JButton("Delete");
        btnEdit = new JButton("Edit");

        panel.add(new JLabel("Name:"));
        panel.add(tfName);
        panel.add(new JLabel("Position:"));
        panel.add(tfPosition);
        panel.add(btnSave);
        panel.add(btnRetrieve);
        panel.add(btnDelete);
        panel.add(btnEdit);
        add(panel, BorderLayout.SOUTH);

        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEmployee();
            }
        });

        btnRetrieve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                retrieveEmployee();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editEmployee();
            }
        });

        setSize(600, 400);
        setVisible(true);
    }

    private void saveEmployee() {
        String name = tfName.getText();
        String position = tfPosition.getText();
        if (name.isEmpty() || position.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name or position cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String sql = "INSERT INTO employees (name, position) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Employee saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                retrieveEmployee(); // Обновляем таблицу
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void retrieveEmployee() {
        try {
            String sql = "SELECT * FROM employees";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            model.setRowCount(0); // Очищаем таблицу перед заполнением
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("name"), rs.getString("position")});
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String name = (String) model.getValueAt(row, 0);
        try {
            String sql = "DELETE FROM employees WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Employee deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                retrieveEmployee(); // Обновляем таблицу
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editEmployee() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String name = (String) model.getValueAt(row, 0);
        String position = (String) model.getValueAt(row, 1);
        tfName.setText(name);
        tfPosition.setText(position);
        btnSave.setText("Update");
        btnSave.removeActionListener(btnSave.getActionListeners()[0]);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateEmployee(name);
            }
        });
    }

    private void updateEmployee(String oldName) {
        String name = tfName.getText();
        String position = tfPosition.getText();
        if (name.isEmpty() || position.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name or position cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String sql = "UPDATE employees SET name = ?, position = ? WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setString(3, oldName);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Employee updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                retrieveEmployee(); // Обновляем таблицу
                btnSave.setText("Save");
                btnSave.removeActionListener(btnSave.getActionListeners()[0]);
                btnSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        saveEmployee();
                    }
                });
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new EmployeeApp();
    }
}
