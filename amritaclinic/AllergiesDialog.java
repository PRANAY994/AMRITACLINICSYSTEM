import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AllergiesDialog extends JDialog {
    public AllergiesDialog(String rollNumber) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle("Add Allergies");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel allergiesLabel = new JLabel("Enter Allergies:");
        JTextField allergiesField = new JTextField(20);
        JButton addButton = new JButton("Add Allergies");

        inputPanel.add(allergiesLabel);
        inputPanel.add(allergiesField);
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String allergies = allergiesField.getText().trim();
                DatabaseUtils.addAllergiesToDB(rollNumber, allergies);
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }
}