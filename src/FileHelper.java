import java.io.*;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHelper {
    private static final Logger LOGGER = Logger.getGlobal();

    private FileHelper() {
    }

    public static InputFileData parseFile(File file) {
        InputFileData data = new InputFileData();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            if (reader.ready()) {
                data.setLinesCount(reader.readLine());
            }

            if (reader.ready()) {
                data.setNumbersInLineCount(reader.readLine());
            }

            reader.lines()
                  .map(line -> Stream.of(line.split(" ")).filter(s -> !s.isEmpty()).collect(Collectors.toList()))
                  .collect(Collectors.toCollection(data::getLines));
            LOGGER.log(Level.INFO, String.format("File %s successfully parsed", file.getName()));

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Exception while reading file", e);
        }

        return data;
    }

    public static void writeToFile(Collection<String> data, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            try {
                for (String s : data) {
                    writer.write(s);
                    writer.newLine();
                }
                writer.flush();
                LOGGER.log(Level.INFO, String.format("Created output file %s", file.getName()));
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception while writing to the file", e);
            }

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Exception while creating file", e);
        }
    }
}
