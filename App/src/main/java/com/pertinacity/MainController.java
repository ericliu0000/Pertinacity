package com.pertinacity;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import javafx.concurrent.Task;

public class MainController {
    public Label recordingIndicator = new Label();
    private TextField fileField = new TextField();

    private boolean isRecording = false;
    private Optional<File> dir = Optional.empty();
    private String fileName;

    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private AudioFormat format = new AudioFormat(44100, 24, 2, true, true);

    private Task<Integer> task = createAudioTask();
    private Thread t;

    @FXML
    protected void record() {
        recordingIndicator.setText("Recording");
        isRecording = true;

        if (fileField.getText().equals("")) {
            fileName = LocalDateTime.now().toString();
        } else {
            fileName = fileField.getText();
        }

        if (task.isDone()) {
            task = createAudioTask();
        }

        t = new Thread(task);
        t.start();
    }

    @FXML
    protected void stopRecording() {
        pause();
        chooseDirectory();

        AudioInputStream inputStream = new AudioInputStream(
                new ByteArrayInputStream(out.toByteArray()),
                format,
                out.size());

        try {
            if (dir.isPresent()) {
                String filePath = Paths.get(dir.get().toString(), (fileName + ".wav")).toString();
                AudioSystem.write(inputStream, AudioFileFormat.Type.WAVE,
                    new File(filePath));

                recordingIndicator.setText(String.format("Not Recording", out.size(), filePath));
            } else {
                AudioSystem.write(inputStream, AudioFileFormat.Type.WAVE,
                    new File(fileName + ".wav"));

                recordingIndicator.setText(String.format("Not Recording", out.size()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileField.clear();
        out = new ByteArrayOutputStream();
    }

    @FXML
    protected void pause() {
        recordingIndicator.setText("Recording Paused");
        isRecording = false;

        try {
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chooseDirectory() {
        Stage primaryStage = (Stage) recordingIndicator.getScene().getWindow();
        DirectoryChooser fileChooser = new DirectoryChooser();

        dir = Optional.ofNullable(fileChooser.showDialog(primaryStage));
    }

    private Task<Integer> createAudioTask() {
        return new Task<Integer>() {
            @Override
            protected Integer call() {
                TargetDataLine line;
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                try {
                    line = (TargetDataLine) AudioSystem.getLine(info);
                    line.open(format);
                    line.start();

                    byte[] data = new byte[line.getBufferSize() / 5];
                    int numBytesRead;

                    while (isRecording) {
                        numBytesRead = line.read(data, 0, data.length);
                        System.out.println(String.format("Recording..."));
                        out.write(data, 0, numBytesRead);
                    }

                    line.close();
                } catch (LineUnavailableException e) {
                    System.err.println("Line unavailable");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // System.out.println("Done");
                return 0;
            }
        };
    }
}