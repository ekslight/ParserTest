import java.util.ArrayList;
import java.util.List;

public class InputFileData {

    private final List<List<String>> lines = new ArrayList<>();
    private String linesCount;
    private String numbersInLineCount;

    public List<List<String>> getLines() {
        return lines;
    }

    public String getLinesCount() {
        return linesCount;
    }

    public void setLinesCount(String linesCount) {
        this.linesCount = linesCount;
    }

    public String getNumbersInLineCount() {
        return numbersInLineCount;
    }

    public void setNumbersInLineCount(String numbersInLineCount) {
        this.numbersInLineCount = numbersInLineCount;
    }
}
