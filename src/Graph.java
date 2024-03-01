import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
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
        cities.add(new City(line));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    try (Scanner roadScanner = new Scanner(road)) {
      while (roadScanner.hasNextLine()) {
        String line = roadScanner.nextLine();
        roads.add(new Road(line));
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
