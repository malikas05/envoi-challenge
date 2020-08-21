package model;

public class LatLonPartition {
    private double lat;
    private double lon;

    private LatLonPartition(Builder builder) {
        this.lat = builder.lat;
        this.lon = builder.lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LatLonPartition that = (LatLonPartition) o;

        if (Double.compare(that.lat, lat) != 0) return false;
        return Double.compare(that.lon, lon) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public static Builder build() {
        return new Builder();
    }

    public static class Builder {
        private double lat;
        private double lon;

        public Builder lat(double lat) {
            this.lat = lat;
            return this;
        }

        public Builder lon(double lon) {
            this.lon = lon;
            return this;
        }

        public LatLonPartition builder() {
            return new LatLonPartition(this);
        }
    }
}
