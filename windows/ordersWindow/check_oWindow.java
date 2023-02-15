package windows.ordersWindow;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import iLayouts.FlowLayoutApplyer;
import objects.orderItems;
import util.abstractTable;
import util.abstractUpdater;
import windows.main_Window;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class check_oWindow extends abstractUpdater {

    private int order_id;
    private JButton cardButton = new JButton("Card Payment");
    private JButton cashButton = new JButton("Cash Payment");
    private JButton backButton = new JButton("Back");
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public check_oWindow(abstractUpdater previousWindow, int order_id) {
        super(previousWindow, new FlowLayoutApplyer(theFrame));
        this.order_id = order_id;
        theFrame.setTitle("Check-out");
    }

    @Override
    public void addComponents() {
        ArrayList<orderItems> order = theManagerDB.getOrderItems(order_id);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Order ID: " + order_id);
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        new abstractTable(panel, order_id, order);
        JTextArea textArea = new JTextArea("SUBTOTAL: " + df.format(getSubtotal()) + "€ \n" +
                                            "TAX: " + df.format(getTax()) + "€ \n" +
                                            "TOTAL: " + df.format(getTotal()) + "€ \n");
        textArea.setEditable(false);
        panel.add(textArea, BorderLayout.SOUTH);
        theFrame.add(panel);
        
        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(300, 550));
        panel2.setLayout(new GridLayout(3, 1));
        panel2.add(cardButton);
        panel2.add(cashButton);
        panel2.add(backButton);
        theFrame.add(panel2);
    }

    private float getSubtotal() {
        ArrayList<orderItems> order = theManagerDB.getOrderItems(order_id);
        float total = 0;
        for (orderItems product : order) {
            total = total + (product.getProduct().getPrice() * product.getQuantity());
        }
        total = Float.parseFloat(df.format(total));
        return total;
    }

    private float getTax() {
        float tax = getSubtotal() / 10;
        tax = Float.parseFloat(df.format(tax));
        return tax;
    }

    private float getTotal() {
        return getSubtotal() + getTax();
    }

    @Override
    public void addActionListeners() {
        abstractUpdater temp = this;
        int table_id = theManagerDB.getTableID(order_id);
        
        cardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Object[] options = {"Ok", "Cancel"};
                int n = JOptionPane.showOptionDialog(theFrame, "Insert Card", "Card Payment",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                
                if (n == 0) {

                    Object[] options2 = {"Yes", "No"};
                    int n2 = JOptionPane.showOptionDialog(theFrame, "Do you want a receipt?", null,
			            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2, null);
                    
                    if (n2 == 0) {
                        theManagerDB.checkOutOrder(order_id, getTotal(), getTax(), getSubtotal(), false);
                        if (table_id != -1) theManagerDB.makeTableEmpty(table_id);
                        receipt_oWindow tempWind = new receipt_oWindow(temp, order_id, getSubtotal(), getTax(), getTotal());
                        tempWind.updateToThisMenu();
                    } else if (n2 == 1) {
                        theManagerDB.checkOutOrder(order_id, getTotal(), getTax(), getSubtotal(), false);
                        if (table_id != -1) theManagerDB.makeTableEmpty(table_id);
                        new main_Window();
                    } 
                }
            }
        });

        cashButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cash_oWindow tempWind = new cash_oWindow(temp, order_id);
                tempWind.updateToThisMenu();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateToPreviousMenu();
            }
        });
    }
}
