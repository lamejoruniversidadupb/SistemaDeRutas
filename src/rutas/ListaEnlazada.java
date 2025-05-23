package rutas;

import java.util.Iterator;
import java.util.function.Predicate;

public class ListaEnlazada<T> implements Iterable<T> {

    private NodoLista<T> cabeza;
    private int tamaño;

    public ListaEnlazada() {
        cabeza = null;
        tamaño = 0;
    }

    public void agregar(T valor) {
        NodoLista<T> nuevoNodo = new NodoLista<>(valor);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoLista<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        tamaño++;
    }

    public void agregarTodos(ListaEnlazada<T> otraLista) {
        for (T valor : otraLista) {
            this.agregar(valor);
        }
    }

    public void agregarInicio(T valor) {
        NodoLista<T> nuevoNodo = new NodoLista<>(valor);
        nuevoNodo.siguiente = cabeza;
        cabeza = nuevoNodo;
        tamaño++;
    }

    public void insertarInicio(T valor) {
        agregarInicio(valor);
    }

    public boolean eliminar(T valor) {
        if (cabeza == null) return false;

        if (cabeza.valor.equals(valor)) {
            cabeza = cabeza.siguiente;
            tamaño--;
            return true;
        }

        NodoLista<T> actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.valor.equals(valor)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return true;
            }
            actual = actual.siguiente;
        }

        return false;
    }

    public void eliminarSi(Predicate<T> condicion) {
        if (cabeza == null) return;

        while (cabeza != null && condicion.test(cabeza.valor)) {
            cabeza = cabeza.siguiente;
            tamaño--;
        }

        NodoLista<T> actual = cabeza;
        while (actual != null && actual.siguiente != null) {
            if (condicion.test(actual.siguiente.valor)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
            } else {
                actual = actual.siguiente;
            }
        }
    }

    public boolean contiene(T valor) {
        NodoLista<T> actual = cabeza;
        while (actual != null) {
            if (actual.valor.equals(valor)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public T buscar(T valor) {
        NodoLista<T> actual = cabeza;
        while (actual != null) {
            if (actual.valor.equals(valor)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public ListaEnlazada<T> filtrar(Predicate<T> condicion) {
        ListaEnlazada<T> resultado = new ListaEnlazada<>();
        NodoLista<T> actual = cabeza;
        while (actual != null) {
            if (condicion.test(actual.valor)) {
                resultado.agregar(actual.valor);
            }
            actual = actual.siguiente;
        }
        return resultado;
    }

    public int indiceDe(T valor) {
        NodoLista<T> actual = cabeza;
        int indice = 0;
        while (actual != null) {
            if (actual.valor.equals(valor)) {
                return indice;
            }
            actual = actual.siguiente;
            indice++;
        }
        return -1; // Retorna -1 si no se encuentra el valor
    }

    public T get(int índice) {
        if (índice < 0 || índice >= tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        NodoLista<T> actual = cabeza;
        for (int i = 0; i < índice; i++) {
            actual = actual.siguiente;
        }
        return actual.valor;
    }

    public void clear() {
        cabeza = null;
        tamaño = 0;
    }

    public boolean isEmpty() {
        return tamaño == 0;
    }

    public int size() {
        return tamaño;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private NodoLista<T> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                T valor = actual.valor;
                actual = actual.siguiente;
                return valor;
            }
        };
    }

    private static class NodoLista<T> {
        T valor;
        NodoLista<T> siguiente;

        NodoLista(T valor) {
            this.valor = valor;
            this.siguiente = null;
        }
    }

    public static class DistanciaNodo {
        public String nombre;
        public int distancia;
        public String anterior;

        public DistanciaNodo(String nombre, int distancia, String anterior) {
            this.nombre = nombre;
            this.distancia = distancia;
            this.anterior = anterior;
        }

        @Override
        public String toString() {
            return "DistanciaNodo{" +
                    "nombre='" + nombre + '\'' +
                    ", distancia=" + distancia +
                    ", anterior='" + anterior + '\'' +
                    '}';
        }
    }
}
