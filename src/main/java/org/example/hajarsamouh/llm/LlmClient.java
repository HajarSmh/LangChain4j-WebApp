package org.example.hajarsamouh.llm;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.Dependent;

import java.io.Serializable;

/**
 * Classe "métier" liée au LLM choisi (Gemini), qui s'appuie sur LangChain4j.
 * De portée dependent : l'instance est supprimée quand l'instance du backing bean est supprimée.
 * Implémente Serializable car le backing bean (portée view) est Serializable.
 */
@Dependent
public class LlmClient implements Serializable {

    /** Le rôle que l'utilisateur choisit pour l'assistant IA. */
    private String systemRole;

    /** Interface qui décrit les interactions avec le LLM (implémentée par LangChain4j). */
    private Assistant assistant;

    /** Mémoire qui garde l'historique de la conversation. */
    private ChatMemory chatMemory;

    public LlmClient() {
        // Clé secrète de l'API, lue dans la variable d'environnement.
        String apiKey = System.getenv("GEMINI_KEY");

        // Le modèle (LLM) créé avec le builder.
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-3.5-flash")
                .build();

        // Mémoire de la conversation : garde les derniers messages.
        this.chatMemory = MessageWindowChatMemory.withMaxMessages(20);

        // Création de l'assistant (service IA) avec la mémoire.
        this.assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();
    }

    /**
     * Définit le rôle système. Le contexte change complètement : la mémoire est vidée,
     * puis le rôle est ajouté à la mémoire comme SystemMessage pour que le LLM en tienne compte.
     *
     * @param systemRole le rôle système choisi dans la liste déroulante.
     */
    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
        this.chatMemory.clear();
        this.chatMemory.add(SystemMessage.from(systemRole));
    }

    /**
     * Envoie une question au LLM et retourne sa réponse.
     *
     * @param prompt la question de l'utilisateur.
     * @return la réponse du LLM.
     */
    public String envoyerRequete(String prompt) {
        return this.assistant.chat(prompt);
    }
}