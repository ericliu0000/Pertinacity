import java.nio.file.Paths;

public class RunPertinacity {
    public static void main(String[] args) {
        String[] command = {Paths.get("res", "jdk-with-fx", "bin", "java").toString(), "-jar", Paths.get("res", "pertinacity.jar").toString()};

        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}