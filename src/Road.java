class Road {
  private int idCitySource;
  private int idCityDestination;

  public Road(int city1, int city2) {
    this.idCitySource = city1;
    this.idCityDestination = city2;
  }

  // Getters
  public int getIdCitySource() {
    return idCitySource;
  }

  public int getIdCityDestination() {
    return idCityDestination;
  }

  /*private City citySource;
  private City cityDestination;

  public Road(City city1, City city2) {
    this.citySource = city1;
    this.cityDestination = city2;
  }
  public City getCitySource() {
    return citySource;
  }

  public City getCityDestination() {
    return cityDestination;
  }*/
}