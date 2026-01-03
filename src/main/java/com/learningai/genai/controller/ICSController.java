package com.learningai.genai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ICSController {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    private final String prompt = """
        Your task is to answer questions about the Indian Constitution.
        Use ONLY the information from the DOCUMENTS section.
        If the answer is not present, say "I don't know".

        QUESTION:
        {input}

        DOCUMENTS:
        {documents}
        """;

    public ICSController(ChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/law")
    public String simplify(
            @RequestParam(value = "question",
                    defaultValue = "What is Article 21 of the Indian Constitution?")
            String question) {

        PromptTemplate template = new PromptTemplate(prompt);

        Map<String, Object> params = new HashMap<>();
        params.put("input", question);
        params.put("documents", findSimilarData(question));

        Prompt finalPrompt = template.create(params);

        return chatModel.call(finalPrompt).getResult().getOutput().getText();
    }

    private String findSimilarData(String question) {

        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder().query(question).topK(5).build());

        return documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));
    }
}