import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main1 extends JFrame {
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

    private void fetchStudentDetails(String rollNumber) {
        String studentDetails = DatabaseUtils.fetchStudentDetails(rollNumber);
        resultArea.setText(studentDetails);

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
    }

    private void addAllergies(String rollNumber) {
        new AllergiesDialog(rollNumber).setVisible(true);
    }

    private void addVisitAndPrescription(String rollNumber) {
        new PrescriptionDialog(rollNumber).setVisible(true);
    }

    private void createMedicalCertificate(String rollNumber) {
        new MedicalCertificateDialog(rollNumber).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}