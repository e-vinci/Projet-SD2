import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Graph {

  private ArrayList<City> cities;
  private ArrayList<Road> roads;

  public Graph(File city, File road) {
    this.cities = new ArrayList<City>();
    this.roads = new ArrayList<Road>();

    try (Scanner cityScanner = new Scanner(city)) {
      while (cityScanner.hasNextLine()) {
        String line = cityScanner.nextLine();
        String[] attributCity = line.split(",");
        int id = Integer.parseInt(attributCity[0]);
        String nom = attributCity[1];
        double longitude = Double.parseDouble(attributCity[2]);
        double latitude = Double.parseDouble(attributCity[3]);
        cities.add(new City(id, nom, longitude, latitude));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    try (Scanner roadScanner = new Scanner(road)) {
      while (roadScanner.hasNextLine()) {
        String line = roadScanner.nextLine();
        String[] attributRoad = line.split(",");
        int cityId1 = Integer.parseInt(attributRoad[0]);
        int cityId2 = Integer.parseInt(attributRoad[1]);
        roads.add(new Road(cityId1, cityId2));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void calculerItineraireMinimisantNombreRoutes(String depart, String arrivee) {
    City start = findCityByName(depart);
    City end = findCityByName(arrivee);

    if (start == null || end == null) {
      throw new IllegalArgumentException("Ville de départ ou d'arrivée non trouvée");
    }

    Queue<City> queue = new LinkedList<>();
    HashMap<City, Integer> routeCount = new HashMap<>();
    HashMap<City, City> parent = new HashMap<>();
    queue.offer(start);
    routeCount.put(start, 0);

    while (!queue.isEmpty()) {
      City current = queue.poll();

      for (City neighbor : getNeighbors(current)) {
        if (!routeCount.containsKey(neighbor)) {
          routeCount.put(neighbor, routeCount.get(current) + 1);
          parent.put(neighbor, current);
          queue.offer(neighbor);
        }
      }
    }

    ArrayList<City> itinerary = new ArrayList<>();
    City current = end;
    while (current != null) {
      itinerary.add(0, current);
      current = parent.get(current);
    }

    System.out.println(
        "Trajet de " + depart + " à " + arrivee + ": " + (routeCount.get(end)) + " routes et "
            + calculateTotalDistance(itinerary) + " kms");
    printItinerary(itinerary);
  }

  public void calculerItineraireMinimisantKm(String depart, String arrivee) {
    City start = findCityByName(depart);
    City end = findCityByName(arrivee);

    if (start == null || end == null) {
      throw new IllegalArgumentException("Ville de départ ou d'arrivée non trouvée");
    }

    PriorityQueue<CityDistance> pq = new PriorityQueue<>();
    HashMap<City, Double> distances = new HashMap<>();
    HashMap<City, City> parent = new HashMap<>();

    for (City city : cities) {
      distances.put(city, Double.MAX_VALUE);
    }

    pq.offer(new CityDistance(start, 0.0));
    distances.put(start, 0.0);

    while (!pq.isEmpty()) {
      CityDistance cd = pq.poll();
      City current = cd.city;
      double distance = cd.distance;

      if (distance > distances.get(current)) {
        continue;
      }

      for (City neighbor : getNeighbors(current)) {
        double newDistance = distance + Util.distance(current.getLatitude(), current.getLongitude(),
            neighbor.getLatitude(), neighbor.getLongitude());
        if (newDistance < distances.get(neighbor)) {
          distances.put(neighbor, newDistance);
          parent.put(neighbor, current);
          pq.offer(new CityDistance(neighbor, newDistance));
        }
      }
    }

    ArrayList<City> itinerary = new ArrayList<>();
    City current = end;
    while (current != null) {
      itinerary.add(0, current);
      current = parent.get(current);
    }

    double totalDistance = distances.get(end);
    System.out.println("Trajet de " + depart + " à " + arrivee + ": " + (itinerary.size() - 1) +
        " routes et " + totalDistance + " kms");

    for (int i = 0; i < itinerary.size() - 1; i++) {
      City city1 = itinerary.get(i);
      City city2 = itinerary.get(i + 1);
      double distanceBetweenCities = Util.distance(city1.getLatitude(), city1.getLongitude(),
          city2.getLatitude(), city2.getLongitude());

      System.out.println(city1.getName() + " -> " + city2.getName() + " (" + String.format("%.2f",
          distanceBetweenCities) + " km)");
    }
  }


  private City findCityByName(String name) {
    for (City city : cities) {
      if (city.getName().equalsIgnoreCase(name)) {
        return city;
      }
    }
    return null;
  }

  private ArrayList<City> getNeighbors(City city) {
    ArrayList<City> neighbors = new ArrayList<>();
    for (Road road : roads) {
      if (road.getIdCitySource() == city.getId()) {
        neighbors.add(findCityById(road.getIdCityDestination()));
      } else if (road.getIdCityDestination() == city.getId()) {
        neighbors.add(findCityById(road.getIdCitySource()));
      }
    }
    return neighbors;
  }

  private City findCityById(int id) {
    for (City city : cities) {
      if (city.getId() == id) {
        return city;
      }
    }
    return null;
  }

  private double calculateTotalDistance(List<City> itinerary) {
    double totalDistance = 0;
    for (int i = 0; i < itinerary.size() - 1; i++) {
      City city1 = itinerary.get(i);
      City city2 = itinerary.get(i + 1);
      totalDistance += Util.distance(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(),
          city2.getLongitude());
    }
    return totalDistance;
  }

  private void printItinerary(List<City> itinerary) {
    for (int i = 0; i < itinerary.size() - 1; i++) {
      City city1 = itinerary.get(i);
      City city2 = itinerary.get(i + 1);
      double distanceBetweenCities = Util.distance(city1.getLatitude(), city1.getLongitude(),
          city2.getLatitude(), city2.getLongitude());

      System.out.println(city1.getName() + " -> " + city2.getName() + " (" + String.format("%.2f",
          distanceBetweenCities) + " km)");
    }
  }
}