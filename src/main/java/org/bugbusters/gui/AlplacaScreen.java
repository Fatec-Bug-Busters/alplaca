package org.bugbusters.gui;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import org.bugbusters.database.ImageSave;
import org.bugbusters.database.entity.Category;
import org.bugbusters.database.entity.Plate;
import org.bugbusters.database.entity.Vehicle;
import org.bugbusters.database.hibernate.HibernateService;
import org.bugbusters.ollama.ModelList;

import org.bugbusters.ollama.Models;
import org.bugbusters.ollama.Ollama;
import org.bugbusters.ollama.OllamaRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;


public class AlplacaScreen {
    JFrame mainFrame;
    private JTextArea textResultLoc;
    private JButton openButton;
    private JButton sendButton;
    private JLabel textFile;
    private JPanel contentPane;
    private JComboBox modelDropdown;
    private JButton addModelButton;
    private JComboBox dropdownOpt;
    private JTextArea textResultIdentPla;
    private JButton enviarBDButton;
    private JButton detalheButton;
    private JTextArea textResultCorPla;
    private JTextArea textResultCorVei;
    private JTextArea textResultCateVei;
    private String fileName;
    private String filePath;

    protected OllamaAPI ollamaAPI;
    protected OllamaRequest request;
    protected Models models;
    protected ModelList modelList;


