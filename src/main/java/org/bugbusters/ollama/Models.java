package org.bugbusters.ollama;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.Model;
import io.github.ollama4j.models.response.ModelDetail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Ollama models
 */
public class Models {
    private OllamaAPI api;

    public Models(OllamaAPI api) {
        setApi(api);
    }

    /**
     * Whether a model is multimodal (vision)
     * @param modelName
     * @return
     * @throws IOException
     * @throws OllamaBaseException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    public boolean isMultimodal(String modelName) throws IOException, OllamaBaseException, URISyntaxException, InterruptedException {
        /* {@link https://github.com/ollama/ollama/issues/2608} */
        ModelDetail detail = this.getModelDetail(modelName);
        boolean isMultimodal = Arrays.asList(detail.getDetails().getFamilies()).contains("clip");
        return isMultimodal;
    }

    /**
     * List the installed models
     */
    public List<Model> getAvailableModels() throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        return this.api.listModels();
    }

    /**
     * Whether a model is installed
     * @param modelName
     * @return
     * @throws OllamaBaseException
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    public boolean isInstalled(String modelName) throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        List<Model> installedModels = getAvailableModels();
        return installedModels.stream().anyMatch(model -> model.getModelName().equals(modelName));
    }

    public void installModel(String modelName) throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        api.pullModel(modelName);
    }

    public ModelDetail getModelDetail(String modelName) throws IOException, OllamaBaseException, URISyntaxException, InterruptedException {
        return this.api.getModelDetails(modelName);
    }

    public OllamaAPI getApi() {
        return api;
    }

    public Models setApi(OllamaAPI api) {
        this.api = api;
        return this;
    }
}
