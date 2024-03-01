import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class Graph {

  HashMap<City,File> cities;

  HashMap<Road,File> roads;


  public Graph(HashMap<City,File> cities, HashMap<Road,File> roads) {
    this.cities = cities;
    this.roads = roads;
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
