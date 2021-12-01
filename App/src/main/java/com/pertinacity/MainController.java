package com.pertinacity;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import javafx.concurrent.Task;

public class MainController {
    boolean isRecording = false;
    String dir;
    File file;

    public Label recordingIndicator = new Label();
    public TextField fileField = new TextField();
    private String fileName;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    AudioFormat format = new AudioFormat(44100, 24, 2, true, true);

    Task<Integer> task = createAudioTask();
    Thread t;

    @FXML
    protected void record() {
        recordingIndicator.setText("Recording");
        isRecording = true;


        if (fileField.getText().equals("")) {
            fileName = "recording";
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
            AudioSystem.write(inputStream, AudioFileFormat.Type.WAVE,
                    new File(dir + "/" + fileName + ".wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileField.clear();
        out = new ByteArrayOutputStream();
    }

    @FXML
    protected void pause() {
        recordingIndicator.setText(String.format("Stopped, %d bytes recorded", out.size()));
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

        dir = String.valueOf(fileChooser.showDialog(primaryStage));
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
                System.out.println("Done");
                return 0;
            }
        };    
    }
}