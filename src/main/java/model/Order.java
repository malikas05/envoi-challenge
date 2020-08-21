package model;

public class Order {
    private String customerName;
    private Address customerAddress;
    private double lat;
    private double lon;

    private Order(Builder builder) {
        this.customerName = builder.customerName;
        this.customerAddress = builder.customerAddress;
        this.lat = builder.lat;
        this.lon = builder.lon;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Address getCustomerAddress() {
        return customerAddress;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public static Builder build() {
        return new Builder();
    }

    public static class Builder {
        private String customerName;
        private Address customerAddress;
        private double lat;
        private double lon;

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder customerAddress(Address customerAddress) {
            this.customerAddress = customerAddress;
            return this;
        }

        public Builder lat(double lat) {
            this.lat = lat;
            return this;
        }

        public Builder lon(double lon) {
            this.lon = lon;
            return this;
        }

        public Order builder() {
            return new Order(this);
        }
    }
}
