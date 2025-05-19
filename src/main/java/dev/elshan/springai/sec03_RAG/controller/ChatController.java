package dev.elshan.springai.sec03_RAG.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/sec03")
public class ChatController {
    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    public ChatController(ChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/const")
    public String askAboutConstitution(@RequestParam String question) {

        String userMessage = """ 
                Give me information about %s.
                """;
        String systemMessage = """ 
                If you dont have any information just tell this:
                Sorry l cant give information about this topic search internet
                """;

        var userMessage1 = new UserMessage(String.format(userMessage, question));
        var systemMessage1 = new SystemMessage(systemMessage);

        Prompt prompt = new Prompt(List.of(userMessage1, systemMessage1));

        var qaAdvisor = QuestionAnswerAdvisor
                .builder(vectorStore)
                .searchRequest(SearchRequest.builder().query(question).similarityThreshold(0.8d).topK(6).build())
                .build();

        return ChatClient.builder(chatModel)
                .defaultAdvisors(qaAdvisor)
                .build()
                .prompt(prompt)
                .call()
                .content();
    }
}