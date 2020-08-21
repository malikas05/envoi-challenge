package model;

public class Address {
    private String street;
    private String city;
    private String province;
    private String country;
    private String postalCode;

    private Address(Builder builder) {
        this.street = builder.street;
        this.city = builder.city;
        this.province = builder.province;
        this.country = builder.country;
        this.postalCode = builder.postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String toString() {
        return this.street
                + ","
                + this.city
                + ","
                + this.province
                + ","
                + this.postalCode
                + (!this.country.isEmpty() ? "," + this.country : "");
    }

    public static Builder build() {
        return new Builder();
    }

    public static class Builder {
        private String street;
        private String city;
        private String province;
        private String country;
        private String postalCode;

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder province(String province) {
            this.province = province;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Address builder() {
            return new Address(this);
        }
    }
}
