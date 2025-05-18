package dev.elshan.springai.multimodality;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sec02")
public class AudioTranscriptionController {
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    public AudioTranscriptionController(OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel) {
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
    }

    @PostMapping("/transcriptAudio")
    public String transcriptAudio(){
        var transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .temperature(0f)
                .build();

        var audioFile = new ClassPathResource("audio/let_me_down_slowly.mp3");

        AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioFile,transcriptionOptions);

        AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(transcriptionRequest);

        return response.getResult().getOutput();
    }
}
