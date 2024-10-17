package org.bugbusters.ollama;

import io.github.ollama4j.exceptions.OllamaBaseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ModelList {
    /**
     * A comprehensive list of all multimodal (vision) model names supported by Ollama.
     * See {@link io.github.ollama4j.types.OllamaModelType}.
     */
    private ArrayList<Model> supportedModels;

    public ModelList() {
        resetSupportedModels();
    }

    public ArrayList<Model> resetSupportedModels() {
        int size = 5;
        this.supportedModels = new ArrayList<>(size);
        this.supportedModels.add(new Model("llava-llama3","Llava Llama 3",8));
        this.supportedModels.add(new Model("chat-gph-vision","Chat GPH",8));
        this.supportedModels.add(new Model("llava:7b","Llava",7));
        this.supportedModels.add(new Model("moondream","Moondream",1.7));
        this.supportedModels.add(new Model("llava-phi3","Llava Phi 3",3.8));

        return supportedModels;
    }

    public String getModelName(String displayName){
        int index = getModelDisplayNames().indexOf(displayName);
        Model model = supportedModels.get(index);
        return model.getName();
    }

    public ArrayList<String> getModelDisplayNames() {
        ArrayList<String> modelDisplayNames = new ArrayList<>();
        supportedModels.forEach(model -> modelDisplayNames.add(model.getDisplayName()+" - "+model.getWeight()));
        return modelDisplayNames;
    }

    public ArrayList<Model> getModels() {
        return supportedModels;
    }
}
