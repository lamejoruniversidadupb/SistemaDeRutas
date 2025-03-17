package rutas;
import java.util.List;
import java.util.Scanner;

public class Principal{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scanner = new Scanner(System.in);
        Grafos graph = new Grafos();
        
       
        graph.addEdge("A", "B", 1);
        graph.addEdge("B", "C", 1);
        graph.addEdge("C", "D", 1);
        graph.addEdge("C", "J", 1);
        graph.addEdge("J", "ZV", 1);
        graph.addEdge("J", "G", 1);
        graph.addEdge("J", "D", 1);
        graph.addEdge("D", "E", 1);
        graph.addEdge("D", "CF", 1);
        graph.addEdge("CF", "B", 1);
        graph.addEdge("E", "F", 1);
        graph.addEdge("E", "I", 1);
        graph.addEdge("E", "K", 1);
        graph.addEdge("E", "D", 1);
        graph.addEdge("F", "G", 1);
        graph.addEdge("F", "H", 1);
        graph.addEdge("G", "ZV", 1);
        graph.addEdge("H", "I", 1);
        graph.addEdge("I", "L", 1);
        graph.addEdge("L", "K", 1);
        graph.addEdge("K", "Porteria 2", 1);
        graph.addEdge("C", "Porteria 1", 1);
        graph.addEdge("ZV", "Porteria 1", 1);
        
       
        System.out.print("Ingrese su ubicación actual: ");
        String start = scanner.nextLine();
        System.out.print("Ingrese su destino: ");
        String end = scanner.nextLine();
        
        List<String> path = graph.dijkstra(start, end);
        System.out.println("Ruta más corta de " + start + " a " + end + ": " + path);
        
        scanner.close();
    }
}