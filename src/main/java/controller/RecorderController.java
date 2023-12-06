package controller;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import utilites.Logger;
import utilites.Whisper;

import java.io.*;
import javax.sound.sampled.*;

public class RecorderController implements Initializable {

    public static final String ERROR_FLAG = "ERROR";

    public interface TranscriptionCallback {
        String onTranscriptionComplete(String transcript);
    }

    @FXML
    private Button startRecordingButton;

    @FXML
    private Button stopRecordingButton;

    @FXML
    private Label recordingLabel;

    private String defaultRecordingLabelText;

    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private String fileName;

    private TranscriptionCallback transcriptionCallback;
    private Thread recordingThread;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void startRecordingHandler() {
        startRecording();
    }

    public void stopRecordingHandler() {
        stopRecording();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        audioFormat = getAudioFormat();

        recordingLabel.setVisible(false);

        startRecordingButton.setDisable(false);
        stopRecordingButton.setDisable(true);

        defaultRecordingLabelText = recordingLabel.getText();
    }

    public void setTranscriptionCallback(TranscriptionCallback callback) {
        this.transcriptionCallback = callback;
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

    public String getRecordingTranscript(String fileName) {
        try {
            return Whisper.getWhisperTranscript(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log("An I/O error occurred: " + e.getMessage());
            return ERROR_FLAG;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Logger.log("Invalid URI: Check file path.");
            return ERROR_FLAG;
        }
    }

    private void startRecording() {
        recordingLabel.setVisible(true);
        recordingLabel.setText(defaultRecordingLabelText);

        startRecordingButton.setDisable(true);
        stopRecordingButton.setDisable(false);

        recordingThread = new Thread(() -> {
            try {
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(audioFormat);
                targetDataLine.start();

                AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                Logger.log("Recording started for file " + fileName);
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

    private void stopRecording() {
        stopRecordingButton.setDisable(true);
        startRecordingButton.setDisable(false);

        if (targetDataLine != null) {
            targetDataLine.stop();
            // targetDataLine.close();
        }

        if (transcriptionCallback != null) {
            String transcript = getRecordingTranscript(fileName);
            transcript = transcriptionCallback.onTranscriptionComplete(transcript);

            recordingLabel.setVisible(true);

            if (transcript == null) {
                recordingLabel.setText("Not a valid option.");
            } else {
                recordingLabel.setText("You said: " + transcript);
            }
        }
    }
}