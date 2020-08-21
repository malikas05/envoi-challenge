package util.impl;

import model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reader;
import util.StringParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvReader implements Reader<Order> {
    private static final Logger logger = LoggerFactory.getLogger(CsvReader.class);

    public List<Order> readFile(String pathToFile) {
        logger.info("Started reading CSV file: {}.", pathToFile);
        long start = System.currentTimeMillis();
        List<Order> orders = new ArrayList<>();

        Path path = Paths.get("src/main/resources/" + pathToFile);
        if (Files.exists(path)) {
            try (BufferedReader csvReader = new BufferedReader(new FileReader(path.toFile()))) {
                csvReader.readLine();
                String row;
                while ((row = csvReader.readLine()) != null) {
                    Order order = StringParser.parseString(row);
                    if (order != null)
                        orders.add(order);
                }
            } catch (FileNotFoundException e) {
                logger.error("File {} not found.", pathToFile);
                throw new RuntimeException(e);
            } catch (IOException e) {
                logger.error("Something wrong has occurred while reading the file {}.", pathToFile);
                throw new RuntimeException(e);
            }
        }

        logger.info("Finished reading CSV file in {}ms; Total number of rows read: {}.", System.currentTimeMillis() - start, orders.size());
        return orders;
    }
}
