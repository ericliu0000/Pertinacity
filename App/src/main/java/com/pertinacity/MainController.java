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
import javax.swing.event.SwingPropertyChangeSupport;

import javafx.concurrent.Task;

public class MainController {
    boolean isRecording = false;

    Label recordingIndicator = new Label();

    Task<Integer> task = new Task<Integer>() {
        @Override
        protected Integer call() {
            AudioFormat format = new AudioFormat(44100, 24, 2, true, true);
        
            TargetDataLine line;
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // Obtain and open the line.
            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();
                
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] data = new byte[line.getBufferSize() / 5];
                int numBytesRead;

                while (isRecording) {
                    numBytesRead = line.read(data, 0, data.length);
                    System.out.println(String.format("Recording..."));
                    out.write(data, 0, numBytesRead);
                }

                line.close();
    
                AudioInputStream inputStream = new AudioInputStream(
                                    new ByteArrayInputStream(out.toByteArray()), 
                                    format,
                                    out.size());

                AudioSystem.write(inputStream, AudioFileFormat.Type.WAVE, 
                    new java.io.File("test.wav"));
            } catch (LineUnavailableException e) {
                System.err.println("Line unavailable");
            } catch (IOException e) {
                System.err.println("Unable to open the file");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Done");
            return 0;
        }
    };

    Thread t;

    @FXML
    protected void record() {
        isRecording = true;
        t = new Thread(task);
        t.start();
    }

    @FXML
    protected void stopRecording() {
        // if (t != null) {
        isRecording = false;
            // task.cancel(true);
            // t.interrupt();
        // }
        System.out.println(task);
    }
}