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
    boolean isRecording = false;

    Task<Integer> task = new Task<Integer>() {
        @Override protected Integer call() throws Exception {
            long iterations;
            for (iterations = 0; iterations < Long.MAX_VALUE; iterations++) {
                if (!isRecording) {
                    break;
                }
                System.out.println("Iteration " + iterations);
            }
            System.out.println("Done");
            return (int) iterations;
        }
    };

    Thread t = new Thread(task);

    @FXML 
    protected void record(){
        isRecording = true;
        t.setDaemon(true);
        t.start();
    }


    @FXML
    protected void stopRecording(){
        isRecording = false;
        task.cancel();
        System.out.println(task);
        t.interrupt();

    }

    
}