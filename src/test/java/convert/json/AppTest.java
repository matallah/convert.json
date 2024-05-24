package convert.json;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    private static final String TEST_INPUT_DIR = "src/test/resources/";
    private static final String TEST_OUTPUT_FILE = "output1.json";

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_OUTPUT_FILE));
    }

    @Test
    public void testDSVToJSONConversion() throws IOException {
        String inputFilePath = TEST_INPUT_DIR + "DSV input 1.txt";
        char delimiter = ',';
        String[] args = {inputFilePath, String.valueOf(delimiter)};

        Main.main(args);

        Path outputPath = Paths.get(TEST_OUTPUT_FILE);
        assertTrue(Files.exists(outputPath));

        try (BufferedReader reader = new BufferedReader(new FileReader(outputPath.toFile()))) {
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                assertTrue(line.contains("firstName"));
                assertTrue(line.contains("lastName"));
            }
            assertEquals(3, lineCount);
        }
    }

}
