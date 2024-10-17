package org.bugbusters.gui;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.Model;
import io.github.ollama4j.models.response.ModelDetail;
import io.github.ollama4j.models.response.OllamaResult;
import org.bugbusters.database.ImageSave;
import org.bugbusters.ollama.ModelList;

import org.bugbusters.ollama.Models;
import org.bugbusters.ollama.Ollama;
import org.bugbusters.ollama.OllamaRequest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlplacaScreen {
    private JTextArea textResult1;
    private JButton openButton;
    private JButton sendButton;
    private JLabel textFile;
    private JPanel contentPane;
    private JComboBox modelDropdown;
    private JTextField textAddModel;
    private JButton addModelButton;
    private JComboBox dropdownOpt;
    private JTextArea textResult2;
    private JButton enviarBDButton;
    private String fileName;
    private String filePath;

    protected OllamaAPI ollamaAPI;
    protected OllamaRequest request;
    protected Models models;
    /**
     * List of model names Ollama supports
     */
    protected String[] supportedModels;


    public AlplacaScreen() {

        //JTextArea Line Break
        textResult1.setLineWrap(true);
        textResult1.setWrapStyleWord(true);
        textResult2.setLineWrap(true);
        textResult2.setWrapStyleWord(true);

        OllamaAPI ollamaAPI = Ollama.getInstance();
        OllamaRequest request = new OllamaRequest(ollamaAPI, "");
        models = new Models(ollamaAPI);

        ArrayList<String> installedModels = new ArrayList<String>();

        // Add models to dropdown
        displaySupportedModels();

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
            add("Número da placa");
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

                //String modelName = modelDropdown.getSelectedItem().toString();
                String modelName = ModelList.alplacaModels.getModelName(modelDropdown.getSelectedItem().toString());

                //Manda a imagem e o prompt para a AI e retorna a resposta na interface
                request.setModel(modelName);

                ImageSave.save(filePath);
                OllamaAPI ollamaAPI = Ollama.getInstance();
                OllamaRequest request = new OllamaRequest(ollamaAPI, "moondream");

                OllamaResult result;
                try {
                    File[] images = {
                        new File(filePath)
                    };
                    String selectedItem = (String) dropdownOpt.getSelectedItem();
                    if(selectedItem.equals("Localidade")){
                        result = request.syncWithImageFilesRequest(
                            "This plate has a text on top of it, this is where it's from, show me only it",
                            images
                        );
                        textResult1.setText(result.getResponse());
                    } else if(selectedItem.equals("Número da placa")) {
                        result = request.syncWithImageFilesRequest(
                            "This car plate model is: 3 letters - 4 numbers. Show me only the numbers and letters of this plate",
                            images
                        );
                        textResult2.setText(result.getResponse());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String modelName = modelDropdown.getSelectedItem().toString();

                try {

                    if (models.isInstalled(modelName)) {
                        JOptionPane.showMessageDialog(null, "O modelo já está instalado.");
                    } else {

                        JOptionPane.showMessageDialog(null, "Baixando modelo: " + modelName);
                        ollamaAPI.pullModel(modelName);

                        enableSendRequestButton();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao verificar ou baixar o modelo.");
                }
            }
        });


        modelDropdown.addActionListener(new ActionListener() {
            /**
             * Check whether the chosen model is installed, and suggest installation, in case it is not
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox trigger = (JComboBox) e.getSource();
                String modelName = (String) trigger.getSelectedItem();

                try {
                    if(models.isInstalled(modelName)) {
                        enableSendRequestButton();
                    } else {
                        // Display the button to suggest installation
                        displayInstallModelTrigger();
                        // Deactivate send request button
                        disableSendRequestButton();

                        // TODO: Call this from trigger listener
                        //  Install model
                        // models.installModel(modelName);
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        });
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Alplaca");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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
        Object[] supportedModels = ModelList.alplacaModels.getModelDisplayNames().toArray();
        modelDropdown.setModel(new DefaultComboBoxModel(supportedModels));
    }

    /**
     * Display the trigger as a suggestion to install the selected model
     */
    protected void displayInstallModelTrigger() {
        String modelName = modelDropdown.getSelectedItem().toString();

        try {
            if (!models.isInstalled(modelName)) {
                addModelButton.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide the trigger as a suggestion to install the selected model
     */
    protected void hideInstallModelTrigger() {
        String modelName = modelDropdown.getSelectedItem().toString();

        try {
            if (models.isInstalled(modelName)) {
                addModelButton.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Disable send request button
     */
    protected void disableSendRequestButton() {

        sendButton.setEnabled(false);

        String modelName = modelDropdown.getSelectedItem().toString();

        boolean fileSelected = filePath != null && !filePath.isEmpty();

        try {
            if (!fileSelected || !models.isInstalled(modelName)) {
                sendButton.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Enable send request button
     */
    protected void enableSendRequestButton() {
        String modelName = modelDropdown.getSelectedItem().toString();

        boolean fileSelected = filePath != null && !filePath.isEmpty();

        try {
            if (fileSelected && models.isInstalled(modelName)) {
                sendButton.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void installModel(String modelName) throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        ollamaAPI.pullModel(modelName);
    }
}
