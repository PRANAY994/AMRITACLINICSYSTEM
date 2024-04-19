import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrescriptionDialog extends JFrame {
    public PrescriptionDialog(String rollNumber) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

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
        JButton createMedicalCertificateButton = new JButton("Create Medical Certificate");

        inputPanel.add(generatePrescriptionButton);
        inputPanel.add(createMedicalCertificateButton);
        add(inputPanel, BorderLayout.NORTH);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

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
                new MedicalCertificateDialog(rollNumber).setVisible(true);
            }
        });

        addMedicineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMedicinePanel(inputPanel, medicinePanels);
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

        inputPanel.add(generatePrescriptionButton);
        inputPanel.add(createMedicalCertificateButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String problem = problemField.getText().trim();
                String medicines = buildMedicinesString(medicinePanels);
                String bp = bpField.getText().trim();
                String temp = tempField.getText().trim();
                String timeIn = timeInField.getText().trim();
                String timeOut = timeOutField.getText().trim();

                DatabaseUtils.addPrescriptionToDB(rollNumber, problem, medicines, bp, temp, timeIn, timeOut);
            }
        });
    }

    private void addMedicinePanel(JPanel inputPanel, List<JPanel> medicinePanels) {
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
        gbc.gridy = medicinePanels.size() + 2;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(medicinePanel, gbc);

        pack();
        inputPanel.add(medicinePanel);
        pack();
        revalidate();
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

    private void generatePrescriptionDetails(String rollNumber) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());

        String studentDetails = DatabaseUtils.fetchStudentDetails(rollNumber);
        if (!studentDetails.startsWith("No student")) {
            String[] lines = studentDetails.split("\n");
            String name = lines[1].substring(6);
            Integer age = Integer.parseInt(lines[2].substring(6));

            String prescriptionDetails = DatabaseUtils.fetchPrescriptionDetails(rollNumber);
            if (!prescriptionDetails.startsWith("No prescription")) {
                JFrame prescriptionDetailsFrame = new JFrame("Prescription Details");
                prescriptionDetailsFrame.setLayout(new BorderLayout());

                JPanel headingPanel = new JPanel();
                JLabel headingLabel = new JLabel("<html><b>PRESCRIPTION</b></html>");
                headingLabel.setFont(new Font("Arial", Font.BOLD, 16));
                headingPanel.add(headingLabel);

                JTextArea prescriptionDetailsArea = new JTextArea();
                prescriptionDetailsArea.setEditable(false);
                prescriptionDetailsArea.setText(
                        "Patient Name: " + name + "\n" +
                        "Age: " + age + "\n" +
                        "Date: " + currentDate + "\n\n" +
                        prescriptionDetails
                );

                JScrollPane scrollPane = new JScrollPane(prescriptionDetailsArea);
                prescriptionDetailsFrame.add(headingPanel, BorderLayout.NORTH);
                prescriptionDetailsFrame.add(scrollPane, BorderLayout.CENTER);
                prescriptionDetailsFrame.setSize(600, 400);
                prescriptionDetailsFrame.setLocationRelativeTo(this);
                prescriptionDetailsFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No prescription found for the given roll number.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No student found for the given roll number.");
        }
    }
}