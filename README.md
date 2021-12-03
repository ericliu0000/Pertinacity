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

## How to Build From Source
All build steps are executed using the Gradle wrapper, gradlew. Each target that Gradle can build is referred to as a task. The most common Gradle task to use is build. This will build all the outputs created by WPILib. To run, open a console and cd into the cloned WPILib directory. Then:

>./gradlew build

To build a specific subproject, such as WPILibC, you must access the subproject and run the build task only on that project. Accessing a subproject in Gradle is quite easy. Simply use :subproject_name:task_name with the Gradle wrapper. For example, building just WPILibC:

>./gradlew :wpilibc:build

The gradlew wrapper only exists in the root of the main project, so be sure to run all commands from there. All of the subprojects have build tasks that can be run. Gradle automatically determines and rebuilds dependencies, so if you make a change in the HAL and then run ./gradlew :wpilibc:build, the HAL will be rebuilt, then WPILibC.

There are a few tasks other than build available. To see them, run the meta-task tasks. This will print a list of all available tasks, with a description of each task.

