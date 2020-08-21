package service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import model.LatLonPartition;
import model.Order;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DistanceCalculator;
import util.Reader;

import javax.annotation.Nonnull;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class OrderPartitionsService {
    private static final Logger logger = LoggerFactory.getLogger(OrderPartitionsService.class);

    private static final int RADIUS_IN_KM = 10;

    private Reader<Order> fileReader;
    private String fileName;
    private Map<String, Map<LatLonPartition, List<Order>>> partitionsByCities;

    public OrderPartitionsService(@Nonnull Reader<Order> fileReader, @Nonnull String fileName) {
        this.fileReader = Preconditions.checkNotNull(fileReader, "FileReader cannot be null!");
        this.fileName = Preconditions.checkNotNull(fileName, "FileName cannot be null!");
        partitionsByCities = new TreeMap<>();
    }

    public void runOrderPartitioner() {
        logger.info("Started OrderPartitionsService.");
        long start = System.currentTimeMillis();

        List<Order> listOfOrders = retrieveOrders(fileName);
        listOfOrders.forEach(order -> {
            partitionsByCities.compute(order.getCustomerAddress().getCity().toLowerCase(), (k, v) -> {
                if (v == null) {
                    Map<LatLonPartition, List<Order>> partitionsForSingleCity = new HashMap<>();
                    partitionsForSingleCity.put(LatLonPartition.build()
                            .lat(order.getLat())
                            .lon(order.getLon())
                            .builder(), Lists.newArrayList(order));

                    return partitionsForSingleCity;
                }

                AtomicBoolean newPartition = new AtomicBoolean(true);
                Map<LatLonPartition, List<Order>> partitionsForSingleCity = v;
                partitionsForSingleCity.forEach(((latLonPartition, orders) -> {
                    if (DistanceCalculator.calculateDistanceInKilometer(latLonPartition.getLat(), latLonPartition.getLon(), order.getLat(), order.getLon()) < RADIUS_IN_KM) {
                        orders.add(order);
                        newPartition.set(false);
                        return;
                    }
                }));

                if (newPartition.get()) {
                    partitionsForSingleCity.put(LatLonPartition.build()
                            .lat(order.getLat())
                            .lon(order.getLon())
                            .builder(), Lists.newArrayList(order));
                }
                return partitionsForSingleCity;
            });
        });

        logger.info("Finished OrderPartitionsService in {}ms.", System.currentTimeMillis() - start);
        displayPartitions();
    }

    private List<Order> retrieveOrders(String fileName) {
        return fileReader.readFile(fileName);
    }

    private void displayPartitions() {
        if (partitionsByCities.isEmpty()) {
            return;
        }

        try {
            FileWriter out = new FileWriter("src/main/resources/Partitions.csv");
            try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
                for (Map.Entry<String, Map<LatLonPartition, List<Order>>> partitionsForSingleCity : partitionsByCities.entrySet()) {
                    // Printing city
                    printer.printRecord(partitionsForSingleCity.getKey().toUpperCase());

                    for (Map.Entry<LatLonPartition, List<Order>> partitionList : partitionsForSingleCity.getValue().entrySet()) {
                        // Printing single partition
                        printer.printRecord(partitionList.getKey().getLat() + ":" + partitionList.getKey().getLon());

                        for (Order order : partitionList.getValue()) {
                            // Printing single order
                            printer.printRecord(order.getCustomerName() + "," + order.getCustomerAddress() + "," + order.getLat() + "," + order.getLon());
                        }
                        printer.println();
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Something wrong has occurred while writing into the file.");
        }
    }
}
