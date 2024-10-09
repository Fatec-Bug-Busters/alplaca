package org.bugbusters.gui;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.Model;
import io.github.ollama4j.models.response.ModelDetail;
import io.github.ollama4j.models.response.OllamaResult;

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
    private JTextArea textResult;
    private JButton openButton;
    private JButton sendButton;
    private JLabel textFile;
    private JPanel contentPane;
    private JComboBox modelDropdown;
    private JTextField textAddModel;
    private JButton addModelButton;
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
        textResult.setLineWrap(true);
        textResult.setWrapStyleWord(true);

        ollamaAPI = Ollama.getInstance();
        request = new OllamaRequest(ollamaAPI, "");
        models = new Models(ollamaAPI);

        // Add models to dropdown
        displaySupportedModels();

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
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                String modelName = modelDropdown.getSelectedItem().toString();

                /*
                //Verifica se o modelo já esta installado e da a opção de instalar caso não esteja
                if (!installedModels.contains(modelName)) {
                    int confirmation = JOptionPane.showConfirmDialog(null,
                        "Modelo não instalado. Deseja instalar?",
                        "confirmação",
                        JOptionPane.YES_NO_OPTION
                        );
                    if (confirmation == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null,"Baixando modelo");

                        //Baixa o modelo
                        try {
                            ollamaAPI.pullModel(modelName);
                            installedModels.add(modelName);
                        } catch (OllamaBaseException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } catch (URISyntaxException ex) {
                            throw new RuntimeException(ex);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                 */

                //Manda a imagem e o prompt para a AI e retorna a resposta na interface
                request.setModel(modelName);
                OllamaResult result;
                try {
                    File[] images = {
                        new File(filePath)
                    };
                    result = request.syncWithImageFilesRequest(
                        "Answer me without an explanation what's the license plate number, the color of the car and where is it from?",
                        images
                    );
                    textResult.setText(result.getResponse());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String modelName = textAddModel.getText();

                /*
                //Verifica se modelo esta no dropbox
                if (listModels.contains(modelName)) {
                    JOptionPane.showMessageDialog(null,"Modelo já esta na lista");

                }else {
                    listModels.add(modelName); //Mudar para adicionar ao BD futuramente (trampo do Gabriel)
                    JOptionPane.showMessageDialog(null,"Modelo adicionado a lista");
                    modelDropdown.setModel(new DefaultComboBoxModel(listModels.toArray()));
                    modelDropdown.setSelectedItem(modelName);
                }
                 */
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
                    if(!models.isInstalled(modelName)) {
                        // Display the button to suggest installation
                        displayInstallModelTrigger();
                        // Deactivate send request button
                        deactivateSendRequestButton();
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
        supportedModels = models.getSupportedModels();
        Arrays.sort(supportedModels);
        modelDropdown.setModel(new DefaultComboBoxModel(supportedModels));
    }

    /**
     * Display the trigger as a suggestion to install the selected model
     */
    protected void displayInstallModelTrigger() {
        //
    }

    /**
     * Deactivate send request button
     */
    protected void deactivateSendRequestButton() {
        //
    }
}
