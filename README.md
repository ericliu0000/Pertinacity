# Pertinacity

#### Audio Recording Application

<img src="/App/src/main/resources/com/pertinacity/icon.png" style="width:200px" />

## Features
* Recording .wav audio files
* Pausing and resuming recording
* Assigning custom file names
  - Defaults to the date and time of recording
* Picking file save location
* Indication of state of application (Recording, Not Recording, Recording Paused)

## How To Run
### On Linux Systems
* Download latest Pertinacity.zip from the releases tab
* Extract the folder contents
* Double click on RunPertinacity.jar
* You may also download the entire repository; you will find the jar in the /dist/ folder
* If that does not work, navigate to the director and enter
> java -jar RunPertinacity.jar
* If that does not work, I'm afraid you are on your own
### [For Windows and MacOS](https://ubuntu.com/download/desktop)


## How to Build From Source
Gradle is required in order to run this program from source. 
* Navigate to the /App Directory
> ./gradlew run
