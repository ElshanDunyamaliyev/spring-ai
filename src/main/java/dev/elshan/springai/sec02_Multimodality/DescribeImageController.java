package dev.elshan.springai.sec02_Multimodality;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sec02")
public class DescribeImageController {
    private final ChatModel chatModel;

    public DescribeImageController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/image-text")
    public String describeImage() {
        return ChatClient.create(chatModel)
                .prompt()
                .user(promptUserSpec -> {
                    promptUserSpec.text("What do you see in this image")
                            .media(MimeTypeUtils.IMAGE_PNG, new ClassPathResource("img/multimodal.test.png"));
                }).call().content();
    }
}
