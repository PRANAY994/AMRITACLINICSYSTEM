import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MedicalCertificateDialog extends JFrame {
    public MedicalCertificateDialog(String rollNumber) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle("Medical Certificate");

        Connection conn = null;
        PreparedStatement studentDetailsStatement = null;
        PreparedStatement prescriptionStatement = null;
        try {
            conn = DatabaseConnection.getConnection();

            // Fetch student details
            String studentDetailsQuery = "SELECT * FROM studentdetails WHERE rollnumber=?";
            studentDetailsStatement = conn.prepareStatement(studentDetailsQuery);
            studentDetailsStatement.setString(1, rollNumber);
            ResultSet studentDetailsResultSet = studentDetailsStatement.executeQuery();

            // Fetch prescription details
            String prescriptionQuery = "SELECT * FROM prescription WHERE rollnumber=?";
            prescriptionStatement = conn.prepareStatement(prescriptionQuery);
            prescriptionStatement.setString(1, rollNumber);
            ResultSet prescriptionResultSet = prescriptionStatement.executeQuery();

            if (studentDetailsResultSet.next() && prescriptionResultSet.next()) {
                String name = studentDetailsResultSet.getString("name");
                Integer age = studentDetailsResultSet.getInt("age");
                String problem = prescriptionResultSet.getString("problem");
                String timeIn = prescriptionResultSet.getString("time_in");
                String timeOut = prescriptionResultSet.getString("time_out");
                String bp = prescriptionResultSet.getString("bp");
                String temp = prescriptionResultSet.getString("temp");

                JTextArea medicalCertificateTextArea = new JTextArea();
                medicalCertificateTextArea.setEditable(false);
                medicalCertificateTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

                // Add current date
                String currentDate = Utils.getCurrentDate();

                // Retrieve doctor's name from user input
                String doctorName = JOptionPane.showInputDialog("Enter Doctor's Name:");

                // Build the content of the medical certificate
                String medicalCertificateContent = "AMRITA CLINIC\n\n";

                // Add current date
                medicalCertificateContent += "Date: " + currentDate + "\n\n";

                // Add doctor's name
                medicalCertificateContent += "Issued By: Dr. " + doctorName + "\n\n";

                // Student details
                medicalCertificateContent += "Student Details:\n" +
                        "Name: " + name + "\n" +
                        "Age: " + age + "\n " +
                        "Roll Number: " + rollNumber + "\n\n" +
                        "Medical Observation Period:\n" +
                        "Time In: " + timeIn + "\n" +
                        "Time Out: " + timeOut + "\n\n" +
                        "Problem: " + problem + "\n\n" + "Blood Pressure: " + bp + "\n" +
                        "Temperature: " + temp + "\n\n" +
                        "This is to certify that the above-named student has been under medical observation during the specified period.\n";

                medicalCertificateTextArea.setText(medicalCertificateContent);

                // Add a "Print" button
                JButton printButton = new JButton("Print");
                printButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Implement the logic to print the medical certificate
                        // You can use a printing API or open a print dialog here
                        // Example: implementPrintLogic(medicalCertificateContent);
                    }
                });

                // Heading panel with "MEDICAL CERTIFICATE" in bold
                JPanel headingPanel = new JPanel();
                JLabel headingLabel = new JLabel("<html><b>MEDICAL CERTIFICATE</b></html>");
                headingLabel.setFont(new Font("Arial", Font.BOLD, 16));
                headingPanel.add(headingLabel);

                // Button panel with "Print" button
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(printButton);

                // Main content panel
                JPanel contentPanel = new JPanel(new BorderLayout());
                contentPanel.add(headingPanel, BorderLayout.NORTH);
                contentPanel.add(new JScrollPane(medicalCertificateTextArea), BorderLayout.CENTER);
                contentPanel.add(buttonPanel, BorderLayout.SOUTH);

                add(contentPanel);
                setSize(600, 500);
                setLocationRelativeTo(null);
            } else {
                JOptionPane.showMessageDialog(this, "No student or prescription found for roll number: " + rollNumber);
                dispose();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (studentDetailsStatement != null)
                    studentDetailsStatement.close();
                if (prescriptionStatement != null)
                    prescriptionStatement.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}