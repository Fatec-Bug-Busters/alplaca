package org.bugbusters.ollama;

import io.github.ollama4j.OllamaAPI;

/**
 * Single-threaded singleton to connect to Ollama server
 */
public class Ollama {
    private static OllamaAPI ollamaAPI;

    private Ollama() {
    }

    /**
     *
     * @return
     */
    public static OllamaAPI getInstance() {
        return Ollama.getInstance("http://localhost:11434", 100L);
    }

    /**
     * Get a single-threaded singleton instance of OllamaAPI object.
     *
     * Do not replace the host address if the instance already exists.
     *
     * @param host
     * @return
     */
    public static OllamaAPI getInstance(String host, long requestTimeoutSeconds) {
        if (ollamaAPI != null) {
            return ollamaAPI;
        }

        Ollama.ollamaAPI = new OllamaAPI(host);
        Ollama.ollamaAPI.setRequestTimeoutSeconds(requestTimeoutSeconds);

        return ollamaAPI;
    }
}
