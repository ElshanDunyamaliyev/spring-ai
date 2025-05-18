package dev.elshan.springai.sec02_Multimodality;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/sec02")
public class TextToSpeechController {
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    public TextToSpeechController(OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }

    @PostMapping("/textToSpeech")
    public byte[] textToSpeech(){
        var speechOptions = OpenAiAudioSpeechOptions.builder()
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ASH)
                .speed(1.0f)
                .model(OpenAiAudioApi.TtsModel.TTS_1.value)
                .build();

        var speechPrompt = new SpeechPrompt("I'm beginnin' to feel like a Rap God, Rap God\n" +
                "All my people from the front to the back nod, back nod\n" +
                "Now, who thinks their arms are long enough to slap box, slap box?\n" +
                "They said I rap like a robot, so call me Rap-bot", speechOptions);

        SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);

        byte[] output = response.getResult().getOutput();
        writeSpeechToMp3File(output);
        return output;
    }


    public void writeSpeechToMp3File(byte[] audioBytes) {
        Path outputPath = Path.of("src/main/resources/audio/rapgod.mp3");

        File file = outputPath.toFile();

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(audioBytes);
            fos.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
