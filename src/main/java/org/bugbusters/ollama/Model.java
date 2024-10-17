package org.bugbusters.ollama;

public class Model {
    private String name;
    private String displayName;
    private double params;

    public Model(String name, String displayName, double params) {
        this.name = name;
        this.displayName = displayName;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getWeight(){
        if (params<2) {
            return "Leve";
        }
        else if (params<4) {
            return "Intermediário";
        }
        return "Pesado";
    }
}
