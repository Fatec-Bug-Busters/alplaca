package org.bugbusters.gui;

import org.bugbusters.database.entity.Plate;
import org.bugbusters.database.entity.Vehicle;
import org.bugbusters.database.hibernate.HibernateService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TelaDetalhes {
    private Plate plate;
    private JFrame mainFrame;
    private JFrame prevScreen;
    private JPanel contentPane;
    private JButton voltarButton;
    private JPanel header;
    private JPanel panelPlacas;
    private JLabel lblPlacaIdentificacao;
    private JLabel lblPlacaLocal;
    private JLabel lblPlacaCor;
    private JPanel panelVeiculos;
    private JLabel lblVeiculoCor;
    private JLabel lblVeiculoTipo;
    private JLabel lblVeiculoCategoria;
    private JPanel panelImage;
    private JLabel lblPhoto;

    public TelaDetalhes(Plate plate, JFrame prevScreen) {
        // HibernateService.openSession();
        // this.plate = HibernateService.findById(plate.getId(), Plate.class);
        this.plate = plate;
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

        loadPlate();
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
        char sep = File.separatorChar;
        String imagePath = sep + "images" + sep + this.plate.getId() + ".jpeg";

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
}
