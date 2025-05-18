package dev.elshan.springai.multimodality;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sec02")
public class GenerateImageController {
    private static final Logger log = LoggerFactory.getLogger(GenerateImageController.class);
    private final ImageModel imageModel;

    public GenerateImageController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @PostMapping("/generateImage")
    public String generateImage(String prompt) {
        ImagePrompt imagePrompt = new ImagePrompt(prompt,
                OpenAiImageOptions.builder()
                        .quality("hd")
                        .N(1)
                        .height(1024)
                        .width(1024).build());

        ImageResponse imageResponse = imageModel.call(imagePrompt);
        var imageUrl = imageResponse.getResult().getOutput().getUrl();
        log.info("Image Url: {}", imageUrl);
        return imageUrl;
    }
}
