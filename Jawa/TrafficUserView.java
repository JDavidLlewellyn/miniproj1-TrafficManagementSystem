import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class TrafficUserView extends JFrame {

    static class DBConfig {
        static final String URL = "jdbc:mysql://localhost:3306/traffic_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        static final String USER = "Ash";
        static final String PASS = "aswanthp@karunya.edu.in";
    }

    static class DBUtil {
        static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASS);
        }
    }

    private JTable vehicleTable, signalTable, violationTable;
    private DefaultTableModel vehicleModel, signalModel, violationModel;

    public TrafficUserView() {
        super("Traffic Management System - User View");

        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Vehicles", createVehiclePanel());
        tabs.addTab("Signals", createSignalPanel());
        tabs.addTab("Violations", createViolationPanel());

        add(tabs);

        loadVehicles();
        loadSignals();
        loadViolations();
    }

    private JPanel createVehiclePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        vehicleModel = new DefaultTableModel(
                new String[]{"ID", "Plate", "Owner", "Type", "Registered Date"}, 0);

        vehicleTable = new JTable(vehicleModel);

        panel.add(new JScrollPane(vehicleTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSignalPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        signalModel = new DefaultTableModel(
                new String[]{"ID", "Location", "Status", "Last Inspection"}, 0);

        signalTable = new JTable(signalModel);

        panel.add(new JScrollPane(signalTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createViolationPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        violationModel = new DefaultTableModel(
                new String[]{"ID", "Plate", "Location", "Violation Type", "Fine", "Date"}, 0);

        violationTable = new JTable(violationModel);

        panel.add(new JScrollPane(violationTable), BorderLayout.CENTER);

        return panel;
    }

    private void loadVehicles() {
        vehicleModel.setRowCount(0);

        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM vehicles")) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();

                row.add(rs.getInt("id"));
                row.add(rs.getString("plate"));
                row.add(rs.getString("owner"));
                row.add(rs.getString("type"));
                row.add(rs.getDate("registered_date"));

                vehicleModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void loadSignals() {
        signalModel.setRowCount(0);

        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM signals")) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();

                row.add(rs.getInt("id"));
                row.add(rs.getString("location"));
                row.add(rs.getString("status"));
                row.add(rs.getDate("last_inspection"));

                signalModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void loadViolations() {
        violationModel.setRowCount(0);

        String sql = "SELECT v.id, ve.plate, s.location, v.violation_type, v.fine, v.violation_date " +
                "FROM violations v " +
                "LEFT JOIN vehicles ve ON v.vehicle_id = ve.id " +
                "LEFT JOIN signals s ON v.signal_id = s.id";

        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();

                row.add(rs.getInt(1));
                row.add(rs.getString(2));
                row.add(rs.getString(3));
                row.add(rs.getString(4));
                row.add(rs.getDouble(5));
                row.add(rs.getTimestamp(6));

                violationModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Driver Error");
        }

        SwingUtilities.invokeLater(() -> {
            new TrafficUserView().setVisible(true);
        });
    }
}