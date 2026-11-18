package org.example.hajarsamouh.llm;

/**
 * Interface du "service IA" de LangChain4j.
 * LangChain4j fournit automatiquement l'implémentation (un objet proxy).
 */
public interface Assistant {

    /**
     * Envoie une question au LLM et retourne sa réponse.
     *
     * @param prompt la question de l'utilisateur.
     * @return la réponse du LLM.
     */
    String chat(String prompt);
}