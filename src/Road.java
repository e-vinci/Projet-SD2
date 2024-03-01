import java.util.*;
class Road {
  private City city1;
  private City city2;

  public Road(City city1, City city2) {
    this.city1 = city1;
    this.city2 = city2;
  }

  // Getters
  public City getCity1() {
    return city1;
  }

  public City getCity2() {
    return city2;
  }
}