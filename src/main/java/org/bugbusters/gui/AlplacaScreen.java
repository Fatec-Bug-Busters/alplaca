package org.bugbusters.gui;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import org.bugbusters.database.ImageSave;
import org.bugbusters.ollama.ModelList;

import org.bugbusters.ollama.Models;
import org.bugbusters.ollama.Ollama;
import org.bugbusters.ollama.OllamaRequest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class AlplacaScreen {
    JFrame mainFrame;
    private JTextArea textResult1;
    private JButton openButton;
    private JButton sendButton;
    private JLabel textFile;
    private JPanel contentPane;
    private JComboBox modelDropdown;
    private JButton addModelButton;
    private JComboBox dropdownOpt;
    private JTextArea textResult2;
    private JButton enviarBDButton;
    private JButton detalheButton;
    private JButton placasButton;
    private JButton inteligenciaButton;
    private JPanel headerPanel;
    private JLabel logoLabel;
    private String fileName;
    private String filePath;

    protected OllamaAPI ollamaAPI;
    protected OllamaRequest request;
    protected Models models;
    protected ModelList modelList;


    public AlplacaScreen() {
        mainFrame = new JFrame("Alplaca");

        //JTextArea Line Break
        textResult1.setLineWrap(true);
        textResult1.setWrapStyleWord(true);
        textResult2.setLineWrap(true);
        textResult2.setWrapStyleWord(true);

        OllamaAPI ollamaAPI = Ollama.getInstance();
        OllamaRequest request = new OllamaRequest(ollamaAPI);
        models = new Models(ollamaAPI);
        modelList = new ModelList();

        // Add models to dropdown
        displaySupportedModels();
        hideInstallModelTrigger();

        // header
        inteligenciaButton.setEnabled(false);
        headerPanel.setBackground(Color.decode("#cccccc"));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

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

                String modelName = modelList.getModelName(modelDropdown.getSelectedItem().toString());
                request.setModel(modelName);

                ImageSave.save(filePath);

                OllamaResult result;
                try {
                    File[] images = {
                        new File(filePath)
                    };
                    String selectedItem = (String) dropdownOpt.getSelectedItem();
                    if (selectedItem.equals("Localidade")) {
                        result = request.syncWithImageFilesRequest(
                            "This plate has a text on top of it, this is where it's from, show me only it",
                            images
                        );
                        textResult1.setText(result.getResponse());
                    } else if (selectedItem.equals("Número da placa")) {
                        result = request.syncWithImageFilesRequest(
                            "This car plate model is: 3 letters - 1 number - 1 letter - 2 numbers. Show me only the numbers and letters of this plate",
                            images
                        );
                        textResult2.setText(result.getResponse());
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
                openTelaLista();
            }
        });
        inteligenciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAlplacaScreen();
            }
        });
        placasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openTelaLista();
            }
        });

        loadLogo();
    }

    public void createAndShowGUI() {
        int width = 800;
        int height = 600;
        mainFrame.setContentPane(contentPane);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(width, height);
        // mainFrame.pack();
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

    protected void openTelaLista() {
        TelaLista telaLista = new TelaLista(mainFrame);
        Rectangle windowSize = this.mainFrame.getBounds();
        telaLista.createAndShowGUI(windowSize);
    }

    protected void openAlplacaScreen() {
        return;
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
