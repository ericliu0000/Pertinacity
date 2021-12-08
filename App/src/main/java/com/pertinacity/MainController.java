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
    // Create labels from FXML
    @FXML
    public Label recordingIndicator = new Label();

    @FXML
    private TextField fileField = new TextField();

    // Create objects for directory and file selection
    private Optional<File> dir = Optional.empty();
    private String fileName;

    // Create audio objects for recording
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private AudioFormat format = new AudioFormat(44100, 24, 2, true, true);

    private Task<Integer> task = createAudioTask();
    private Thread t;
    private boolean isRecording = false;


    /**
     * Begins recording audio, creating a new thread for the recording if starting a new recording.
     * If the thread is already running, it will restart the recording.
     */
    @FXML
    protected void record() {
        // Set indicators for user indication and task continuation
        recordingIndicator.setText("Recording");
        isRecording = true;

        // If the thread is not running, create a new thread
        // This will occur if the user is starting a new recording, rather than resuming from pause
        if (task.isDone()) {
            task = createAudioTask();
        }

        // Start the thread, beginning the recording
        t = new Thread(task);
        t.start();
    }

    /**
     * Stops the recording, and saves the audio file to the directory selected by the user.
     * The task will be cleared, and the thread will be stopped. Audio data will be saved to the desired file.
     * If no directory is selected, the user will be prompted to select one.
     * If no file name is entered, the current date and time will be used as the file name.
     */
    @FXML
    protected void stopRecording() {
        // Halt the task and thread, and prompt user for directory selection
        pause();
        chooseDirectory();

        // Update the file name based off of the text box
        // If there is no file name selected, use the current date and time
        if (fileField.getText().equals("")) {
            fileName = LocalDateTime.now().toString();
        } else {
            fileName = fileField.getText();
        }

        // Create audio input stream from the prewritten audio data
        AudioInputStream inputStream = new AudioInputStream(
                new ByteArrayInputStream(out.toByteArray()),
                format,
                out.size());

        try {
            // Write the audio data to the file selected, or the current working directory if no directory is selected
            // Update user indication to reflect audio status
            if (dir.isPresent()) {
                String filePath = Paths.get(dir.get().toString(), (fileName + ".wav")).toString();
                AudioSystem.write(inputStream, AudioFileFormat.Type.WAVE,
                        new File(filePath));

                recordingIndicator.setText("Saved, Not Recording");
            } else {
                AudioSystem.write(inputStream, AudioFileFormat.Type.WAVE,
                        new File(fileName + ".wav"));

                recordingIndicator.setText("Saved to current working directory, Not Recording");
            }
        } catch (IOException e) {
            // If the file cannot be written, update user indication
            e.printStackTrace();
            recordingIndicator.setText("Error saving file");
        }

        // Clear input box and reset audio data
        fileField.clear();
        out = new ByteArrayOutputStream();
    }

    /**
     * Pauses the recording, and preserves the task and thread for resuming later.
     */
    @FXML
    protected void pause() {
        // Reject the task if it does not exist
        if (!(t instanceof Thread)) {
            return;
        }

        // Set flags for user indication and task cancellation
        recordingIndicator.setText("Recording Paused");
        isRecording = false;

        // Wait for the thread to finish
        try {
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets directory field to the user's selection.
     * If no directory is selected or a nonexistent directory is selected, the field will be set to <code>null</code>.
     */
    private void chooseDirectory() {
        // Invoke directory selection dialog
        Stage primaryStage = (Stage) recordingIndicator.getScene().getWindow();
        DirectoryChooser fileChooser = new DirectoryChooser();

        dir = Optional.ofNullable(fileChooser.showDialog(primaryStage));
    }

    /**
     * Creates a new task for recording audio.
     *
     * @return A new Task that writes audio into the ByteArrayOutputStream field.
     */
    private Task<Integer> createAudioTask() {
        return new Task<Integer>() {
            @Override
            protected Integer call() {
                // Create a new audio line for recording with default format
                TargetDataLine line;
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                // Begin recording
                try {
                    // Open audio line
                    line = (TargetDataLine) AudioSystem.getLine(info);
                    line.open(format);
                    line.start();

                    // Buffer creation for audio recording
                    byte[] data = new byte[line.getBufferSize() / 5];
                    int numBytesRead;

                    // Recording loop until recording flag is updated or task is cancelled
                    while (isRecording) {
                        numBytesRead = line.read(data, 0, data.length);
                        out.write(data, 0, numBytesRead);
                    }

                    line.close();
                } catch (LineUnavailableException e) {
                    System.err.println("Line unavailable");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        };
    }
}