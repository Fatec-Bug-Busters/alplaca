package samples;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.response.Model;
import io.github.ollama4j.types.OllamaModelType;

import java.io.File;
import java.util.List;

/**
 * Examples
 */
public class Sample {
    public static void main(String[] args) {

        // Connect
        OllamaAPI ollamaAPI = Ollama.getInstance();

        // Factory a Request object
        OllamaRequest request = new OllamaRequest(ollamaAPI, OllamaModelType.LLAMA2);

        // test the connection
        boolean isConnected = request.testConnection();
        System.out.println("Is connected to the Ollama server? " + isConnected);

        // change Ollama model
        request.setModel(OllamaModelType.LLAVA);

        // List available models
        try {
            System.out.println("\nAvailable models: ");
            List<Model> models = request.listAvailableModels();
            models.forEach(model -> System.out.println(model.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send an image and prompt to the Ollama server
        try {
            File[] images = {
                new File("./placas/placa1'.jpg"),
                new File("./placas/placa-no-carro'.jpg"),
            };
            request.syncWithImageFilesRequest(
                "Read the alphanumeric identification of this car plate.",
                images
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
