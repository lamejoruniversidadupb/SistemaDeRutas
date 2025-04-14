package rutas;

import java.util.*;

public class Grafos {
    private final Map<String, List<Arista>> adjList = new HashMap<>();
    private final Map<String, Nodo> nodos = new HashMap<>();
    private final List<Arista> aristas = new ArrayList<>();

    public Grafos() {
        inicializarNodos();
        inicializarGrafo();
    }

    private void inicializarNodos() {
        nodos.put("A", new Nodo("A", 20, 250));
        nodos.put("B", new Nodo("B", 60, 270));
        nodos.put("C", new Nodo("C", 100, 290));
        nodos.put("CAFETERIA", new Nodo("CAFETERIA", 60, 200)); 
        nodos.put("D", new Nodo("D", 160, 250));
        nodos.put("E", new Nodo("E", 220, 200));
        nodos.put("F", new Nodo("F", 280, 180));
        nodos.put("G", new Nodo("G", 260, 250));
        nodos.put("H", new Nodo("H", 280, 120));
        nodos.put("I", new Nodo("I", 240, 60));
        nodos.put("J", new Nodo("J", 180, 320));
        nodos.put("K", new Nodo("K", 160, 100));
        nodos.put("L", new Nodo("L", 300, 40));
        nodos.put("PORTERIA", new Nodo("PORTERIA", 140, 360));
    }

    private void inicializarGrafo() {
        conectar("A", "B", 10);
        conectar("B", "C", 10);
        conectar("B", "CAFETERIA", 15); 
        conectar("CAFETERIA", "C", 10); 
        conectar("CAFETERIA", "D", 30); 
        conectar("C", "PORTERIA", 30);
        conectar("PORTERIA", "J", 30);
        conectar("J", "D", 40);
        conectar("D", "E", 40);
        conectar("D", "G", 25);
        conectar("D", "K", 15); 
        conectar("E", "F", 10);
        conectar("E", "I", 60);
        conectar("F", "G", 2);
        conectar("G", "ZV", 3);
        conectar("H", "F", 4);
        conectar("H", "I", 5);
        conectar("H", "L", 6);
        conectar("I", "K", 2);
        conectar("I", "L", 3);
        conectar("J", "G", 1);  
        conectar("C", "J", 15); 
    }

    private void conectar(String a, String b, int d) {
        adjList.putIfAbsent(a, new ArrayList<>());
        adjList.putIfAbsent(b, new ArrayList<>());
        adjList.get(a).add(new Arista(a, b, d));
        adjList.get(b).add(new Arista(b, a, d)); 
        aristas.add(new Arista(a, b, d));
    }

    public List<String> dijkstra(String inicio, String destino) {
        Map<String, Integer> distancias = new HashMap<>();
        Map<String, String> previos = new HashMap<>();
        
        // Inicializamos las distancias a infinito
        for (String nodo : adjList.keySet()) {
            distancias.put(nodo, Integer.MAX_VALUE);
        }
        distancias.put(inicio, 0);

        // Usamos una PriorityQueue para obtener el nodo con la menor distancia
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distancias::get));
        pq.add(inicio);

        while (!pq.isEmpty()) {
            String nodoActual = pq.poll();
            int distanciaActual = distancias.get(nodoActual);

            // Recorremos los vecinos del nodo actual
            for (Arista vecino : adjList.get(nodoActual)) {
                String destinoVecino = vecino.getDestino();
                int nuevaDistancia = distanciaActual + vecino.getDistancia();

                // Si encontramos una ruta más corta a un vecino, la actualizamos
                if (nuevaDistancia < distancias.get(destinoVecino)) {
                    distancias.put(destinoVecino, nuevaDistancia);
                    previos.put(destinoVecino, nodoActual);
                    pq.add(destinoVecino);
                }
            }
        }

        // Reconstruir la ruta más corta
        List<String> ruta = new ArrayList<>();
        for (String at = destino; at != null; at = previos.get(at)) {
            ruta.add(at);
        }
        Collections.reverse(ruta);

        return ruta.get(0).equals(inicio) ? ruta : Collections.emptyList();
    }

    public Set<String> getNombresNodos() {
        return nodos.keySet();
    }

    public Nodo getNodo(String nombre) {
        return nodos.get(nombre);
    }

    public Collection<Nodo> getNodos() {
        return nodos.values();
    }

    public List<Arista> getAristas() {
        return aristas;
    }
}
