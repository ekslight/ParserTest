import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

public class DataConverter {

    public static OutputFileData convert(InputFileData fileData) {
        long numberCount = Long.parseLong(fileData.getNumbersInLineCount());
        long linesCount = Long.parseLong(fileData.getLinesCount());

        OutputFileData result = new OutputFileData();
        if (linesCount <= fileData.getLines().size()) {

            for (int i = 0; i < fileData.getLines().size(); i++) {
                List<Long> numbersList = fileData.getLines().get(i).stream().map(Long::valueOf)
                                                 .collect(Collectors.toList());

                long totalLineSum = numbersList.stream().mapToLong(Long::longValue).sum();
                result.setTotalSum(result.getTotalSum() + totalLineSum);

                if (numberCount <= numbersList.size()) {
                    if (i < linesCount) {
                        LongSummaryStatistics statistics = numbersList.stream().limit(numberCount)
                                                                      .mapToLong(Long::longValue).summaryStatistics();
                        result.getStringList()
                              .add(createText(i + 1, statistics.getSum(), statistics.getMax(), statistics.getMin()));
                    }

                } else {
                    result.getStringList().add(createErrorText(numberCount, i + 1, numbersList));
                }
            }
        } else {
            result.getStringList()
                  .add(String.format("Error, найдено %d строки вместо %d", fileData.getLines().size(), linesCount));
        }

        result.getStringList().add("total=" + result.getTotalSum());
        return result;
    }

    private static String createErrorText(long numberCount, int lineNumber, List<Long> numbersList) {
        return new StringBuilder().append(lineNumber).append(": Error найдено ").append(numbersList.size())
                                  .append(" чисел вместо ").append(numberCount).toString();
    }

    private static String createText(int lineNumber, long sum, long max, long min) {
        return new StringBuilder().append(lineNumber).append(" : sum=").append(sum).append(" : max=").append(max)
                                  .append(" : min=").append(min).toString();
    }
}
