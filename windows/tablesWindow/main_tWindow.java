package windows.tablesWindow;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import iLayouts.GridLayoutApplyer;
import util.abstractUpdater;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class main_tWindow extends abstractUpdater {

    private ArrayList<JButton> buttons = new ArrayList<>();
    private JButton backButton = new JButton("Back");
    private static int numberTables = theManagerDB.getAmountOfTables();

    public main_tWindow(abstractUpdater previousWindow) {
        super(previousWindow, new GridLayoutApplyer(theFrame, numberTables+1));
    }

    @Override
    public void addComponents() {
        theFrame.setTitle("Tables menu");
        // Adds a button for every table
        for (int i = 0; i < numberTables; i++) {
            JButton button = new JButton("Table " + (i+1));
            theFrame.add(button);
            buttons.add(button);
        }
        // Back button
        theFrame.add(backButton);
    }

    @Override
    public void addActionListeners() {
        abstractUpdater temp = this;

        for (int i=0; i < numberTables; i++) {
            int table_id = i+1;
            buttons.get(i).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    table_tWindow tempWind = new table_tWindow(temp, table_id);
                    tempWind.updateToThisMenu();
                }
            });
        }

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateToPreviousMenu();
            }
        });
    }
}
