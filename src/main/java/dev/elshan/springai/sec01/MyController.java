package dev.elshan.springai.sec01;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MyController {
    private final ChatClient chatClient;

    public MyController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai")
    String generation(String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

    @GetMapping("/ai/celebrity")
    String getCelebrityAchievements(String celebrityName) {
        PromptTemplate promptTemplate = new PromptTemplate("Tell me a few achievement of this {celebrity}");
        Prompt prompt = promptTemplate.create(Map.of("celebrity", celebrityName));
        return chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/ai/roles")
    String getAnswerWithRoles(String sport) {
        String userMessage = """ 
                Give me information about %s
                What is the rules, tips and tricks.
                """;
        String systemMessage = """ 
                Give only information about sports
                If they ask anything else say l dont have any information.
                Also try to talk like a mafia boss.
                """;

        var userMessage1 = new UserMessage(String.format(userMessage, sport));
        var systemMessage1 = new SystemMessage(systemMessage);

        Prompt prompt = new Prompt(List.of(userMessage1, systemMessage1));

        return chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/ai/movies")
    List<ActorsFilms> getMovies(String actor) {
        BeanOutputConverter<ActorsFilms> beanOutputConverter =
                new BeanOutputConverter<>(ActorsFilms.class);
        String format = beanOutputConverter.getFormat();

        String userMessage = """ 
                Give me movies of %s.
                %s
                """;
        String systemMessage = """ 
                Give only information about actor and movies
                And try to talk like a lgbt member and be so kind
                """;

        var userMessage1 = new UserMessage(String.format(userMessage, actor, format));
        var systemMessage1 = new SystemMessage(systemMessage);

        Prompt prompt = new Prompt(List.of(userMessage1, systemMessage1));

        return chatClient
                .prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });
    }
}
