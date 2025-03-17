package rutas;
import java.util.*;

class Grafo {
    private Map<String, Map<String, Integer>> nodos;

    public Grafo() {
        this.nodos = new HashMap<>();
    }

    public void agregarEdificio(String nombre) {
        nodos.putIfAbsent(nombre, new HashMap<>());
    }

    public void agregarCamino(String origen, String destino, int distancia) {
        if (nodos.containsKey(origen) && nodos.containsKey(destino)) {
            nodos.get(origen).put(destino, distancia);
            nodos.get(destino).put(origen, distancia);
        }
    }

    public Set<String> obtenerEdificios() {
        return nodos.keySet();
    }

    public List<String> dijkstra(String inicio, String fin) {
        if (!nodos.containsKey(inicio) || !nodos.containsKey(fin)) {
            System.out.println("Uno o ambos nodos no existen en el grafo.");
            return Collections.emptyList();
        }

        PriorityQueue<Arista> cola = new PriorityQueue<>(Comparator.comparingInt(a -> a.distancia));
        Map<String, Integer> distancias = new HashMap<>();
        Map<String, String> padres = new HashMap<>();
        List<String> ruta = new ArrayList<>();
        Set<String> visitados = new HashSet<>();

        for (String nodo : nodos.keySet()) {
            distancias.put(nodo, Integer.MAX_VALUE);
        }
        distancias.put(inicio, 0);
        cola.add(new Arista(inicio, 0));

        while (!cola.isEmpty()) {
            Arista actual = cola.poll();

            if (visitados.contains(actual.nombre)) {
                continue;
            }
            visitados.add(actual.nombre);

            if (actual.nombre.equals(fin)) {
                String nodo = fin;
                while (nodo != null) {
                    ruta.add(0, nodo);
                    nodo = padres.get(nodo);
                }
                return ruta;
            }

            for (Map.Entry<String, Integer> vecino : nodos.get(actual.nombre).entrySet()) {
                int nuevaDistancia = distancias.get(actual.nombre) + vecino.getValue();
                if (nuevaDistancia < distancias.get(vecino.getKey())) {
                    distancias.put(vecino.getKey(), nuevaDistancia);
                    padres.put(vecino.getKey(), actual.nombre);
                    cola.add(new Arista(vecino.getKey(), nuevaDistancia));
                }
            }
        }
        return ruta;
    }

    private static class Arista {
        String nombre;
        int distancia;

        Arista(String nombre, int distancia) {
            this.nombre = nombre;
            this.distancia = distancia;
        }
    }

    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        grafo.agregarEdificio("A");
        grafo.agregarEdificio("B");
        grafo.agregarEdificio("C");
        grafo.agregarEdificio("D");

        grafo.agregarCamino("A", "B", 1);
        grafo.agregarCamino("A", "C", 4);
        grafo.agregarCamino("B", "C", 2);
        grafo.agregarCamino("B", "D", 5);
        grafo.agregarCamino("C", "D", 1);

        List<String> ruta = grafo.dijkstra("A", "D");
        System.out.println("Ruta más corta de A a D: " + ruta);
    }
}