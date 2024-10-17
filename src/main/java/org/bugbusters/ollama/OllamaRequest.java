package org.bugbusters.ollama;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.Model;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Make a Request to the Ollama server
 */
public class OllamaRequest {

    private OllamaAPI api;
    private OptionsBuilder optionsBuilder;

    /**
     * Vision Model for Ollama
     * @see io.github.ollama4j.types.OllamaModelType
     */
    private String model;

    public OllamaRequest(OllamaAPI api) {
        this.setApi(api);

        this.optionsBuilder = new OptionsBuilder();
    }

    public OllamaRequest(OllamaAPI api, String model) {
        this.setApi(api)
            .setModel(model);

        this.optionsBuilder = new OptionsBuilder();
    }

    /**
     * Make a sync request
     *
     * @param prompt
     * @return
     * @throws OllamaBaseException
     * @throws IOException
     * @throws InterruptedException
     */
    public OllamaResult syncRequest(String prompt) throws OllamaBaseException, IOException, InterruptedException {
        return this.api.generate(
            this.model,
            prompt,
            true,
            this.optionsBuilder.build()
        );
    }

    /**
     * Make a sync with image files request
     *
     * @param prompt
     * @param imageFiles  An array containing the images
     * @return
     * @throws OllamaBaseException
     * @throws IOException
     * @throws InterruptedException
     */
    public OllamaResult syncWithImageFilesRequest(String prompt, File[] imageFiles) throws OllamaBaseException, IOException, InterruptedException {

        return this.api.generateWithImageFiles(
            this.model,
            prompt,
            List.of(imageFiles),
            this.optionsBuilder.build()
        );
    }

    /**
     * List the available models
     */
    public List<Model> listAvailableModels() throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        return this.api.listModels();
    }

    /**
     * Test the connection to the Ollama server
     *
     * @return
     */
    public boolean testConnection() {
        try {
            return this.api.ping();
        } catch (Exception e) {
            return false;
        }
    }


    public String getModel() {
        return model;
    }

    public OllamaRequest setModel(String model) {
        this.model = model;
        return this;
    }

    public OllamaAPI getApi() {
        return api;
    }

    public OllamaRequest setApi(OllamaAPI api) {
        this.api = api;
        return this;
    }

    public OptionsBuilder getOptionsBuilder() {
        return optionsBuilder;
    }

    public OllamaRequest setOptionsBuilder(OptionsBuilder optionsBuilder) {
        this.optionsBuilder = optionsBuilder;

        return this;
    }
}
