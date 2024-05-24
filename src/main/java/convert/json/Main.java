package convert.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    //"DSV input 1.txt" ","
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final List<String> DATE_PATTERNS = Arrays.asList(
            "yyyy-MM-dd",
            "MM/dd/yyyy",
            "dd-MM-yyyy",
            "dd/MM/yyyy",
            "yyyy/MM/dd"
            // Add more patterns as needed
    );

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java -jar dsv-to-json.jar <inputfile> <delimiter>");
            System.exit(1);
        }
        String inputFile = args[0];
        char delimiter = args[1].charAt(0);

        try (FileReader fileReader = new FileReader(inputFile);
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output1.json"))) {

            Iterable<CSVRecord> records = CSVFormat.newFormat(delimiter)
                    .withFirstRecordAsHeader()
                    .withQuote('"')
                    .parse(fileReader);
            Iterator<CSVRecord> iterator = records.iterator();
            ObjectMapper objectMapper = new ObjectMapper();
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (iterator.hasNext()) {
                CSVRecord record = iterator.next();
                ObjectNode jsonNode = objectMapper.createObjectNode();

                for (Map.Entry<String, String> entry : record.toMap().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (value != null && !value.isEmpty()) {
                        boolean isDate = false;
                        for (String pattern : DATE_PATTERNS) {
                            try {
                                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
                                LocalDate date = LocalDate.parse(value, dateFormatter);
                                jsonNode.put(key, date.format(outputFormatter));
                                isDate = true;
                                break;
                            } catch (DateTimeParseException e) {
                                // Not a valid date for this pattern
                            }
                        }

                        if (!isDate) {
                            try {
                                int intValue = Integer.parseInt(value);
                                jsonNode.put(key, intValue);
                            } catch (NumberFormatException e1) {
                                try {
                                    double doubleValue = Double.parseDouble(value);
                                    jsonNode.put(key, doubleValue);
                                } catch (NumberFormatException e2) {
                                    jsonNode.put(key, value);
                                }
                            }
                        }
                    }
                }

                bufferedWriter.write(objectMapper.writeValueAsString(jsonNode));
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error processing the file", e);
        }
    }
}