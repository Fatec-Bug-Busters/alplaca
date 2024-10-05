package org.bugbusters.gui;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.response.OllamaResult;
import org.bugbusters.database.ImageSave;
import org.bugbusters.ollama.Ollama;
import org.bugbusters.ollama.OllamaRequest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AlplacaScreen {
    private JTextArea textResult;
    private JButton openButton;
    private JButton sendButton;
    private JLabel textFile;
    private JPanel contentPane;
    private String fileName;
    private String filePath;

    public AlplacaScreen() {

        //JTextArea Line Break
        textResult.setLineWrap(true);
        textResult.setWrapStyleWord(true);

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
                    textFile.setText("No File Selected");

                }
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ImageSave.save(filePath);
                OllamaAPI ollamaAPI = Ollama.getInstance();
                OllamaRequest request = new OllamaRequest(ollamaAPI, "moondream");
                OllamaResult result;
                try {
                    File[] images = {
                        new File(filePath)
                    };
                    result = request.syncWithImageFilesRequest(
                        "Read the alphanumeric identification of this car plate.",
                        images
                    );
                    textResult.setText(result.getResponse());
                } catch (Exception e) {
                    e.printStackTrace();
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
