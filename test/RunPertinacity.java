public class RunPertinacity {
    public static void main(String[] args) {
        String[] command = {"res/jdk-with-fx/bin/java", "-jar", "res/pertinacity.jar"};

        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}