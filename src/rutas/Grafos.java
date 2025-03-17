package rutas;

import java.util.*;

public class Grafos {
    private final Map<String, List<Arista>> adjList = new HashMap<>();

    public Grafos() {
        inicializarGrafo();
    }

    private void inicializarGrafo() {
        addEdge("A", "B", 4);
        addEdge("A", "C", 8);
        addEdge("B", "D", 5);
        addEdge("B", "E", 10);
        addEdge("C", "F", 7);
        addEdge("D", "G", 6);
        addEdge("E", "H", 3);
        addEdge("F", "I", 9);
        addEdge("G", "J", 4);
        addEdge("H", "K", 2);
        addEdge("I", "L", 5);
        addEdge("J", "M", 7);
        addEdge("K", "M", 6);
        addEdge("L", "M", 8);
        addEdge("C", "D", 3);
        addEdge("E", "F", 4);
        addEdge("H", "I", 6);
        addEdge("J", "K", 2);
    }

    public void addEdge(String edificio1, String edificio2, int distancia) {
        adjList.putIfAbsent(edificio1, new ArrayList<>());
        adjList.putIfAbsent(edificio2, new ArrayList<>());
        adjList.get(edificio1).add(new Arista(edificio2, distancia));
        adjList.get(edificio2).add(new Arista(edificio1, distancia));
    }

    public List<String> dijkstra(String inicio, String destino) {
        Map<String, Integer> distancias = new HashMap<>();
        Map<String, String> previos = new HashMap<>();
        PriorityQueue<Arista> pq = new PriorityQueue<>(Comparator.comparingInt(Arista::getDistancia));

        for (String nodo : adjList.keySet()) {
            distancias.put(nodo, Integer.MAX_VALUE);
        }
        distancias.put(inicio, 0);
        pq.add(new Arista(inicio, 0));

        while (!pq.isEmpty()) {
            Arista actual = pq.poll();
            String nodo = actual.getDestino();
            int distancia = actual.getDistancia();

            if (distancia > distancias.getOrDefault(nodo, Integer.MAX_VALUE)) continue;

            for (Arista arista : adjList.get(nodo)) {
                int nuevaDist = distancia + arista.getDistancia();
                if (nuevaDist < distancias.getOrDefault(arista.getDestino(), Integer.MAX_VALUE)) {
                    distancias.put(arista.getDestino(), nuevaDist);
                    previos.put(arista.getDestino(), nodo);
                    pq.add(new Arista(arista.getDestino(), nuevaDist));
                }
            }
        }

        List<String> ruta = new ArrayList<>();
        for (String at = destino; at != null; at = previos.get(at)) {
            ruta.add(at);
        }
        Collections.reverse(ruta);
        return ruta.isEmpty() || !ruta.get(0).equals(inicio) ? Collections.emptyList() : ruta;
    }

    void addArista(String a, String b, int i) {
    }
}