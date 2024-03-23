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
        /*if (cityId1 >= 0 && cityId1 < cities.size() && cityId2 >= 0 && cityId2 < cities.size()) {
          roads.add(new Road(cities.get(cityId1), cities.get(cityId2)));
        } else {
          System.err.println("Invalid city IDs found in road data: " + cityId1 + ", " + cityId2);
        }*/
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }



    for (City cityFor : cities) {
      listeDadjacences.put(cityFor, new HashSet<>());

    }

    for (Road roadFor: roads) {
      City city1 = findCityById(roadFor.getIdCitySource());
      City city2 = findCityById(roadFor.getIdCityDestination());

//      City city1 = roadFor.getCitySource();
//      City city2 = roadFor.getCityDestination();

      listeDadjacences.get(city1).add(new Road(city1.getId(),city2.getId()));
      listeDadjacences.get(city2).add(new Road(city2.getId(),city1.getId()));
//      listeDadjacences.get(city1).add(roadFor);
//      listeDadjacences.get(city2).add(roadFor);
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

    while(!file.isEmpty()){
      City current = file.remove();

      for (Road road: listeDadjacences.get(current)) {
        //City neighbor = road.getCityDestination();
        City neighbor = findCityById(road.getIdCityDestination());

        if(!visited.containsKey(neighbor)){
          visited.put(neighbor, visited.get(current)+1);
          previousCity.put(neighbor, current);
          file.add(neighbor);
        }
      }
    }

    ArrayList<City> path = new ArrayList<>();
    City current = destination;
    while(current!=null){
      path.add(0,current);
      current = previousCity.get(current);
    }

    System.out.println("Trajet de " + depart + " à " + arrivee + ": " + (path.size()-1) + " routes et " + visited.get(current) + " km");
    for (int i = 0; i < path.size()-1 ; i++) {
      City city1 = path.get(i);
      City city2 = path.get(i+1);
      System.out.println(city1.getName() + " -> " + city2.getName() + " (" + Util.distance(city1.getLatitude(),city1.getLongitude(), city2.getLatitude(),city2.getLongitude()) + " km)");
    }
  }

  private City findCityByName(String name) {
    for (City city:cities) {
      if (city.getName().equals(name)){
        return city;
      }
    }
    return null;
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
    //HashMap<City, City> parent = new HashMap<>();

    City city = start;
    provisional.put(start,0.0);

    while (!finals.containsKey(end)){
      double countMin =Integer.MAX_VALUE;

      for (City cityFor : provisional.keySet()){
        if (provisional.get(cityFor) <countMin){
          countMin = provisional.get(cityFor);
          city = cityFor;
        }
      } finals.put(city,countMin);

      for (Road road : listeDadjacences.get(city)) {
        City sourceRoad = findCityById(road.getIdCitySource());
        City destinationRoad = findCityById(road.getIdCityDestination());
        double distance = countMin + Util.distance(sourceRoad.getLongitude(),sourceRoad.getLatitude(),destinationRoad.getLongitude(),destinationRoad.getLatitude());

         if (!finals.containsKey(destinationRoad) && (provisional.get(destinationRoad) == null ||(distance <provisional.get(destinationRoad)))) {
           visited.put(destinationRoad, road);
           provisional.put(destinationRoad, distance);
           //parent.put(destinationRoad, sourceRoad);
         }

      }


    }

  }

}/*


public void calculerItineraireMinimisantKm(String depart, String arrivee) {
    // Utilisation de l'algorithme de Dijkstra pour trouver le chemin le plus court
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

      if (distance > distances.get(current)) continue;

      for (Road road : adjacencyList.get(current)) {
        City neighbor = findCityById(road.getIdCityDestination());
        double newDistance = distance + Util.distance(current.getLatitude(), current.getLongitude(), neighbor.getLatitude(), neighbor.getLongitude());
        if (newDistance < distances.get(neighbor)) {
          distances.put(neighbor, newDistance);
          parent.put(neighbor, current);
          pq.offer(new CityDistance(neighbor, newDistance));
        }
      }
    }

    // Reconstruction de l'itinéraire
    ArrayList<City> itinerary = new ArrayList<>();
    City current = end;
    while (current != null) {
      itinerary.add(0, current);
      current = parent.get(current);
    }

    System.out.println("Trajet de " + depart + " à " + arrivee + ": " + (itinerary.size() - 1) + " routes et " + distances.get(end) + " kms");
    for (int i = 0; i < itinerary.size() - 1; i++) {
      City city1 = itinerary.get(i);
      City city2 = itinerary.get(i + 1);
      System.out.println(city1.getName() + " -> " + city2.getName() + " (" + Util.distance(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(), city2.getLongitude()) + " km)");
    }
  }
  private City findCityByName(String name) {
    for (City city : cities) {
      if (city.getName().equals(name)) {
        return city;
      }
    }
    return null;
  }
  private City findCityById(int id) {
    for (City city : cities) {
      if (city.getId() == id) {
        return city;
      }
    }
    return null;
  }
}










import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Graph {
  private ArrayList<City> cities;
  private ArrayList<Road> roads;
  private Map<City, Set<Road>> adjacencyList;

  public Graph(File city, File road) {
    this.cities = new ArrayList<>();
    this.roads = new ArrayList<>();
    this.adjacencyList = new HashMap<>();

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
    buildAdjacencyList();
  }
  private void buildAdjacencyList() {
    for (City city : cities) {
      adjacencyList.put(city, new HashSet<>());
    }

    for (Road road : roads) {
      City city1 = findCityById(road.getIdCitySource());
      City city2 = findCityById(road.getIdCityDestination());

      adjacencyList.get(city1).add(new Road(city1.getId(), city2.getId()));
      adjacencyList.get(city2).add(new Road(city2.getId(), city1.getId()));
    }
  }

  public void calculerItineraireMinimisantNombreRoutes(String depart, String arrivee) {
    City start = findCityByName(depart);
    City end = findCityByName(arrivee);

    if (start == null || end == null) {
      throw new IllegalArgumentException("Ville de départ ou d'arrivée non trouvée");
    }

    Queue<City> queue = new LinkedList<>();
    Map<City, Integer> path = new HashMap<>();
    Map<City, City> parent = new HashMap<>();
    queue.offer(start);
    path.put(start, 0);

    while (!queue.isEmpty()) {
      City current = queue.poll();

      for (Road road : adjacencyList.get(current)) {
        City neighbor = findCityById(road.getIdCityDestination());
        if (!path.containsKey(neighbor)) {
          path.put(neighbor, path.get(current) + 1);
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

    System.out.println("Trajet de " + depart + " à " + arrivee + ": " + (itinerary.size() - 1) + " routes");
    for (int i = 0; i < itinerary.size() - 1; i++) {
      City city1 = itinerary.get(i);
      City city2 = itinerary.get(i + 1);
      System.out.println(city1.getName() + " -> " + city2.getName() + " (" + Util.distance(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(), city2.getLongitude()) + " km)");
    }
  }

  public void calculerItineraireMinimisantKm(String depart, String arrivee) {
    // Utilisation de l'algorithme de Dijkstra pour trouver le chemin le plus court
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

      if (distance > distances.get(current)) continue;

      for (Road road : adjacencyList.get(current)) {
        City neighbor = findCityById(road.getIdCityDestination());
        double newDistance = distance + Util.distance(current.getLatitude(), current.getLongitude(), neighbor.getLatitude(), neighbor.getLongitude());
        if (newDistance < distances.get(neighbor)) {
          distances.put(neighbor, newDistance);
          parent.put(neighbor, current);
          pq.offer(new CityDistance(neighbor, newDistance));
        }
      }
    }

    // Reconstruction de l'itinéraire
    ArrayList<City> itinerary = new ArrayList<>();
    City current = end;
    while (current != null) {
      itinerary.add(0, current);
      current = parent.get(current);
    }

    System.out.println("Trajet de " + depart + " à " + arrivee + ": " + (itinerary.size() - 1) + " routes et " + distances.get(end) + " kms");
    for (int i = 0; i < itinerary.size() - 1; i++) {
      City city1 = itinerary.get(i);
      City city2 = itinerary.get(i + 1);
      System.out.println(city1.getName() + " -> " + city2.getName() + " (" + Util.distance(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(), city2.getLongitude()) + " km)");
    }
  }
  private City findCityByName(String name) {
    for (City city : cities) {
      if (city.getName().equals(name)) {
        return city;
      }
    }
    return null;
  }
  private City findCityById(int id) {
    for (City city : cities) {
      if (city.getId() == id) {
        return city;
      }
    }
    return null;
  }
}*/
