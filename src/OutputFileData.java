import java.util.ArrayList;
import java.util.List;

public class OutputFileData {

    private final List<String> stringList = new ArrayList<>();
    private long totalSum = 0;

    public long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(long totalSum) {
        this.totalSum = totalSum;
    }

    public List<String> getStringList() {

        return stringList;
    }

}
