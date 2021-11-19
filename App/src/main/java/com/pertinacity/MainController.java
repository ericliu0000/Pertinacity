package com.pertinacity;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import javafx.concurrent.Task;

public class MainController {

    private boolean isRecording = false;
    @FXML
    private Label welcomeText;

    private int counter = 0;

    @FXML
    protected void record() {
        Task<> t = new Task<>(){
            @Override
            protected void call() throws Exception {
                AudioFormat format = new AudioFormat(44100, 24, 2, true, true);
            TargetDataLine line;
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, 
                format); // format is an AudioFormat object
            if (!AudioSystem.isLineSupported(info)) {
                // Handle the error ... 
                System.err.println("bruh");
            }
            // Obtain and open the line.
            try {
                isRecording = true;
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int numBytesRead;
                byte[] data = new byte[line.getBufferSize() / 5];
                line.start();
                while(isRecording){
                    numBytesRead = line.read(data, 0, data.length);
                    System.out.println(isRecording);
                    out.write(data, 0, numBytesRead);
                    counter++;
                }
                line.close();
    
                AudioInputStream inputStream = new AudioInputStream(
                    new ByteArrayInputStream(out.toByteArray()), format,
                    out.size());
                AudioSystem.write(inputStream, AudioFileFormat.Type.WAVE, 
                    new java.io.File("./test.wav"));
            } catch(Exception e){
                e.printStackTrace();
            }
            }
        };
        t.call();
    }

    @FXML
    protected void stopRecording(){
        isRecording = false;
        System.out.println("STOPPED RECORDING PRESSED");
    }

    
}