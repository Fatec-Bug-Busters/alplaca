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

                    // Habilitar o botão Enviar caso o modelo esteja instalado e o arquivo seja selecionado
                    enableSendRequestButton();
                } else if (returnValue == JFileChooser.CANCEL_OPTION) {
                    textFile.setText("Nenhum arquivo selecionado");
                    disableSendRequestButton(); // Desabilitar se nenhum arquivo for selecionado
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
                String modelName = modelDropdown.getSelectedItem().toString(); // ou textAddModel.getText(), conforme o campo correto

                try {
                    // Verifica se o modelo já está instalado
                    if (models.isInstalled(modelName)) {
                        JOptionPane.showMessageDialog(null, "O modelo já está instalado.");
                    } else {
                        // Baixa o modelo, pois não está instalado
                        JOptionPane.showMessageDialog(null, "Baixando modelo: " + modelName);
                        ollamaAPI.pullModel(modelName); // Instala o modelo
                        // Após a instalação, habilita o botão enviar se um arquivo também foi selecionado
                        enableSendRequestButton();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao verificar ou baixar o modelo.");
                }
            }
        });


        modelDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox trigger = (JComboBox) e.getSource();
                String modelName = (String) trigger.getSelectedItem();

                try {
                    if (models.isInstalled(modelName)) {
                        enableSendRequestButton(); // Habilita o botão caso o modelo esteja instalado e o arquivo selecionado
                    } else {
                        disableSendRequestButton(); // Desabilita o botão caso o modelo não esteja instalado
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
        String modelName = modelDropdown.getSelectedItem().toString();

        // Verifica se o modelo está instalado
        try {
            if (!models.isInstalled(modelName)) {
                addModelButton.setVisible(true); // Mostra o botão "Instalar"
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

        // Verifica se o modelo está instalado
        try {
            if (models.isInstalled(modelName)) {
                addModelButton.setVisible(false); // Esconde o botão "Instalar"
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

        // Verifica se o arquivo foi selecionado
        boolean fileSelected = filePath != null && !filePath.isEmpty();

        // Verifica se o modelo está instalado
        try {
            if (!fileSelected || !models.isInstalled(modelName)) {
                sendButton.setEnabled(false); // Desabilita o botão "Enviar"
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

        // Verifica se o arquivo foi selecionado
        boolean fileSelected = filePath != null && !filePath.isEmpty();

        // Verifica se o modelo está instalado
        try {
            if (fileSelected && models.isInstalled(modelName)) {
                sendButton.setEnabled(true); // Habilita o botão "Enviar"
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void installModel(String modelName) throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        ollamaAPI.pullModel(modelName);
    }
}
