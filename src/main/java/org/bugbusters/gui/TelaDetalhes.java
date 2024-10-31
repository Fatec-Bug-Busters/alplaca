package org.bugbusters.gui;

import org.bugbusters.database.entity.Plate;
import org.bugbusters.database.entity.Vehicle;

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

        // Load photo
        char sep = File.separatorChar;
        String imagePath = sep + "src" + sep + "java" + sep + "org" + sep + "bugbusters" + sep + "database" + sep + "images";
        BufferedImage photo = null;
        try {
            photo = ImageIO.read(new File(imagePath + this.plate.getId() + ".jpg"));
            ImageIcon image = new ImageIcon(photo);
            this.lblPhoto.setIcon(image);
            this.lblPhoto.setBounds(0, 0, photo.getWidth(), photo.getHeight());
        } catch (IOException e) {
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
