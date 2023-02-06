package windows.tablesWindow;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import iLayouts.FlowLayoutApplyer;
import windows.main_Window;
import windows.ordersWindow.assist_edit_oWindow;
import windows.ordersWindow.edit_oWindow;
import util.abstractUpdater;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;

public class table_tWindow extends abstractUpdater {

    private JLabel statusLabel = new JLabel();
    private JButton viewOrder = new JButton("View Order");
    private JButton backButton = new JButton("Back");
    private int table_id;
    private String status;

    public table_tWindow(abstractUpdater previousWindow, int table_id) {
        super(previousWindow, new FlowLayoutApplyer(theFrame));
        this.table_id = table_id;
        this.getStatus();
    }

    public String getStatus() {
        int is_empty = theManagerDB.getTableStatus(table_id);
        if(is_empty == 1) status = "EMPTY";
        if(is_empty == 0) status = "OCCUPIED";
        statusLabel = new JLabel("Table status: " + status);
        return status;
    }

    @Override
    public void addComponents() {
        theFrame.setTitle("Tables menu");
        theFrame.add(statusLabel);
        if(getStatus() == "OCCUPIED") theFrame.add(viewOrder);
        theFrame.add(backButton);
    }

    @Override
    public void addActionListeners() {
        abstractUpdater temp = this;

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new main_tWindow(new main_Window()).updateToThisMenu();
            }
        });

        viewOrder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewOrder_tWindow tempWind = new viewOrder_tWindow(temp, theManagerDB.getOrderID(table_id));
                tempWind.updateToThisMenu();
            }
        });
    }
}
