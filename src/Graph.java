import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


class Graph {

  private ArrayList<City> cities;

  private ArrayList<Road> roads;


  public Graph(File city,File road) {

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

        cities.add(new City(id,nom, longitude,latitude));
      }
    } catch (RuntimeException | FileNotFoundException e) {
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

  // Méthode pour calculer l'itinéraire minimisant le nombre de routes
  public void calculerItineraireMinimisantNombreRoutes(String depart, String arrivee) {
    // À implémenter
  }

  // Méthode pour calculer l'itinéraire minimisant les kilomètres
  public void calculerItineraireMinimisantKm(String depart, String arrivee) {
    // À implémenter
  }
}
