package UI;

import game.components.Component;
import game.components.Conector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ConnectorPlacementDialog extends JDialog {

    private JComboBox<String> fromComboBox;
    private JComboBox<String> toComboBox;
    private boolean confirmed = false;

    private Component fromComponent;
    private Component toComponent;

    public ConnectorPlacementDialog(List<Component> availableComponents) {
        setTitle("Place Connector");
        setModal(true);
        setSize(400, 200);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel selectionPanel = new JPanel(new GridLayout(3, 2));

        // Create arrays of component names
        String[] componentNames = availableComponents.stream().map(Component::getName).toArray(String[]::new);

        selectionPanel.add(new JLabel("Select first component:"));
        fromComboBox = new JComboBox<>(componentNames);
        selectionPanel.add(fromComboBox);

        selectionPanel.add(new JLabel("Select second component:"));
        toComboBox = new JComboBox<>(componentNames);
        selectionPanel.add(toComboBox);

        add(selectionPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("Place Connector");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected components
                int fromIndex = fromComboBox.getSelectedIndex();
                int toIndex = toComboBox.getSelectedIndex();
                if (fromIndex == toIndex) {
                    JOptionPane.showMessageDialog(ConnectorPlacementDialog.this, "You must select two different components.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                fromComponent = availableComponents.get(fromIndex);
                toComponent = availableComponents.get(toIndex);

                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Component getFromComponent() {
        return fromComponent;
    }

    public Component getToComponent() {
        return toComponent;
    }
}
