import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainClass {

    private static final Logger LOGGER = Logger.getGlobal();
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("in(?<fileNumber>[0-9]+)\\.txt");
    private static final String ROOT_PATH_NAME = "files";

    static {
        try {
            FileHandler handler = new FileHandler("logs/log", 1024 * 1024, 5, true);
            handler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(handler);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Cannot create FileHandler", e);
        }
    }

    public static void main(String[] args) {
        LOGGER.log(Level.INFO, "Application started");

        String pathName = ROOT_PATH_NAME;
        if (args.length > 0 && !args[0].substring(1).isEmpty()) {
            pathName = args[0].substring(1);
        }

        parseFilesInDirectory(pathName);

        LOGGER.log(Level.INFO, "Application stopped");
    }

    private static boolean isApplicableData(InputFileData fileData) {
        return fileData.getLinesCount() != null
                && fileData.getLinesCount().matches("[0-9]+")
                && fileData.getNumbersInLineCount() != null
                && fileData.getNumbersInLineCount().matches("[0-9]+")
                && fileData.getLines().stream().flatMap(Collection::stream).allMatch(s -> s.matches("-?[0-9]+"));
    }

    private static void parseFilesInDirectory(String dirName) {
        Map<OutputFileData, String> dataMap = new HashMap<>();

        File[] files = new File(dirName).listFiles();
        if (files != null) {
            Stream.of(files).filter(Objects::nonNull).forEach(file -> {
                Matcher matcher = FILE_NAME_PATTERN.matcher(file.getName());

                if (matcher.find()) {
                    String fileNumber = matcher.group("fileNumber");
                    InputFileData fileData = FileHelper.parseFile(file);

                    if (isApplicableData(fileData)) {
                        OutputFileData outputFileData = DataConverter.convert(fileData);
                        String dstFileName = String.format("out%s.txt", fileNumber);
                        dataMap.put(outputFileData, dstFileName);

                        if (!outputFileData.getStringList().isEmpty()) {
                            FileHelper.writeToFile(outputFileData.getStringList(), new File(dirName, dstFileName));
                        }

                    } else {
                        LOGGER.log(Level.WARNING, String.format("File %s has invalid format", file.getName()));
                    }
                }
            });

            List<String> list = dataMap.entrySet().stream()
                                       .sorted((e1, e2) -> (int) (e2.getKey().getTotalSum() - e1.getKey()
                                                                                                .getTotalSum()))
                                       .map(Map.Entry::getValue).collect(Collectors.toList());

            if (!list.isEmpty()) {
                FileHelper.writeToFile(list, new File(dirName, "result.txt"));
            }
        }
    }
}
