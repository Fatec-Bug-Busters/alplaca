package org.bugbusters.gui;

import org.bugbusters.database.entity.Plate;
import org.bugbusters.database.hibernate.HibernateService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


public class TelaLista {
    private JFrame mainFrame;
    private JFrame prevScreen;
    private JPanel contentPane;
    private JTable tablePlacas;
    private JPanel headerPanel;
    private JButton placasButton;
    private JButton inteligenciaButton;
    private JLabel logoLabel;
    public List plateList;

    public TelaLista(JFrame prevScreen) {
        mainFrame = new JFrame("Alplaca");

        // Hide previous screen
        this.prevScreen = prevScreen;
        prevScreen.setVisible(false);

        placasButton.setEnabled(false);
        headerPanel.setBackground(Color.decode("#cccccc"));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));


        createTable();
        inteligenciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        loadLogo();
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
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Ver");
            button.addActionListener(e -> {
                // Plate plate = plateList.get(selectedRow);
                Plate plate = (Plate) plateList.stream().toArray()[selectedRow];
                TelaDetalhes telaDetalhes = new TelaDetalhes(plate, mainFrame);

                Rectangle windowSize = mainFrame.getBounds();
                telaDetalhes.createAndShowGUI(windowSize);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.selectedRow = row;
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

    protected void loadLogo() {
        char sep = File.separatorChar;
        String imagePath = sep  + "images"  + sep + "logo.png";
        try {
            URL imageURL = getClass().getResource(imagePath);
            ImageIcon icon = new ImageIcon(imageURL);

            int width = 78;
            int height = icon.getIconHeight() * width / icon.getIconWidth();

            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            this.logoLabel.setIcon(scaledIcon);
            this.logoLabel.setBounds(0, 0, width, height);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.err.println("Logo não encontrado.");
            //throw e;
        }
    }
}
