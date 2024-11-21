package org.bugbusters.gui;

import org.bugbusters.database.entity.Plate;
import org.bugbusters.database.hibernate.HibernateService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.util.Arrays;
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
    private JButton voltarButton;
    private JButton pesquisarButton;
    private JComboBox pesquisarDropdown;
    private JTextField pesquisarTextField;
    public List plateList;

    public TelaLista(JFrame prevScreen) {
        mainFrame = new JFrame("Alplaca");
        Image image = Toolkit.getDefaultToolkit().getImage("src/main/resources/images/logo.png");
        mainFrame.setIconImage(image);

        // Hide previous screen
        this.prevScreen = prevScreen;
        prevScreen.setVisible(false);

        placasButton.setEnabled(false);
        headerPanel.setBackground(Color.decode("#cccccc"));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        List<String> options = Arrays.asList("Localidade","Identificação da Placa","Cor da placa","Cor da placa","Cor do veículo","Categoria do veículo");
        DefaultComboBoxModel<String> modelPesquisar = new DefaultComboBoxModel<>(options.toArray(new String[0]));
        pesquisarDropdown.setModel(modelPesquisar);


        createTable("plates","id > 0");

        inteligenciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        loadLogo();

        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String option = pesquisarDropdown.getSelectedItem().toString();
                switch (option) {
                    case "Localidade":
                        createTable("plates","location LIKE '%"+pesquisarTextField.getText()+"%'");
                        break;
                    case "Identificação da Placa":
                        createTable("plates","identification LIKE '%"+pesquisarTextField.getText()+"%'");
                        break;
                    case "Cor da placa":
                        createTable("plates","color LIKE '%"+pesquisarTextField.getText()+"%'");
                        break;
                    case "Cor do veículo":
                        createTable("SELECT p.id, p.color, p.identification, p.location, p.id_vehicle FROM plates p JOIN vehicles v ON p.id_vehicle = v.id WHERE v.color LIKE '%"+pesquisarTextField.getText()+"%'");
                        break;
                    default:
                        createTable("SELECT p.id, p.color, p.identification, p.location, p.id_vehicle FROM plates p JOIN vehicles v ON p.id_vehicle = v.id JOIN categories c ON v.id_category = c.id WHERE c.name LIKE '%"+pesquisarTextField.getText()+"%'");
                }
            }
        });
    }

    public void createTable(String nameTable, String condition) {
        HibernateService.openSession();
        this.plateList = HibernateService.findByCondition(nameTable,condition,Plate.class);

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

    public void createTable(String sql) {
        HibernateService.openSession();
        this.plateList = HibernateService.findByCondition(sql,Plate.class);

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
                TelaDetalhes telaDetalhes = new TelaDetalhes(plate, mainFrame, prevScreen);

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
        mainFrame.setLocationRelativeTo(null);

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
        prevScreen.dispose();
        AlplacaScreen alplacaScreen = new AlplacaScreen();
        Rectangle windowSize = this.mainFrame.getBounds();
        alplacaScreen.createAndShowGUI(windowSize);

    }

    protected void loadLogo() {
        String filename = "src/main/resources/images/logo.png";
        try {
            ImageIcon icon = new ImageIcon(filename);

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
