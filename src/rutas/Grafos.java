package rutas;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class Grafos {
    private final ListaEnlazada<Nodo> nodos = new ListaEnlazada<>();
    private final ListaEnlazada<Arista> aristas = new ListaEnlazada<>();
    private ListaEnlazada<DistanciaNodo> distancias = new ListaEnlazada<>();

    public Grafos() {
        inicializarNodos();
        inicializarGrafo();
    }

    private void inicializarNodos() {
        agregarNodo(new Nodo("A", 20, 250));
        agregarNodo(new Nodo("B", 60, 270));
        agregarNodo(new Nodo("C", 100, 290));
        agregarNodo(new Nodo("CAFETERIA", 60, 200));
        agregarNodo(new Nodo("D", 160, 250));
        agregarNodo(new Nodo("E", 220, 200));
        agregarNodo(new Nodo("F", 280, 180));
        agregarNodo(new Nodo("G", 260, 250));
        agregarNodo(new Nodo("H", 280, 120));
        agregarNodo(new Nodo("I", 240, 60));
        agregarNodo(new Nodo("J", 180, 320));
        agregarNodo(new Nodo("K", 160, 100));
        agregarNodo(new Nodo("L", 300, 40));
        agregarNodo(new Nodo("PORTERIA", 140, 360));
    }

    private void inicializarGrafo() {
        conectar("A", "B", 10);
        conectar("B", "C", 10);
        conectar("B", "CAFETERIA", 15);
        conectar("CAFETERIA", "C", 15);
        conectar("CAFETERIA", "D", 30);
        conectar("C", "PORTERIA", 30);
        conectar("PORTERIA", "J", 30);
        conectar("J", "D", 40);
        conectar("D", "E", 40);
        conectar("D", "G", 25);
        conectar("D", "K", 150);
        conectar("D", "K", 50);
        conectar("E", "F", 10);
        conectar("E", "I", 60);
        conectar("F", "G", 20);
        conectar("H", "F", 15);
        conectar("H", "I", 30);
        conectar("H", "L", 130);
        conectar("I", "K", 100);
        conectar("I", "L", 20);
        conectar("J", "G", 50);
        conectar("C", "J", 15);
    }

    public void agregarNodo(Nodo nodo) {
        nodos.agregar(nodo);
    }

    public void eliminarNodo(Nodo nodo) {
        nodos.eliminar(nodo);
    }

    public void conectar(String a, String b, int distancia) {
        aristas.agregar(new Arista(a, b, distancia));
        aristas.agregar(new Arista(b, a, distancia));
    }

    public ListaEnlazada<String> dijkstra(String inicio, String destino) {
        distancias.clear(); 
        ListaEnlazada<String> nodosPorVisitar = new ListaEnlazada<>();
        ListaEnlazada<String> nodosVisitados = new ListaEnlazada<>();
        ListaEnlazada<String> ruta = new ListaEnlazada<>();

        for (Nodo nodo : nodos) {
            int distancia = nodo.getNombre().equals(inicio) ? 0 : Integer.MAX_VALUE;
            distancias.agregar(new DistanciaNodo(nodo.getNombre(), distancia, null));
            nodosPorVisitar.agregar(nodo.getNombre());
        }

        while (nodosPorVisitar.size() > 0) {
            DistanciaNodo actual = obtenerMenorDistancia(distancias, nodosPorVisitar);
            if (actual == null) break;

            nodosVisitados.agregar(actual.nombre);
            eliminarDeLista(nodosPorVisitar, actual.nombre);

            for (Arista arista : aristas) {
                if (arista.getOrigen().equals(actual.nombre)) {
                    String vecino = arista.getDestino();
                    if (!contiene(nodosVisitados, vecino)) {
                        int nuevaDistancia = (actual.distancia == Integer.MAX_VALUE ? Integer.MAX_VALUE : actual.distancia + arista.getDistancia());
                        DistanciaNodo dnVecino = buscarDistancia(distancias, vecino);
                        if (dnVecino != null && nuevaDistancia < dnVecino.distancia) {
                            dnVecino.distancia = nuevaDistancia;
                            dnVecino.anterior = actual.nombre;
                        }
                    }
                }
            }
        }

        String actual = destino;
        while (actual != null) {
            ruta.insertarInicio(actual);
            DistanciaNodo dn = buscarDistancia(distancias, actual);
            actual = (dn != null) ? dn.anterior : null;
        }

        if (ruta.size() > 0 && ruta.get(0).equals(inicio)) {
            return ruta;
        } else {
            return new ListaEnlazada<>();
        }
    }

    public ListaEnlazada<String> dijkstraEvitarEscaleras(String inicio, String destino) {
        distancias.clear(); 
        ListaEnlazada<String> nodosPorVisitar = new ListaEnlazada<>();
        ListaEnlazada<String> nodosVisitados = new ListaEnlazada<>();
        ListaEnlazada<String> ruta = new ListaEnlazada<>();

        for (Nodo nodo : nodos) {
            if (nodo.getNombre().equals(inicio) || nodo.getNombre().equals(destino) || !estaEnEscalera(nodo.getNombre())) {
                int distancia = nodo.getNombre().equals(inicio) ? 0 : Integer.MAX_VALUE;
                distancias.agregar(new DistanciaNodo(nodo.getNombre(), distancia, null));
                nodosPorVisitar.agregar(nodo.getNombre());
            }
        }

        while (nodosPorVisitar.size() > 0) {
            DistanciaNodo actual = obtenerMenorDistancia(distancias, nodosPorVisitar);
            if (actual == null) break;

            nodosVisitados.agregar(actual.nombre);
            eliminarDeLista(nodosPorVisitar, actual.nombre);

            for (Arista arista : aristas) {
                if (arista.getOrigen().equals(actual.nombre)) {
                    String vecino = arista.getDestino();
                    if (!contiene(nodosVisitados, vecino)
                            && (vecino.equals(inicio) || vecino.equals(destino) || !estaEnEscalera(vecino))) {
                        int nuevaDistancia = (actual.distancia == Integer.MAX_VALUE ? Integer.MAX_VALUE : actual.distancia + arista.getDistancia());
                        DistanciaNodo dnVecino = buscarDistancia(distancias, vecino);
                        if (dnVecino != null && nuevaDistancia < dnVecino.distancia) {
                            dnVecino.distancia = nuevaDistancia;
                            dnVecino.anterior = actual.nombre;
                        }
                    }
                }
            }
        }

        String actual = destino;
        while (actual != null) {
            ruta.insertarInicio(actual);
            DistanciaNodo dn = buscarDistancia(distancias, actual);
            actual = (dn != null) ? dn.anterior : null;
        }

        if (ruta.size() > 0 && ruta.get(0).equals(inicio)) {
            return ruta;
        } else {
            return new ListaEnlazada<>();
        }
    }

    public ListaEnlazada<String> dijkstraAlternativo(String inicio, String destino, ListaEnlazada<String> rutaPrincipal) {
        distancias.clear(); 
        ListaEnlazada<String> nodosPorVisitar = new ListaEnlazada<>();
        ListaEnlazada<String> nodosVisitados = new ListaEnlazada<>();
        ListaEnlazada<String> ruta = new ListaEnlazada<>();

        for (Nodo nodo : nodos) {
            int distancia = nodo.getNombre().equals(inicio) ? 0 : Integer.MAX_VALUE;
            distancias.agregar(new DistanciaNodo(nodo.getNombre(), distancia, null));
            nodosPorVisitar.agregar(nodo.getNombre());
        }

        while (nodosPorVisitar.size() > 0) {
            DistanciaNodo actual = obtenerMenorDistancia(distancias, nodosPorVisitar);
            if (actual == null) break;

            nodosVisitados.agregar(actual.nombre);
            eliminarDeLista(nodosPorVisitar, actual.nombre);

            for (Arista arista : aristas) {
                if (arista.getOrigen().equals(actual.nombre)) {
                    String vecino = arista.getDestino();
                    if (!contiene(nodosVisitados, vecino)) {
                        if (!estaEnRutaPrincipal(arista, rutaPrincipal)) {
                            int nuevaDistancia = (actual.distancia == Integer.MAX_VALUE ? Integer.MAX_VALUE : actual.distancia + arista.getDistancia());
                            DistanciaNodo dnVecino = buscarDistancia(distancias, vecino);
                            if (dnVecino != null && nuevaDistancia < dnVecino.distancia) {
                                dnVecino.distancia = nuevaDistancia;
                                dnVecino.anterior = actual.nombre;
                            }
                        }
                    }
                }
            }
        }

        String actual = destino;
        while (actual != null) {
            ruta.insertarInicio(actual);
            DistanciaNodo dn = buscarDistancia(distancias, actual);
            actual = (dn != null) ? dn.anterior : null;
        }

        if (ruta.size() > 0 && ruta.get(0).equals(inicio) && !rutaIgual(ruta, rutaPrincipal)) {
            return ruta;
        } else {
            return null;
        }
    }

    private boolean estaEnEscalera(String nodo) {
        String n = nodo.toUpperCase();
        if (n.equals("PORTERIA") || n.equals("J")) return true;
        if (n.equals("B") || n.equals("CAFETERIA")) return true;
        if (n.equals("J") || n.equals("D")) return true;
        if (n.equals("D") || n.equals("K")) return true;
        if (n.equals("I") || n.equals("K")) return true;
        return false;
    }

    private DistanciaNodo obtenerMenorDistancia(ListaEnlazada<DistanciaNodo> distancias, ListaEnlazada<String> porVisitar) {
        DistanciaNodo menor = null;
        for (DistanciaNodo dn : distancias) {
            if (contiene(porVisitar, dn.nombre)) {
                if (menor == null || dn.distancia < menor.distancia) {
                    menor = dn;
                }
            }
        }
        return menor;
    }

    private void eliminarDeLista(ListaEnlazada<String> lista, String valor) {
        ListaEnlazada<String> nueva = new ListaEnlazada<>();
        for (String s : lista) {
            if (!s.equals(valor)) {
                nueva.agregar(s);
            }
        }
        lista.clear();
        lista.agregarTodos(nueva);
    }

    public void eliminarNodo(String nombreNodo) {
        ListaEnlazada<Arista> aristasAEliminar = new ListaEnlazada<>();
        for (Arista arista : aristas) {
            if (arista.getOrigen().equals(nombreNodo) || arista.getDestino().equals(nombreNodo)) {
                aristasAEliminar.agregar(arista);
            }
        }

        for (Arista arista : aristasAEliminar) {
            aristas.eliminar(arista);
        }

        Nodo nodoAEliminar = null;
        for (Nodo nodo : nodos) {
            if (nodo.getNombre().equals(nombreNodo)) {
                nodoAEliminar = nodo;
                break;
            }
        }
        if (nodoAEliminar != null) {
            nodos.eliminar(nodoAEliminar);
        }
    }

    private boolean contiene(ListaEnlazada<String> lista, String valor) {
        for (String s : lista) {
            if (s.equals(valor)) return true;
        }
        return false;
    }

    private DistanciaNodo buscarDistancia(ListaEnlazada<DistanciaNodo> distancias, String nombre) {
        for (DistanciaNodo dn : distancias) {
            if (dn.nombre.equals(nombre)) return dn;
        }
        return null;
    }

    public Nodo getNodo(String nombre) {
        for (Nodo nodo : nodos) {
            if (nodo.getNombre().equals(nombre)) {
                return nodo;
            }
        }
        return null;
    }

    public Arista getArista(String origen, String destino) {
        for (Arista arista : aristas) {
            if (arista.getOrigen().equals(origen) && arista.getDestino().equals(destino)) {
                return arista;
            }
        }
        return null;
    }

    public int getPeso(String origen, String destino) {
        for (Arista arista : aristas) {
            if (arista.getOrigen().equals(origen) && arista.getDestino().equals(destino)) {
                return arista.getDistancia();
            }
        }
        return Integer.MAX_VALUE;
    }

    private boolean estaEnRutaPrincipal(Arista arista, ListaEnlazada<String> rutaPrincipal) {
        for (int i = 0; i < rutaPrincipal.size() - 1; i++) {
            String origen = rutaPrincipal.get(i);
            String destino = rutaPrincipal.get(i + 1);
            if (arista.getOrigen().equals(origen) && arista.getDestino().equals(destino)) {
                return true;
            }
        }
        return false;
    }

    private boolean rutaIgual(ListaEnlazada<String> ruta1, ListaEnlazada<String> ruta2) {
        if (ruta1 == null || ruta2 == null) return false;
        if (ruta1.size() != ruta2.size()) return false;
        for (int i = 0; i < ruta1.size(); i++) {
            if (!ruta1.get(i).equals(ruta2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public ListaEnlazada<String> getNombresNodos() {
        ListaEnlazada<String> nombresNodos = new ListaEnlazada<>();
        for (Nodo nodo : nodos) {
            nombresNodos.agregar(nodo.getNombre());
        }
        return nombresNodos;
    }

    public ListaEnlazada<Nodo> getNodos() {
        return nodos;
    }

    public ListaEnlazada<Arista> getAristas() {
        return aristas;
    }

    public ListaEnlazada<DistanciaNodo> getDistancias() {
        return distancias;
    }

    public void actualizarInterfaz(JComboBox<String> comboBoxInicio, JComboBox<String> comboBoxDestino) {
        DefaultComboBoxModel<String> modelInicio = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> modelDestino = new DefaultComboBoxModel<>();
        for (Nodo nodo : nodos) {
            modelInicio.addElement(nodo.getNombre());
            modelDestino.addElement(nodo.getNombre());
        }
        comboBoxInicio.setModel(modelInicio);
        comboBoxDestino.setModel(modelDestino);
    }

    private static class DistanciaNodo {
        String nombre;
        int distancia;
        String anterior;

        DistanciaNodo(String nombre, int distancia, String anterior) {
            this.nombre = nombre;
            this.distancia = distancia;
            this.anterior = anterior;
        }
    }
}

