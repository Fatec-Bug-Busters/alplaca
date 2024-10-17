package org.bugbusters.ollama;

import java.util.ArrayList;

public class ModelList {
    private ArrayList<Model> models;

    public static final ModelList alplacaModels = new ModelList(new ArrayList<Model>() {{
        add(new Model("llava-llama3","Llava Llama 3",8));
        add(new Model("chat-gph-vision","Chat GPH",8));
        add(new Model("llava:7b","Llava",7));
        add(new Model("moondream","Moondream",1.7));
        add(new Model("llava-phi3","Llava Phi 3",3.8));
    }});

    public ModelList(ArrayList<Model> models) {
        this.models = models;
    }

    public void addModel(Model model) {
        this.models.add(model);
    }

    public void removeModel(Model model) {
        this.models.remove(model);
    }

    public String getModelName(String displayName){
        displayName = displayName.replace(" - Leve", "");
        displayName = displayName.replace(" - Intermediário", "");
        displayName = displayName.replace(" - Pesado", "");

        int index = alplacaModels.getModelDisplayNames().indexOf(displayName);
        Model model = models.get(index);
        return model.getName();
    }

    public ArrayList<String> getModelDisplayNames() {
        ArrayList<String> modelDisplayNames = new ArrayList<>();
        models.forEach(model -> modelDisplayNames.add(model.getDisplayName()+" - "+model.getWeight()));
        return modelDisplayNames;
    }
}
