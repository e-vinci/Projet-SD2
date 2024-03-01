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
}