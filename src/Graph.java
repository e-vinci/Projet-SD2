import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Graph {
  private ArrayList<City> cities;
  private ArrayList<Road> roads;

  private Map<City, Set<Road>> listeDadjacences;

  public Graph(File city, File road) {
    this.cities = new ArrayList<City>();
    this.roads = new ArrayList<Road>();
    this.listeDadjacences = new HashMap<>();

    try (Scanner cityScanner = new Scanner(city)) {
      while (cityScanner.hasNextLine()) {
        String line = cityScanner.nextLine();
        String[] attributCity = line.split(",");
        int id = Integer.parseInt(attributCity[0]);
        String nom = attributCity[1];
        double longitude = Double.parseDouble(attributCity[2]);
        double latitude = Double.parseDouble(attributCity[3]);
        City newCity = new City(id, nom, longitude, latitude);
        cities.add(newCity);
        listeDadjacences.put(newCity,new HashSet<>()); // Initialiser un ensemble vide pour chaque ville
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
        Road newRoad = new Road(cityId1, cityId2);
        roads.add(newRoad);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    for (Road roadFor: roads) {
      City city1 = findCityById(roadFor.getIdCitySource());
      City city2 = findCityById(roadFor.getIdCityDestination());
      listeDadjacences.get(city1).add(new Road(city1.getId(),city2.getId()));
      listeDadjacences.get(city2).add(new Road(city2.getId(),city1.getId()));
    }
  }

  private City findCityById(int idCitySource) {
    for (City city: cities) {
      if (city.getId() == idCitySource) {
        return city;
      }
    }
    return null;
  }

  private City findCityByName(String name) {
    for (City city:cities) {
      if (city.getName().equals(name)){
        return city;
      }
    }
    return null;
  }

  public void calculerItineraireMinimisantNombreRoutes(String depart, String arrivee) {
    City source = findCityByName(depart);
    City destination = findCityByName(arrivee);

    if (source == null || destination == null){
      throw new IllegalArgumentException("Ville de départ ou d'arrivée null");
    }

    Queue<City> file = new LinkedList<>();
    Map<City, Integer> visited = new HashMap<>();
    Map<City,City> previousCity = new HashMap<>();
    file.add(source);
    visited.put(source,0);


    while (!file.isEmpty()) {
      City current = file.remove();

      for (Road road : listeDadjacences.get(current)) {
        int neighborId;
        if (road.getIdCitySource() == current.getId()) {
          neighborId = road.getIdCityDestination();
        } else {
          neighborId = road.getIdCitySource();
        }
        City neighbor = findCityById(neighborId);
        if (!visited.containsKey(neighbor)) {
          visited.put(neighbor, visited.get(current) + 1);
          previousCity.put(neighbor, current);
          file.add(neighbor);
        }
      }
    }

    ArrayList<City> itinerary = new ArrayList<>();
    City current = destination;
    while (current != null) {
      itinerary.add(0, current);
      current = previousCity.get(current);
    }

    double totalDistance = 0;
    for (int i = 0; i < itinerary.size() - 1; i++) {
      City city1 = itinerary.get(i);
      City city2 = itinerary.get(i + 1);
      totalDistance += Util.distance(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(),
              city2.getLongitude());
    }

    System.out.println(
            "Trajet de " + depart + " à " + arrivee + ": " + (visited.get(destination)) + " routes et "
                    + totalDistance + " kms");

    for (int i = 0; i < itinerary.size() - 1; i++) {
      City city1 = itinerary.get(i);
      City city2 = itinerary.get(i + 1);
      double distanceBetweenCities = Util.distance(city1.getLatitude(), city1.getLongitude(),
              city2.getLatitude(), city2.getLongitude());

      System.out.println(city1.getName() + " -> " + city2.getName() + " (" + String.format("%.2f",
              distanceBetweenCities) + " km)");
    }
  }



  public void calculerItineraireMinimisantKm(String depart, String arrivee) {
    City start = findCityByName(depart);
    City end = findCityByName(arrivee);

    if (start == null || end == null){
      throw new IllegalArgumentException("Ville de départ ou d'arrivée null");
    }

    Map<City, Double> provisional = new HashMap<>();
    Map<City, Double> finals = new HashMap<>();
    Map<City,Road> visited = new HashMap<>();
    Map<City,City> previous = new HashMap<>();

    City current = start;
    provisional.put(start, 0.0);

    while (!finals.containsKey(end)) {
      double minDistance = Double.MAX_VALUE;
      for (City cityFor : provisional.keySet()) {
        if (provisional.get(cityFor) < minDistance && !finals.containsKey(cityFor)) {
          minDistance = provisional.get(cityFor);
          current = cityFor;
        }
      }

      finals.put(current, minDistance);

      for (Road road : listeDadjacences.get(current)) {
        City nextCity = findCityById(road.getIdCityDestination());
        double distance = minDistance + Util.distance(current.getLongitude(), current.getLatitude(), nextCity.getLongitude(), nextCity.getLatitude());

        if (!finals.containsKey(nextCity) && (provisional.get(nextCity) == null || distance < provisional.get(nextCity))) {
          visited.put(nextCity, road);
          provisional.put(nextCity, distance);
          previous.put(nextCity, current);
        }
      }
    }



    ArrayList<City> path = new ArrayList<>();
    City finalCity = end;
    while (finalCity != null) {
      path.add(0, finalCity);
      finalCity = previous.get(finalCity);
    }

    double totalDistance = 0.0;
    for (int i = 0; i < path.size() - 1; i++) {
      City city1 = path.get(i);
      City city2 = path.get(i + 1);
      totalDistance += Util.distance(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(), city2.getLongitude());
    }

    System.out.println("Trajet de " + depart + " à " + arrivee + ": " + (path.size() - 1) + " routes et " + String.format("%.2f", totalDistance) + " km");
    for (int i = 0; i < path.size() - 1; i++) {
      City city1 = path.get(i);
      City city2 = path.get(i + 1);
      System.out.println(city1.getName() + " -> " + city2.getName() + " (" + String.format("%.2f", Util.distance(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(), city2.getLongitude())) + " km)");
    }

  }

}