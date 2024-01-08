package Server.Interface;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

/**
 * Build Utilities Edge Hill Box Server Version 1
 * This class is to reduce code bloat in the panel creation classes
 * it houses custom Jcomponent building methods
 * @author James Martland 24233781
 */
public class BuildUtilitiesServerV1 extends InterfaceController {
    protected JPanel panel = new JPanel(); // this panel is for

    /**
     * This method returns a panel
     * @return
     */
    public JPanel getPanel() {
        return this.panel;
    }

    /**
     * This method is to add a button to a panel
     * @param l
     * @param toAddTo
     * @param button
     * @param x
     * @param y
     * @param text
     * @param sizeX
     * @param sizeY
     * @param tooltip
     */
    public void addButton(ActionListener l, JPanel toAddTo,
                          JButton button,
                          int x, int y,
                          String text, int sizeX,
                          int sizeY, String tooltip) {
        button.setLocation(x, y);
        button.setSize(sizeX, sizeY);

        button.setOpaque(true);
        button.setVisible(true);
        if (text.equals(null) == true) {
            button.setText("");
        } else {
            button.setText(text);
        }
        button.addActionListener(l);
        button.setToolTipText(tooltip);
        System.out.println("\tButton has been created");
        toAddTo.add(button);
    }

    /**
     * This method adds a label to the panel
     * @param label
     * @param panel
     * @param x
     * @param y
     * @param text
     * @param sizeX
     * @param sizeY
     */
    public void addLabel(JLabel label, JPanel panel,
                         int x, int y, String text, int sizeX, int sizeY) {
        label.setLocation(x, y);
        label.setSize(sizeX, sizeY);
        label.setOpaque(true);
        label.setText(text);
        panel.add(label);
    }

    /**
     * This method adds a text field to the panel
     * @param txtfield
     * @param panel
     * @param x
     * @param y
     * @param sizeX
     * @param sizeY
     */
    public void addTextField(JTextField txtfield, JPanel panel,
                             int x, int y, int sizeX, int sizeY) {
        txtfield.setLocation(x, y);
        txtfield.setSize(sizeX, sizeY);
        panel.add(txtfield);
    }

    /**
     * This method adds a password field to the panel
     * @param passfield
     * @param panel
     * @param x
     * @param y
     * @param sizeX
     * @param sizeY
     */
    public void addPasswordField(JPasswordField passfield, JPanel panel, int x, int y, int sizeX, int sizeY ) {
        passfield.setLocation(x, y);
        passfield.setSize(sizeX, sizeY);
        panel.add(passfield);
    }

    /**
     * This method adds attributes to a combobox, the combobox is then added to the panel
     * @param combo
     * @param panel
     * @param x
     * @param y
     * @param sizeX
     * @param sizeY
     */
    public void addComboBox(JComboBox combo, JPanel panel, int x, int y, int sizeX, int sizeY) {
        combo.setLocation(x, y);
        combo.setSize(sizeX, sizeY);
        combo.setEditable(false);
        combo.setOpaque(true);
        panel.add(combo);
    }

    public boolean hasIllegalChars(String input) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        return p.matcher( input ).find();
    }
}
