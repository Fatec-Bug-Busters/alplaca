package org.bugbusters.gui;

import org.bugbusters.database.entity.Plate;
import org.bugbusters.database.hibernate.HibernateService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


public class TelaLista {
    private JFrame mainFrame;
    private JFrame prevScreen;
    private JPanel contentPane;
    private JButton voltarButton;
    private JTable tablePlacas;
    public java.util.List plateList;

    public TelaLista(JFrame prevScreen) {
        mainFrame = new JFrame("Alplaca");

        // Hide previous screen
        this.prevScreen = prevScreen;
        prevScreen.setVisible(false);


        /**
         * Back button
         */
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        createTable();
    }

    public void createTable() {
        HibernateService.openSession();
        this.plateList = HibernateService.findByCondition("plates","id > 0",Plate.class);

        Object[][] data = new Object[plateList.size()][3];
        for (int i = 0; i < plateList.size(); i++) {
            Plate plate = (Plate) plateList.get(i);
            data[i][0] = plate.getId();
            data[i][1] = plate.getIdentification();
            data[i][2] = "Detalhes";
        }
        HibernateService.closeSession();

        tablePlacas.setModel(new DefaultTableModel(
            data,
            new String[]{"ID", "Placa", "Detalhes"}
        ));

        tablePlacas.getColumn("Detalhes").setCellRenderer((TableCellRenderer) new ButtonRenderer());
        tablePlacas.getColumn("Detalhes").setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Ver");
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private  int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Ver");
            button.addActionListener(e -> {
                System.out.println(e);
                // goBack();
                // TODO: Load plate ...

             Plate plate = (Plate) plateList.get(selectedRow);
             TelaDetalhes telaDetalhes = new TelaDetalhes(plate, mainFrame);

                Rectangle windowSize = mainFrame.getBounds();
                telaDetalhes.createAndShowGUI(windowSize);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            clicked = false;
            return "Ver";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public void createAndShowGUI(Rectangle windowSize) {
        mainFrame.setContentPane(contentPane);
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // mainFrame.pack();
        mainFrame.setSize(windowSize.width, windowSize.height);
        mainFrame.setVisible(true);

        /**
         * Override the close window operation
         */
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                goBack();
            }
        });
    }

    /**
     * Destroy this screen and make the previous screen visible
     */
    public void goBack() {
        mainFrame.dispose();
        prevScreen.setVisible(true);
    }
}
