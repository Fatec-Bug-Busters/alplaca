package org.bugbusters.gui;

import org.bugbusters.database.entity.Category;
import org.bugbusters.database.entity.Plate;
import org.bugbusters.database.entity.Vehicle;
import org.bugbusters.database.hibernate.HibernateService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TelaDetalhes {
    private Plate plate;
    private JFrame mainFrame;
    private JFrame prevScreen;
    private JFrame prevPrevScreen;
    private JPanel contentPane;
    private JPanel header;
    private JPanel panelPlacas;
    private JPanel panelVeiculos;
    private JLabel lblPhoto;
    private JPanel headerPanel;
    private JButton placasButton;
    private JButton inteligenciaButton;
    private JLabel logoLabel;
    private JButton voltarButton;
    private JButton atualizarButton;
    private JButton deletarButton;
    private JTextField textFieldIdentificacao;
    private JTextField textFieldLocal;
    private JTextField textFieldCorPlaca;
    private JTextField textFieldCorVeiculo;
    private JTextField textFieldCategoria;
    private JButton editarButton;

    public TelaDetalhes(Plate plate, JFrame prevScreen, JFrame prevPrevScreen) {
        // HibernateService.openSession();
        // this.plate = HibernateService.findById(plate.getId(), Plate.class);
        this.plate = plate;
        mainFrame = new JFrame("Alplaca");
        Image image = Toolkit.getDefaultToolkit().getImage("src/main/resources/images/logo.png");
        mainFrame.setIconImage(image);

        // Hide previous screen
        this.prevScreen = prevScreen;
        this.prevPrevScreen = prevPrevScreen;
        prevScreen.setVisible(false);

        // header
        headerPanel.setBackground(Color.decode("#cccccc"));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        placasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        inteligenciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackTwice();
            }
        });

        loadPlate();
        loadLogo();
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        textFieldIdentificacao.setEnabled(false);
        textFieldLocal.setEnabled(false);
        textFieldCorPlaca.setEnabled(false);
        textFieldCorVeiculo.setEnabled(false);
        textFieldCategoria.setEnabled(false);

        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldIdentificacao.setEnabled(true);
                textFieldLocal.setEnabled(true);
                textFieldCorPlaca.setEnabled(true);
                textFieldCorVeiculo.setEnabled(true);
                textFieldCategoria.setEnabled(true);
            }
        });
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HibernateService.openSession();
                Category categoryUpdate = HibernateService.findByConditionObject("categories",
                    "name = '"+textFieldCategoria.getText()+"'",
                    Category.class);

                if (categoryUpdate == null)
                {
                    Category categoryInsert = new Category();
                    categoryInsert.setName(textFieldCategoria.getText());
                    HibernateService.insertValue(categoryInsert);

                    categoryUpdate = HibernateService.findByConditionObject("categories",
                        "name = '"+textFieldCorVeiculo.getText()+"'",
                        Category.class);
                }

                Vehicle vehicleUpdate = new Vehicle();
                vehicleUpdate.setId(plate.getVehicle().getId());
                vehicleUpdate.setColor(textFieldCorVeiculo.getText());
                vehicleUpdate.setCategory(categoryUpdate);


                System.out.println("Placa Criada");
                Plate plateUpdate = new Plate();
                plateUpdate.setId(plate.getId());
                plateUpdate.setVehicle(vehicleUpdate);
                plateUpdate.setIdentification(textFieldIdentificacao.getText());
                plateUpdate.setLocation(textFieldLocal.getText());
                plateUpdate.setColor(textFieldCorPlaca.getText());

                System.out.println(categoryUpdate);
                System.out.println(vehicleUpdate);
                System.out.println(plateUpdate);

                HibernateService.updateValue(categoryUpdate);
                HibernateService.updateValue(vehicleUpdate);
                HibernateService.updateValue(plateUpdate);
                HibernateService.closeSession();


            }
        });
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

        // Load the plate photo
        loadPlateImage();
    }

    /**
     * Destroy this screen and make the previous screen visible
     */
    public void goBack() {
        mainFrame.dispose();
        prevScreen.dispose();
        prevPrevScreen.dispose();
        TelaLista telaLista = new TelaLista(mainFrame);
        Rectangle windowSize = this.mainFrame.getBounds();
        telaLista.createAndShowGUI(windowSize);

    }

    public void goBackTwice() {
        mainFrame.dispose();
        prevPrevScreen.setVisible(true);
    }


    /**
     * Load plate data into the page
     */
    public void loadPlate() {
        this.textFieldIdentificacao.setText(this.plate.getIdentification());
        this.textFieldLocal.setText(this.plate.getLocation());
        this.textFieldCorPlaca.setText(this.plate.getColor());

        Vehicle vehicle = this.plate.getVehicle();
        this.textFieldCorVeiculo.setText(vehicle.getColor());
        this.textFieldCategoria.setText(vehicle.getCategory().getName());


    }

    public void loadPlateImage() {
        String filename = "src/main/resources/images/" + this.plate.getId() + ".jpeg";

        int windowWidth = mainFrame.getWidth();
        int windowHeight = mainFrame.getHeight();

        try {
            ImageIcon icon = new ImageIcon(filename);

            int width = 360;
            int height = icon.getIconHeight() * width / icon.getIconWidth();

            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            this.lblPhoto.setIcon(scaledIcon);
            this.lblPhoto.setBounds(0, 0, width, height);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.err.println("Erro ao carregar a foto da placa: " + filename);
        }
    }

    public void setPlate(Plate plate) {
        this.plate = plate;
    }

    public Plate getPlate() {
        return plate;
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
