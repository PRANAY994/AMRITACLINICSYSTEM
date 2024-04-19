import java.sql.*;

public class DatabaseUtils {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/oops";
    static final String USER = "root";
    static final String PASS = "Pranay@2005";

    public static String fetchStudentDetails(String rollNumber) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql = "SELECT * FROM studentdetails WHERE rollnumber='" + rollNumber + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("Age");
                int year = rs.getInt("year");
                String branch = rs.getString("branch");
                String hostelName = rs.getString("hostelname");
                int roomNumber = rs.getInt("roomnumber");
                String email = rs.getString("Email");

                return "Student Details:\n" +
                        "Name: " + name + "\n" +
                        "Age: " + age + "\n" +
                        "Year: " + year + "\n" +
                        "Branch: " + branch + "\n" +
                        "Hostel Name: " + hostelName + "\n" +
                        "Room Number: " + roomNumber + "\n" +
                        "Email: " + email + "\n";
            } else {
                return "No student found with roll number: " + rollNumber;
            }
        } catch (SQLException | ClassNotFoundException se) {
            se.printStackTrace();
            return "Error fetching student details.";
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void addAllergiesToDB(String rollNumber, String allergies) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "UPDATE studentdetails SET allergies = ? WHERE rollnumber = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, allergies);
            preparedStatement.setString(2, rollNumber);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Allergies updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update allergies for the given roll number.");
            }
        } catch (SQLException | ClassNotFoundException se) {
            se.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void addPrescriptionToDB(String rollNumber, String problem, String medicines,
                                           String bp, String temp, String timeIn, String timeOut) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "UPDATE prescription SET problem = ?, medicines = ?, bp = ?, temp = ?, time_in = ?, time_out = ? WHERE rollnumber = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, problem);
            preparedStatement.setString(2, medicines);
            preparedStatement.setString(3, bp);
            preparedStatement.setString(4, temp);
            preparedStatement.setString(5, timeIn);
            preparedStatement.setString(6, timeOut);
            preparedStatement.setString(7, rollNumber);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Prescription details updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update prescription details for the given roll number.");
            }
        } catch (SQLException | ClassNotFoundException se) {
            se.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}