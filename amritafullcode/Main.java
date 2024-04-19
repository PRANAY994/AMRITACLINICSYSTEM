    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.sql.*;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.List;
    import java.text.SimpleDateFormat;
    import java.util.Date;


    public class Main extends JFrame {
        static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        static final String DB_URL = "jdbc:mysql://localhost/oops";
        static final String USER = "root";
        static final String PASS = "Pranay@2005";

        private JTextField rollNumberField;
        private JTextArea resultArea;
        private String rollNumber;

        public Main() {
            setTitle("Student Details");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            setSize(800, 600);
            ImageIcon mainIcon = new ImageIcon("D:images (1).png");
            setIconImage(mainIcon.getImage());
            setMinimumSize(new Dimension(1000, 700));


            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new FlowLayout());
            JPanel buttonPanel = new JPanel();
            inputPanel.add(buttonPanel, BorderLayout.SOUTH);

            JLabel rollNumberLabel = new JLabel("Enter Roll Number:");
            rollNumberField = new JTextField(15);
            JButton searchButton = new JButton("Search");

            inputPanel.add(rollNumberLabel);
            inputPanel.add(rollNumberField);
            inputPanel.add(searchButton);

            resultArea = new JTextArea(10, 30);
            resultArea.setEditable(false);
            resultArea.setFont(new Font("Arial", Font.PLAIN, 14));

            JScrollPane scrollPane = new JScrollPane(resultArea);

            add(inputPanel, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);

            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String rollNumber = rollNumberField.getText().trim();
                    fetchStudentDetails(rollNumber);
                }
            });

            pack();
            setLocationRelativeTo(null);

        }

        /**
         * @param rollNumber
         */
        private void createMedicalCertificate(String rollNumber) {
            JFrame medicalCertificateFrame = new JFrame("Medical Certificate");
            medicalCertificateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            medicalCertificateFrame.setLayout(new BorderLayout());

            // Fetch student details
            Connection conn = null;
            PreparedStatement studentDetailsStatement = null;
            PreparedStatement prescriptionStatement = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

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
                    Integer age =  studentDetailsResultSet.getInt("age");
                    String problem = prescriptionResultSet.getString("problem");
                    String timeIn = prescriptionResultSet.getString("time_in");
                    String timeOut = prescriptionResultSet.getString("time_out");
                    String bp = prescriptionResultSet.getString("bp");
                    String temp = prescriptionResultSet.getString("temp");

                    JTextArea medicalCertificateTextArea = new JTextArea();
                    medicalCertificateTextArea.setEditable(false);
                    medicalCertificateTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

                    // Add current date
                    String currentDate = java.time.LocalDate.now().toString();

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
                            "Age: " + age + "\n "+
                            "Roll Number: " + rollNumber + "\n\n" +
                            "Medical Observation Period:\n" +
                            "Time In: " + timeIn + "\n" +
                            "Time Out: " + timeOut + "\n\n" +
                            "Problem: " + problem + "\n\n" + "Blood Pressure: " + bp + "\n" +
                            "Temperature: " + temp + "\n\n"+
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

                    medicalCertificateFrame.add(contentPanel);
                    medicalCertificateFrame.setSize(600, 500); // Set the frame size
                    medicalCertificateFrame.setLocationRelativeTo(null);
                    medicalCertificateFrame.setVisible(true);
                } else {
                    resultArea.setText("No student or prescription found for roll number: " + rollNumber);
                }

                studentDetailsResultSet.close();
                prescriptionResultSet.close();
            } catch (SQLException | ClassNotFoundException se) {
                se.printStackTrace();
            } finally {
                try {
                    if (studentDetailsStatement != null)
                        studentDetailsStatement.close();
                    if (prescriptionStatement != null)
                        prescriptionStatement.close();
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }

        private void fetchStudentDetails(String rollNumber) {
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

                    String output = "Student Details:\n" +
                            "Name: " + name + "\n" +
                            "Age: " + age +"\n" +
                            "Year: " + year + "\n" +
                            "Branch: " + branch + "\n" +
                            "Hostel Name: " + hostelName + "\n" +
                            "Room Number: " + roomNumber + "\n" +
                            "Email: " + email + "\n";

                    resultArea.setText(output);

                    JButton addVisitButton = new JButton("Add Visit");

                    addVisitButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            addAllergies(rollNumber);
                        }
                    });

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(addVisitButton);
                    add(buttonPanel, BorderLayout.SOUTH);
                    revalidate();

                    JButton addPrescriptionButton = new JButton("Add Prescription");

                    addPrescriptionButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            addVisitAndPrescription(rollNumber);
                        }
                    });

                    JPanel prescriptionButtonPanel = new JPanel();
                    prescriptionButtonPanel.add(addPrescriptionButton);
                    add(prescriptionButtonPanel, BorderLayout.SOUTH);
                    revalidate();

                } else {
                    resultArea.setText("No student found with roll number: " + rollNumber);
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException | ClassNotFoundException se) {
                se.printStackTrace();
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

        private void addAllergies(String rollNumber) {
            JFrame allergiesFrame = new JFrame("Add Allergies");
            allergiesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            allergiesFrame.setLayout(new BorderLayout());

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new FlowLayout());

            JLabel allergiesLabel = new JLabel("Enter Allergies:");
            JTextField allergiesField = new JTextField(20);
            JButton addButton = new JButton("Add Allergies");

            inputPanel.add(allergiesLabel);
            inputPanel.add(allergiesField);
            inputPanel.add(addButton);

            allergiesFrame.add(inputPanel, BorderLayout.NORTH);

            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String allergies = allergiesField.getText().trim();
                    addAllergiesToDB(rollNumber, allergies);
                    allergiesFrame.dispose();
                    removeAddVisitButton();
                }
            });

            allergiesFrame.pack();
            allergiesFrame.setLocationRelativeTo(null);
            allergiesFrame.setVisible(true);
        }

        private void removeAddVisitButton() {
            Component[] components = getContentPane().getComponents();
            for (Component component : components) {
                if (component instanceof JPanel) {
                    Component[] subComponents = ((JPanel) component).getComponents();
                    for (Component subComponent : subComponents) {
                        if (subComponent instanceof JButton) {
                            if (((JButton) subComponent).getText().equals("Add Visit")) {
                                ((JPanel) component).remove(subComponent);
                                revalidate();
                                repaint();
                                return;
                            }
                        }
                    }
                }
            }
        }

        private void addAllergiesToDB(String rollNumber, String allergies) {
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

                preparedStatement.close();
                conn.close();
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

        private void addVisitAndPrescription(String rollNumber) {
            JFrame visitPrescriptionFrame = new JFrame(" Prescription");
            visitPrescriptionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            visitPrescriptionFrame.setLayout(new BorderLayout());

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(0, 2, 10, 10));

            JLabel problemLabel = new JLabel("Problem:");
            JTextField problemField = new JTextField();

            JLabel medicinesLabel = new JLabel("Medicines:");
            JTextField medicinesField = new JTextField();

            JLabel dosageLabel = new JLabel("Dosage:");
            JTextField dosageField = new JTextField();

            JLabel timeToTakeLabel = new JLabel("Time to Take Medicine:");
            JTextField timeToTakeField = new JTextField();

            JLabel bpLabel = new JLabel("Blood Pressure:");
            JTextField bpField = new JTextField();

            JLabel tempLabel = new JLabel("Temperature:");
            JTextField tempField = new JTextField();

            JLabel timeInLabel = new JLabel("Time In:");
            JTextField timeInField = new JTextField();

            JLabel timeOutLabel = new JLabel("Time Out:");
            JTextField timeOutField = new JTextField();

            JButton addMedicineButton = new JButton("Add Medicine");
            JButton addButton = new JButton("Add Prescription");
            JButton generatePrescriptionButton = new JButton("Generate Prescription");
            JButton createMedicalCertificateButton = new JButton("Create Medical Certificate"); // Declare the button

            inputPanel.add(generatePrescriptionButton);
            inputPanel.add(createMedicalCertificateButton); // Add the button to the panel
            visitPrescriptionFrame.add(inputPanel, BorderLayout.NORTH);
            visitPrescriptionFrame.setSize(800, 600);
            visitPrescriptionFrame.setLocationRelativeTo(null);
            visitPrescriptionFrame.setVisible(true);
            List<JPanel> medicinePanels = new ArrayList<>();

            generatePrescriptionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    generatePrescriptionDetails(rollNumber);
                }
            });
            createMedicalCertificateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Ensure that rollNumber is not null before calling createMedicalCertificate

                    createMedicalCertificate(rollNumber);

                }
            });

            addMedicineButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JPanel medicinePanel = new JPanel(new GridLayout(0, 2, 5, 5));

                    JTextField medicineField = new JTextField();
                    JTextField dosageField = new JTextField();
                    JTextField timeToTakeField = new JTextField();

                    medicinePanel.add(new JLabel("Medicine:"));
                    medicinePanel.add(medicineField);
                    medicinePanel.add(new JLabel("Dosage:"));
                    medicinePanel.add(dosageField);
                    medicinePanel.add(new JLabel("Time to Take:"));
                    medicinePanel.add(timeToTakeField);

                    medicinePanels.add(medicinePanel);
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridx = 0;
                    gbc.gridy = medicinePanels.size() + 2; // Adjust grid y-position to accommodate new elements
                    gbc.anchor = GridBagConstraints.WEST;
                    inputPanel.add(medicinePanel, gbc); // Use GridBagConstraints for adding to inputPanel

                    visitPrescriptionFrame.pack();
                    inputPanel.add(medicinePanel);
                    visitPrescriptionFrame.pack();
                    visitPrescriptionFrame.revalidate();
                }
            });

            inputPanel.add(problemLabel);
            inputPanel.add(problemField);
            inputPanel.add(bpLabel);
            inputPanel.add(bpField);
            inputPanel.add(tempLabel);
            inputPanel.add(tempField);
            inputPanel.add(timeInLabel);
            inputPanel.add(timeInField);
            inputPanel.add(timeOutLabel);
            inputPanel.add(timeOutField);
            inputPanel.add(addMedicineButton);
            inputPanel.add(addButton);

            // Move the following line to place the Create Medical Certificate button below
            // the Generate Prescription button
            // inputPanel.add(createMedicalCertificateButton);

            inputPanel.add(generatePrescriptionButton);
            inputPanel.add(createMedicalCertificateButton); // Add the Create Medical Certificate button after Generate
            // Prescription button

            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String problem = problemField.getText().trim();
                    String medicines = buildMedicinesString(medicinePanels);
                    String bp = bpField.getText().trim(); // Get Blood Pressure
                    String temp = tempField.getText().trim();
                    String timeIn = timeInField.getText().trim();
                    String timeOut = timeOutField.getText().trim();

                    addPrescriptionToDB(rollNumber, problem, medicines, bp, temp, timeIn, timeOut);
                    // visitPrescriptionFrame.dispose();
                }
            });

            visitPrescriptionFrame.add(inputPanel, BorderLayout.NORTH);
            visitPrescriptionFrame.setSize(800, 600);
            visitPrescriptionFrame.setLocationRelativeTo(null);
            visitPrescriptionFrame.setVisible(true);
        }

        private String buildMedicinesString(List<JPanel> medicinePanels) {
            StringBuilder medicinesBuilder = new StringBuilder();
            for (JPanel medicinePanel : medicinePanels) {
                JTextField medicineField = (JTextField) medicinePanel.getComponent(1);
                JTextField dosageField = (JTextField) medicinePanel.getComponent(3);
                JTextField timeToTakeField = (JTextField) medicinePanel.getComponent(5);

                String medicine = medicineField.getText().trim();
                String dosage = dosageField.getText().trim();
                String timeToTake = timeToTakeField.getText().trim();

                if (!medicine.isEmpty() && !dosage.isEmpty() && !timeToTake.isEmpty()) {
                    medicinesBuilder.append(medicine).append(": Dosage - ").append(dosage)
                            .append(", Time to Take - ").append(timeToTake).append("\n");
                }
            }
            return medicinesBuilder.toString();
        }

        private void addPrescriptionToDB(String rollNumber, String problem, String medicines,
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

                preparedStatement.close();
                conn.close();
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

        private void generatePrescriptionDetails(String rollNumber) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());
            Connection conn = null;
            PreparedStatement preparedStatement = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                String studentDetailsQuery = "SELECT * FROM studentdetails WHERE rollnumber=?";
                preparedStatement = conn.prepareStatement(studentDetailsQuery);
                preparedStatement.setString(1, rollNumber);
                ResultSet studentDetailsResultSet = preparedStatement.executeQuery();

                if (studentDetailsResultSet.next()) {
                    String name = studentDetailsResultSet.getString("name");
                    Integer age = studentDetailsResultSet.getInt("age");

                    String prescriptionQuery = "SELECT * FROM prescription WHERE rollnumber=?";
                    preparedStatement = conn.prepareStatement(prescriptionQuery);
                    preparedStatement.setString(1, rollNumber);
                    ResultSet prescriptionResultSet = preparedStatement.executeQuery();

                    if (prescriptionResultSet.next()) {
                        JFrame prescriptionDetailsFrame = new JFrame("Prescription Details");
                        prescriptionDetailsFrame.setLayout(new BorderLayout());
                        JPanel headingPanel = new JPanel();
                        JLabel headingLabel = new JLabel("<html><b>PRESCRIPTION</b></html>");
                        headingLabel.setFont(new Font("Arial", Font.BOLD, 16));
                        headingPanel.add(headingLabel);
                        JTextArea prescriptionDetailsArea = new JTextArea();
                        prescriptionDetailsArea.setEditable(false);
                        prescriptionDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));


                        String problem = prescriptionResultSet.getString("problem");
                        String medicines = prescriptionResultSet.getString("medicines");
                        String timeIn = prescriptionResultSet.getString("time_in");
                        String timeOut = prescriptionResultSet.getString("time_out");
                        String bp = prescriptionResultSet.getString("bp");
                        String temp = prescriptionResultSet.getString("temp");


                        String output = "Date: " + currentDate + "\n\n" +
                                "Student Details:\n" +
                                "Age: " + age + "\n" +
                                "Name: " + name + "\n" +
                                "Prescription Details:\n" +
                                "Problem: " + problem + "\n" +
                                "Medicines: " + medicines + "\n" +
                                "Time In: " + timeIn + "\n" +
                                "Time Out: " + timeOut + "\n" +
                                "Blood Pressure: " + bp + "\n" + // Include Blood Pressure
                                "Temperature: " + temp + "\n"; // Include Temperature

                        prescriptionDetailsArea.setText(output);
                        prescriptionDetailsFrame.add(headingPanel, BorderLayout.NORTH);
                        prescriptionDetailsFrame.add(new JScrollPane(prescriptionDetailsArea), BorderLayout.CENTER);
                        prescriptionDetailsFrame.setSize(600, 500);
                        prescriptionDetailsFrame.setLocationRelativeTo(null);
                        prescriptionDetailsFrame.setVisible(true);
                        resultArea.setText(output);

                    } else {
                        resultArea.setText("No prescription found for roll number: " + rollNumber);
                    }

                    prescriptionResultSet.close();
                } else {
                    resultArea.setText("No student found with roll number: " + rollNumber);
                }

                studentDetailsResultSet.close();
                preparedStatement.close();
                conn.close();
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

        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new Main().setVisible(true);
                }
            });
        }
    }