class CityDistance implements Comparable<CityDistance> {
    City city;
    double distance;

    public CityDistance(City city, double distance) {
        this.city = city;
        this.distance = distance;
    }

    @Override
    public int compareTo(CityDistance other) {
        return Double.compare(this.distance, other.distance);
    }
}