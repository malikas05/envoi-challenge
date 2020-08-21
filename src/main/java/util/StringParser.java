package util;

import model.Address;
import model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class StringParser {
    private static final Logger logger = LoggerFactory.getLogger(StringParser.class);

    private StringParser() {
    }

    public static Order parseString(@Nonnull String row) {
        if (row.isEmpty()) {
            return null;
        }

        try {
            int firstIndexToParse = row.indexOf("\"") - 1;
            String customerName = row.substring(0, firstIndexToParse);

            int secondIndexToParse = row.indexOf("\"", firstIndexToParse + 2);
            String[] fullAddressDetails = row.substring(firstIndexToParse + 2, secondIndexToParse).trim().split(",");
            Address address = Address.build()
                    .street(fullAddressDetails.length > 0 ? fullAddressDetails[0].trim() : "")
                    .city(fullAddressDetails.length > 1 ? fullAddressDetails[1].trim() : "")
                    .province(fullAddressDetails.length > 2 ? fullAddressDetails[2].trim() : "")
                    .postalCode(fullAddressDetails.length > 3 ? fullAddressDetails[3].trim() : "")
                    .country(fullAddressDetails.length > 4 ? fullAddressDetails[4].trim() : "")
                    .builder();

            String[] latLon = row.substring(row.indexOf(",", secondIndexToParse) + 1).split(",");
            double lat = Double.parseDouble(latLon[0]);
            double lon = Double.parseDouble(latLon[1]);

            return Order.build()
                    .customerName(customerName)
                    .customerAddress(address)
                    .lat(lat)
                    .lon(lon)
                    .builder();
        } catch (Exception e) {
            logger.error("Could not parse this row due to invalid format: {}", row);
        }

        return null;
    }
}
