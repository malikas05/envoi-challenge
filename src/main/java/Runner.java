import service.OrderPartitionsService;
import util.impl.CsvReader;

public class Runner {
    public static void main(String[] args) {
        OrderPartitionsService orderPartitionsService = new OrderPartitionsService(new CsvReader(), "Dataset.csv");
        orderPartitionsService.runOrderPartitioner();
    }
}
