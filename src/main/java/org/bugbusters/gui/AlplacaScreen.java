package org.bugbusters.gui;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.Model;
import io.github.ollama4j.models.response.OllamaResult;
import org.bugbusters.ollama.Ollama;
import org.bugbusters.ollama.OllamaRequest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
    private JComboBox dropdownOpt;
    private String fileName;
    private String filePath;


    public AlplacaScreen() {

        //JTextArea Line Break
        textResult.setLineWrap(true);
        textResult.setWrapStyleWord(true);
        OllamaAPI ollamaAPI = Ollama.getInstance();
        OllamaRequest request = new OllamaRequest(ollamaAPI, "");

        ArrayList<String> installedModels = new ArrayList<String>();


        //Fazer isso pelo BD depois (trampo do Gabriel)
        ArrayList<String> listModels = new ArrayList<String>() {{
            add("moondream");
            add("llava-phi3");
            add("llava-llama3");
        }};
        modelDropdown.setModel(new DefaultComboBoxModel(listModels.toArray()));

        try {
            List<Model> models = request.listAvailableModels();
            models.forEach(model -> installedModels.add(model.getModelName()));
        } catch (Exception e) {
            e.printStackTrace();
        }


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

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                String modelName = modelDropdown.getSelectedItem().toString();

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

                //Manda a imagem e o prompt para a AI e retorna a resposta na interface
                request.setModel(modelName);
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
                        textResult.setText(result.getResponse());
                    } else if(selectedItem.equals("Número da placa")) {
                        result = request.syncWithImageFilesRequest(
                            "This car plate model is: 3 letters - 4 numbers. Show me only the numbers and letters of this plate",
                            images
                        );
                        textResult.setText(result.getResponse());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String modelName = textAddModel.getText();

                //Verifica se modelo esta no dropbox
                if (listModels.contains(modelName)) {
                    JOptionPane.showMessageDialog(null,"Modelo já esta na lista");

                }else {
                    listModels.add(modelName); //Mudar para adicionar ao BD futuramente (trampo do Gabriel)
                    JOptionPane.showMessageDialog(null,"Modelo adicionado a lista");
                    modelDropdown.setModel(new DefaultComboBoxModel(listModels.toArray()));
                    modelDropdown.setSelectedItem(modelName);
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
}