    public AlplacaScreen() {
        mainFrame = new JFrame("Alplaca");

        //JTextArea Line Break
        textResultLoc.setLineWrap(true);
        textResultLoc.setWrapStyleWord(true);
        textResultIdentPla.setLineWrap(true);
        textResultIdentPla.setWrapStyleWord(true);

        OllamaAPI ollamaAPI = Ollama.getInstance();
        OllamaRequest request = new OllamaRequest(ollamaAPI);
        models = new Models(ollamaAPI);
        modelList = new ModelList();

        // Add models to dropdown
        displaySupportedModels();
        hideInstallModelTrigger();

        /**
         * Open dialog to upload image
         */
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(contentPane);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                    fileName = selectedFile.getName();

                    textFile.setText(fileName);
                } else if (returnValue == JFileChooser.CANCEL_OPTION) {
                    textFile.setText("Nenhum arquivo selecionado");

                }
            }
        });

        ArrayList<String> showInfo = new ArrayList<String>() {{
            add("Localidade");
            add("Identificação da placa");
            add("Cor da placa");
            add("Cor do veículo");
            add("Categoria do Veículo");
        }};
        dropdownOpt.setModel(new DefaultComboBoxModel(showInfo.toArray()));

        dropdownOpt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) dropdownOpt.getSelectedItem();
            }
        });

        /**
         * Send request to the API
         */
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                String modelName = modelList.getModelName(modelDropdown.getSelectedItem().toString());
                request.setModel(modelName);


                OllamaResult result;
                try {
                    File[] images = {
                        new File(filePath)
                    };
                    String selectedItem = (String) dropdownOpt.getSelectedItem();
                    if (selectedItem.equals("Localidade")) {
                        result = request.syncWithImageFilesRequest(
                            "Identify the location (city or state) linked to this license plate. Return only the location without any additional text",
                            images
                        );
                        textResultLoc.setText(result.getResponse());
                    } else if (selectedItem.equals("Identificação da placa")) {
                        result = request.syncWithImageFilesRequest(
                            "This car plate model is: 3 letters - 1 number - 1 letter - 2 numbers or 3 letters - 4 numbers. Rerurn only the numbers and letters of this plate",
                            images
                        );
                        textResultIdentPla.setText(result.getResponse());
                    } else if (selectedItem.equals("Cor da placa")) {
                        result = request.syncWithImageFilesRequest(
                            "What color is the letters in the license plate? return only the color without any additional text",
                            images
                        );
                        textResultCorPla.setText(result.getResponse());
                    } else if (selectedItem.equals("Cor do veículo")) {
                        result = request.syncWithImageFilesRequest(
                            "What color is the vehicle? return only the color without any additional text",
                            images
                        );
                        textResultCorVei.setText(result.getResponse());
                    } else if (selectedItem.equals("Categoria do Veículo")) {
                        result = request.syncWithImageFilesRequest(
                            "What type of vehicle is this (e.g., car, truck, motorcycle) based on the image? Return only the type without  any additional text. If not possible to identify the type of vehicle, retrurn: Não Identificado",
                            images
                        );
                        textResultCateVei.setText(result.getResponse());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Install model listener
         */
        addModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String modelName = modelList.getModelName(modelDropdown.getSelectedItem().toString());

                try {
                    if (models.isInstalled(modelName)) {
                        JOptionPane.showMessageDialog(null, "O modelo já está instalado.");
                    } else {
                        // Show installing message
                        showInstallingMessage(modelName);

                        // Install model
                        models.installModel(modelName);

                        // Show installation complete message
                        showInstallationCompleteMessage(modelName);

                        // Show installation complete message
                        showInstallationCompleteMessage(modelName);

                        hideInstallModelTrigger();
                        enableSendRequestButton();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao verificar ou baixar o modelo.");
                }
            }
        });


        /**
         * Model Dropdown changed
         */
        modelDropdown.addActionListener(new ActionListener() {
            /**
             * Check whether the chosen model is installed, and suggest installation, in case it is not
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String modelName = modelList.getModelName(modelDropdown.getSelectedItem().toString());

                try {
                    if(models.isInstalled(modelName)) {
                        enableSendRequestButton();
                        hideInstallModelTrigger();
                    } else {
                        // Display the button to suggest installation
                        displayInstallModelTrigger();
                        // Deactivate send request button
                        disableSendRequestButton();
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        });
        detalheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TelaLista telaLista = new TelaLista(mainFrame);
                telaLista.createAndShowGUI(new Rectangle(800, 600));

                // TODO: Load plate ...
//                TelaDetalhes telaDetalhes = new TelaDetalhes(plate, mainFrame);
//
//                Rectangle windowSize = mainFrame.getBounds();
//                telaDetalhes.createAndShowGUI(windowSize);
            }
        });
        enviarBDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean filledField = (textResultCateVei.getText().equals("") || textResultIdentPla.getText().equals("")
                    || (filePath == null));


                if (filledField){
                    JOptionPane.showMessageDialog(contentPane,"Há campos a serem preenchidos");
                }
                else {
                HibernateService.openSession();

                Vehicle vehicle = new Vehicle();
                vehicle.setCategory(HibernateService.findByConditionObject("categories","name='"+
                    textResultCateVei.getText()+"'", Category.class));
                vehicle.setColor(textResultCorVei.getText());

                HibernateService.insertValue(vehicle);

                Plate plate = new Plate();
                plate.setLocation(textResultLoc.getText());
                plate.setIdentification(textResultIdentPla.getText());
                plate.setColor(textResultCorPla.getText());
                plate.setVehicle(vehicle);
                System.out.println(plate);

                HibernateService.insertValue(plate);
                HibernateService.closeSession();

                ImageSave.save(filePath);

                }
            }
        });
    }

    public void createAndShowGUI() {
        mainFrame.setContentPane(contentPane);
        Image image = Toolkit.getDefaultToolkit().getImage("src/main/java/org/bugbusters/gui/icon2.png");
        mainFrame.setIconImage(image);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AlplacaScreen tela = new AlplacaScreen();
            tela.createAndShowGUI();
        });
    }

    /**
     * Display the supported models
     */
    protected void displaySupportedModels() {
        String[] supportedModels = modelList.getModelDisplayNames().toArray(new String[0]);
        String[] title = {"Selecione um modelo"};
        String[] both = Arrays.copyOfRange(title, 0, supportedModels.length + title.length);
        System.arraycopy(supportedModels, 0, both, title.length, supportedModels.length);

        modelDropdown.setModel(new DefaultComboBoxModel(both));
    }

    /**
     * Display the trigger as a suggestion to install the selected model
     */
    protected void displayInstallModelTrigger() {
                addModelButton.setVisible(true);
    }

    /**
     * Hide the trigger as a suggestion to install the selected model
     */
    protected void hideInstallModelTrigger() {
                addModelButton.setVisible(false);
    }

    /**
     * Disable send request button
     */
    protected void disableSendRequestButton() {

        sendButton.setEnabled(false);
    }

    /**
     * Enable send request button
     */
    protected void enableSendRequestButton() {
                sendButton.setEnabled(true);
    }


    public void installModel(String modelName) throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        ollamaAPI.pullModel(modelName);
    }

    protected void showInstallingMessage(String modelName) {
        JOptionPane.showMessageDialog(null, "Instalando o modelo: " + modelName + ". Por favor, aguarde...");
    }

    /**
     * Show a message that the model installation is complete
     */
    protected void showInstallationCompleteMessage(String modelName) {
        JOptionPane.showMessageDialog(null, "Instalação do modelo " + modelName + " concluída com sucesso!");
    }


}
