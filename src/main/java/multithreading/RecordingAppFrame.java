package multithreading;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.geometry.Insets;
import java.io.*;
import javax.sound.sampled.*;
import java.net.URISyntaxException;

public class RecordingAppFrame extends FlowPane {

    public static final String ERROR_FLAG = "ERROR";

    public interface TranscriptionCallback{
        void onTranscriptionComplete(String transcript);
    }

    private Button startButton;
    private Button stopButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    private String fileName;

    private TranscriptionCallback transcriptionCallback;
    private Thread recordingThread;

    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 150px; -fx-pref-height: 10px; -fx-text-fill: red; visibility: hidden";

    public RecordingAppFrame(String fileName) {
        this.fileName = fileName;
        // Set properties for the flowPane
        this.setPrefSize(100, 120);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setVgap(10);
        this.setHgap(10);
        this.setPrefWrapLength(170);

        // Add the buttons and text fields
        startButton = new Button("Start");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        this.getChildren().addAll(startButton, stopButton, recordingLabel);

        // Get the audio format
        audioFormat = getAudioFormat();

        // Add the listeners to the buttons
        addListeners();
    }

    public void addListeners() {
        // Start Button
        startButton.setOnAction(e -> {
            startRecording();
        });

        // Stop Button
        stopButton.setOnAction(e -> {
            stopRecording();
        });
    }

    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    private void startRecording() {
        recordingLabel.setVisible(true);

        recordingThread = new Thread(() -> {
            try {
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(audioFormat);
                targetDataLine.start();

                AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                File audioFile = new File(fileName);
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                targetDataLine.close();
                recordingLabel.setVisible(false);
            }
        });

        recordingThread.start();
    }

    public void getStartRecording() {
        this.startRecording();
    }

    private void stopRecording() {
        if (targetDataLine != null) {
            targetDataLine.stop();
            // targetDataLine.close();
        }
        if (transcriptionCallback != null) {
            String transcript = getRecordingTranscript(fileName);
            transcriptionCallback.onTranscriptionComplete(transcript);
        }
    }

    public void getStopRecording() {
        this.stopRecording();
    }

    public void setTranscriptionCallback(TranscriptionCallback callback) {
        this.transcriptionCallback = callback;
    }

    public String getRecordingTranscript(String fileName) {
        try {
            return Whisper.getWhisperTranscript(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An I/O error occurred: " + e.getMessage());
            return ERROR_FLAG;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Invalid URI: Check file path.");
            return ERROR_FLAG;
        } 
    }
}
