package org.bugbusters.gui;

import org.bugbusters.database.entity.Plate;
import org.bugbusters.database.entity.Vehicle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class TelaDetalhes {
    private Plate plate;
    private JFrame mainFrame;
    private JFrame prevScreen;
    private JPanel contentPane;
    private JPanel header;
    private JPanel panelPlacas;
    private JLabel lblPlacaIdentificacao;
    private JLabel lblPlacaLocal;
    private JLabel lblPlacaCor;
    private JPanel panelVeiculos;
    private JLabel lblVeiculoCor;
    private JLabel lblVeiculoTipo;
    private JLabel lblVeiculoCategoria;
    private JLabel lblPhoto;
    private JPanel headerPanel;
    private JButton placasButton;
    private JButton inteligenciaButton;
    private JLabel logoLabel;
    private JButton voltarButton;

    public TelaDetalhes(Plate plate, JFrame prevScreen) {
        // HibernateService.openSession();
        // this.plate = HibernateService.findById(plate.getId(), Plate.class);
        this.plate = plate;
        mainFrame = new JFrame("Alplaca");

        // Hide previous screen
        this.prevScreen = prevScreen;
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
                goBack();
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

        // Load the plate photo
        loadPlateImage();
    }

    /**
     * Destroy this screen and make the previous screen visible
     */
    public void goBack() {
        mainFrame.dispose();
        prevScreen.setVisible(true);
    }

    /**
     * Load plate data into the page
     */
    public void loadPlate() {
        this.lblPlacaIdentificacao.setText(this.plate.getIdentification());
        this.lblPlacaLocal.setText(this.plate.getLocation());
        this.lblPlacaCor.setText(this.plate.getColor());

        Vehicle vehicle = this.plate.getVehicle();
        this.lblVeiculoCor.setText(vehicle.getColor());
        this.lblVeiculoCategoria.setText(vehicle.getCategory().getName());
    }

    public void loadPlateImage() {
        String imagePath =  "/images/" + this.plate.getId() + ".jpeg";

        int windowWidth = mainFrame.getWidth();
        int windowHeight = mainFrame.getHeight();

        try {
            URL imageURL = getClass().getResource(imagePath);
            ImageIcon icon = new ImageIcon(imageURL);

//            int width = Math.min(icon.getIconWidth(), windowWidth);
//            System.out.println(icon.getIconWidth()+ " "+ windowWidth + " " + width);
//            int height = Math.min(icon.getIconHeight(), windowHeight);
//            System.out.println(icon.getIconHeight()+ " "+ windowHeight + " " + height);
            int width = 360;
            int height = icon.getIconHeight() * width / icon.getIconWidth();

            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            this.lblPhoto.setIcon(scaledIcon);
            this.lblPhoto.setBounds(0, 0, width, height);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.err.println("Erro ao carregar a foto da placa: " + imagePath);
        }
    }

    public void setPlate(Plate plate) {
        this.plate = plate;
    }

    public Plate getPlate() {
        return plate;
    }

    protected void loadLogo() {
        String imagePath = "/images/logo.png";
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
