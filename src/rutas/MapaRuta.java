package rutas;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MapaRuta extends JFrame {
    private final JComboBox<String> inicioCombo;
    private final JComboBox<String> destinoCombo;
    private final JTextArea resultadoArea;
    private final JLabel distanciaLabel;
    private final Grafos grafo;
    private ListaEnlazada<String> rutaActual;
    private GrafoPanel panelGrafo;
    private boolean mostrarRutaActual = true;
    private boolean evitarEscalerasActivo = false;
    private JToggleButton evitarEscalerasBtn;

    public MapaRuta() {
        setTitle("Sistema de Rutas UPB");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        grafo = new Grafos();
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.decode("#F5F5F5"), 0, getHeight(), Color.decode("#E0E0E0"));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new GridLayout(2, 5, 15, 15));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panelSuperior.setBackground(new Color(255, 255, 255, 230));

        inicioCombo = new JComboBox<>();
        destinoCombo = new JComboBox<>();
        for (String nodo : grafo.getNombresNodos()) {
            inicioCombo.addItem(nodo);
            destinoCombo.addItem(nodo);
        }

        styleComboBox(inicioCombo);
        styleComboBox(destinoCombo);

        JButton calcularBtn = new JButton("Calcular Ruta");
        styleButton(calcularBtn);
        calcularBtn.setPreferredSize(new Dimension(150, 40));

        JButton agregarEdificioBtn = new JButton("Agregar Edificio");
        styleButton(agregarEdificioBtn);
        agregarEdificioBtn.setPreferredSize(new Dimension(150, 40));

        JButton eliminarEdificioBtn = new JButton("Eliminar Edificio de Ruta");
        styleButton(eliminarEdificioBtn);
        eliminarEdificioBtn.setPreferredSize(new Dimension(150, 40));

        JButton reiniciarRutaBtn = new JButton("Reiniciar Ruta");
        styleButton(reiniciarRutaBtn);
        reiniciarRutaBtn.setPreferredSize(new Dimension(150, 40));

        evitarEscalerasBtn = new JToggleButton("Evitar Escaleras");
        styleToggleButton(evitarEscalerasBtn);
        evitarEscalerasBtn.setPreferredSize(new Dimension(150, 40));
        evitarEscalerasBtn.setEnabled(true);

        // Configurar color naranja para el toggle button
        evitarEscalerasBtn.setBackground(new Color(255, 140, 0));
        evitarEscalerasBtn.setForeground(Color.WHITE);
        evitarEscalerasBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        evitarEscalerasBtn.setFocusPainted(false);
        evitarEscalerasBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        evitarEscalerasBtn.setBorder(BorderFactory.createLineBorder(new Color(204, 85, 0), 2, true));
        evitarEscalerasBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                evitarEscalerasBtn.setBackground(new Color(204, 85, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (evitarEscalerasBtn.isSelected()) {
                    evitarEscalerasBtn.setBackground(new Color(153, 69, 0));
                } else {
                    evitarEscalerasBtn.setBackground(new Color(255, 140, 0));
                }
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (evitarEscalerasBtn.isSelected()) {
                    evitarEscalerasBtn.setBackground(new Color(153, 69, 0));
                } else {
                    evitarEscalerasBtn.setBackground(new Color(255, 140, 0));
                }
            }
        });

        calcularBtn.addActionListener(new CalcularRutaListener());
        agregarEdificioBtn.addActionListener(e -> agregarEdificio());
        eliminarEdificioBtn.addActionListener(e -> eliminarEdificioDeRutaRecalcular());
        reiniciarRutaBtn.addActionListener(e -> reiniciarRuta());
        evitarEscalerasBtn.addActionListener(e -> toggleEvitarEscaleras());

        JLabel labelInicio = new JLabel("Inicio:");
        JLabel labelDestino = new JLabel("Destino:");
        styleLabel(labelInicio);
        styleLabel(labelDestino);

        panelSuperior.add(labelInicio);
        panelSuperior.add(inicioCombo);
        panelSuperior.add(labelDestino);
        panelSuperior.add(destinoCombo);
        panelSuperior.add(calcularBtn);
        panelSuperior.add(agregarEdificioBtn);
        panelSuperior.add(eliminarEdificioBtn);
        panelSuperior.add(reiniciarRutaBtn);
        panelSuperior.add(evitarEscalerasBtn);

        distanciaLabel = new JLabel("Distancia total: 0 pasos");
        distanciaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        distanciaLabel.setForeground(new Color(33, 150, 243));
        panelSuperior.add(distanciaLabel);

        backgroundPanel.add(panelSuperior, BorderLayout.NORTH);
        container.add(backgroundPanel, BorderLayout.NORTH);

        panelGrafo = new GrafoPanel(grafo, rutaActual);
        panelGrafo.setBackground(new Color(245, 245, 245));
        container.add(panelGrafo, BorderLayout.CENTER);

        resultadoArea = new JTextArea(8, 20);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        resultadoArea.setBackground(Color.WHITE);
        resultadoArea.setForeground(new Color(50, 50, 50));
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);

        resultadoArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(null, "Resultado",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(33, 150, 243)));
        scrollPane.setPreferredSize(new Dimension(0, 150));

        container.add(scrollPane, BorderLayout.SOUTH);
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setFocusable(false);
        comboBox.setBackground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(150, 30));
        comboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(new Color(33, 150, 243));
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(25, 118, 210), 2, true));
        addHoverEffect(button);
    }

    private void styleToggleButton(JToggleButton toggleButton) {
        // Custom styling done in constructor
    }

    private void addHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(25, 118, 210));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(33, 150, 243));
            }
        });
    }

    private void toggleEvitarEscaleras() {
        evitarEscalerasActivo = evitarEscalerasBtn.isSelected();
        String inicio = (String) inicioCombo.getSelectedItem();
        String destino = (String) destinoCombo.getSelectedItem();

        if (inicio == null || destino == null) {
            return;
        }
        rutaActual = grafo.obtenerRuta(inicio, destino, evitarEscalerasActivo);

        if ((rutaActual == null || rutaActual.size() == 0) && evitarEscalerasActivo) {
            rutaActual = grafo.obtenerRuta(inicio, destino, false);
            resultadoArea.setText("No fue posible evitar escaleras, mostrando ruta con escaleras:\n");
        } else if (evitarEscalerasActivo) {
            resultadoArea.setText("Mostrando ruta evitando escaleras:\n");
        } else {
            resultadoArea.setText("");
        }

        if (rutaActual == null || rutaActual.size() == 0) {
            resultadoArea.setText("No se encontró ruta entre " + inicio + " y " + destino);
            distanciaLabel.setText("Distancia total: 0 pasos");
        } else {
            mostrarRutasConAlternativas(rutaActual, evitarEscalerasActivo);
        }
        panelGrafo.setMostrarEscaleras(evitarEscalerasActivo);
        panelGrafo.setRutaActual(rutaActual);
        panelGrafo.repaint();
        mostrarRutaActual = true;
    }

    private class CalcularRutaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String inicio = (String) inicioCombo.getSelectedItem();
            String destino = (String) destinoCombo.getSelectedItem();

            if (inicio != null && destino != null) {
                rutaActual = grafo.obtenerRuta(inicio, destino, evitarEscalerasActivo);

                if ((rutaActual == null || rutaActual.size() == 0) && evitarEscalerasActivo) {
                    rutaActual = grafo.obtenerRuta(inicio, destino, false);
                    resultadoArea.setText("No fue posible evitar escaleras, mostrando ruta con escaleras:\n");
                } else if (evitarEscalerasActivo) {
                    resultadoArea.setText("Mostrando ruta evitando escaleras:\n");
                } else {
                    resultadoArea.setText("");
                }

                mostrarRutaActual = true;

                if (rutaActual == null || rutaActual.size() == 0) {
                    resultadoArea.setText("No se encontró ruta entre " + inicio + " y " + destino);
                    distanciaLabel.setText("Distancia total: 0 pasos");
                } else {
                    mostrarRutasConAlternativas(rutaActual, evitarEscalerasActivo);
                }
                panelGrafo.setMostrarEscaleras(evitarEscalerasActivo);
                panelGrafo.setRutaActual(rutaActual);
                panelGrafo.repaint();
            }
        }
    }

    private void reiniciarRuta() {
        inicioCombo.setSelectedIndex(0);
        destinoCombo.setSelectedIndex(0);
        resultadoArea.setText("");
        distanciaLabel.setText("Distancia total: 0 pasos");
        rutaActual = new ListaEnlazada<>();
        panelGrafo.setRutaActual(rutaActual);
        panelGrafo.repaint();
    }

    private void mostrarRutasConAlternativas(ListaEnlazada<String> rutaPrincipal, boolean evitarEscaleras) {
        if (rutaPrincipal == null || rutaPrincipal.size() == 0) {
            resultadoArea.setText("");
            distanciaLabel.setText("Distancia total: 0 pasos");
            return;
        }

        StringBuilder sb = new StringBuilder();
        int distanciaTotal = 0;

        sb.append("Ruta más corta:\n");
        for (int i = 0; i < rutaPrincipal.size() - 1; i++) {
            String actual = rutaPrincipal.get(i);
            String siguiente = rutaPrincipal.get(i + 1);
            sb.append(actual).append(" -> ");
            Arista arista = grafo.getArista(actual, siguiente);
            if (arista != null) {
                distanciaTotal += arista.getDistancia();
            }
        }
        sb.append(rutaPrincipal.get(rutaPrincipal.size() - 1)).append("\n\n");

        sb.append("Distancia total: ").append(distanciaTotal).append(" pasos\n\n");

        sb.append("Rutas alternativas nodo a nodo:\n");
        for (int i = 0; i < rutaPrincipal.size() - 1; i++) {
            String nodoOrigen = rutaPrincipal.get(i);
            String nodoDestino = rutaPrincipal.get(i + 1);

            ListaEnlazada<String> rutaAlternativa = grafo.obtenerRutaAlternativa(nodoOrigen, nodoDestino, rutaPrincipal, evitarEscaleras);
            if (rutaAlternativa != null && rutaAlternativa.size() > 1) {
                int distanciaAlternativa = 0;
                for (int j = 0; j < rutaAlternativa.size() - 1; j++) {
                    Arista aristaAlt = grafo.getArista(rutaAlternativa.get(j), rutaAlternativa.get(j + 1));
                    if (aristaAlt != null)
                        distanciaAlternativa += aristaAlt.getDistancia();
                }
                sb.append("De ").append(nodoOrigen).append(" a ").append(nodoDestino)
                  .append(" (alternativa ").append(distanciaAlternativa).append(" pasos): ");
                for (int j = 0; j < rutaAlternativa.size() - 1; j++) {
                    sb.append(rutaAlternativa.get(j)).append(" -> ");
                }
                sb.append(rutaAlternativa.get(rutaAlternativa.size() - 1)).append("\n");
            } else {
                sb.append("De ").append(nodoOrigen).append(" a ").append(nodoDestino).append(": No hay alternativa\n");
            }
        }
        resultadoArea.setText(sb.toString());
        distanciaLabel.setText("Distancia total: " + distanciaTotal + " pasos");
    }

    private void agregarEdificio() {
        if (rutaActual == null || rutaActual.size() == 0) {
            JOptionPane.showMessageDialog(this, "Primero calcula una ruta.");
            return;
        }

        String nombre = JOptionPane.showInputDialog(this, "Nombre del nuevo edificio:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            String[] nodosEnRuta = new String[rutaActual.size()];
            for (int i = 0; i < rutaActual.size(); i++) {
                nodosEnRuta[i] = rutaActual.get(i);
            }

            String nodoSiguiente = (String) JOptionPane.showInputDialog(
                    this, "Seleccione el nodo después del cual agregar el nuevo edificio:",
                    "Agregar Edificio a la Ruta", JOptionPane.PLAIN_MESSAGE,
                    null, nodosEnRuta, nodosEnRuta[0]);

            if (nodoSiguiente != null) {
                if (((DefaultComboBoxModel<String>) inicioCombo.getModel()).getIndexOf(nombre) == -1) {
                    inicioCombo.addItem(nombre);
                    destinoCombo.addItem(nombre);
                }
                rutaActual.agregar(nombre);
                mostrarRutasConAlternativas(rutaActual, evitarEscalerasActivo);
                mostrarRutaActual = true;
                panelGrafo.setRutaActual(rutaActual);
                panelGrafo.repaint();
            }
        }
    }

    private void eliminarEdificioDeRutaRecalcular() {
        if (rutaActual == null || rutaActual.size() == 0) {
            JOptionPane.showMessageDialog(this, "Primero calcula una ruta.");
            return;
        }

        String[] nodosEnRuta = new String[rutaActual.size()];
        for (int i = 0; i < rutaActual.size(); i++) {
            nodosEnRuta[i] = rutaActual.get(i);
        }

        String nodoAEliminar = (String) JOptionPane.showInputDialog(
                this, "Seleccione el edificio a eliminar de la ruta:",
                "Eliminar Edificio de la Ruta", JOptionPane.PLAIN_MESSAGE,
                null, nodosEnRuta, nodosEnRuta[0]);

        if (nodoAEliminar != null) {
            rutaActual.eliminar(nodoAEliminar);

            if (rutaActual.size() < 2) {
                resultadoArea.setText("Ruta demasiado corta para recalcular tras eliminar.");
                distanciaLabel.setText("Distancia total: 0 pasos");
                panelGrafo.setRutaActual(rutaActual);
                panelGrafo.repaint();
                return;
            }

            String inicio = rutaActual.get(0);
            String destino = rutaActual.get(rutaActual.size() - 1);

            ListaEnlazada<String> rutaCompleta = new ListaEnlazada<>();
            boolean rutaValida = true;

            for (int i = 0; i < rutaActual.size() - 1; i++) {
                String desde = rutaActual.get(i);
                String hasta = rutaActual.get(i + 1);

                ListaEnlazada<String> subRuta = grafo.obtenerRuta(desde, hasta, evitarEscalerasActivo);

                if (subRuta == null || subRuta.size() == 0) {
                    subRuta = grafo.obtenerRuta(desde, hasta, false);
                    if (subRuta == null || subRuta.size() == 0) {
                        rutaValida = false;
                        break;
                    }
                }

                if (rutaCompleta.size() > 0) {
                    for (int j = 1; j < subRuta.size(); j++) {
                        rutaCompleta.agregar(subRuta.get(j));
                    }
                } else {
                    for (int j = 0; j < subRuta.size(); j++) {
                        rutaCompleta.agregar(subRuta.get(j));
                    }
                }
            }

            if (rutaValida) {
                rutaActual = rutaCompleta;
                mostrarRutasConAlternativas(rutaActual, evitarEscalerasActivo);
                resultadoArea.append("Ruta recalculada tras eliminar edificio " + nodoAEliminar + "\n");
            } else {
                resultadoArea.setText("No fue posible recalcular ruta tras eliminar el edificio " + nodoAEliminar);
                distanciaLabel.setText("Distancia total: 0 pasos");
                rutaActual = new ListaEnlazada<>();
            }

            panelGrafo.setMostrarEscaleras(evitarEscalerasActivo);
            panelGrafo.setRutaActual(rutaActual);
            panelGrafo.repaint();
            mostrarRutaActual = true;
        }
    }

    class GrafoPanel extends JPanel {
        private final Grafos grafo;
        private ListaEnlazada<String> rutaActual;
        private java.util.List<ListaEnlazada<String>> rutasAlternativas; 
        private boolean mostrarEscaleras = false; 

        public GrafoPanel(Grafos grafo, ListaEnlazada<String> rutaActual) {
            this.grafo = grafo;
            this.rutaActual = rutaActual;
            this.rutasAlternativas = new java.util.ArrayList<>();
        }

        public void setRutaActual(ListaEnlazada<String> ruta) {
            this.rutaActual = ruta;
            rutasAlternativas.clear();
            if (ruta != null) {
                for (int i = 0; i < ruta.size() - 1; i++) {
                    ListaEnlazada<String> alt = grafo.obtenerRutaAlternativa(ruta.get(i), ruta.get(i+1), ruta, mostrarEscaleras);
                    if (alt != null && alt.size() > 1) {
                        rutasAlternativas.add(alt);
                    } else {
                        rutasAlternativas.add(null);
                    }
                }
            }
        }

        public void setMostrarEscaleras(boolean mostrarEscaleras) {
            this.mostrarEscaleras = mostrarEscaleras;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Dimension size = getSize();
            double scaleX = size.width / 400.0;
            double scaleY = size.height / 500.0;

            // Dibujar todas las aristas en gris claro
            g2.setColor(new Color(100, 100, 100, 150));
            g2.setStroke(new BasicStroke(2));
            for (Arista arista : grafo.getAristas()) {
                Nodo n1 = grafo.getNodo(arista.getOrigen());
                Nodo n2 = grafo.getNodo(arista.getDestino());
                if (n1 != null && n2 != null)
                    g2.drawLine((int) (n1.getX() * scaleX), (int) (n1.getY() * scaleY), (int) (n2.getX() * scaleX), (int) (n2.getY() * scaleY));
            }

            // Dibujar línea naranja entre nodos con escaleras (solo una vez por arista)
            g2.setColor(new Color(255, 140, 0)); // naranja
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (Arista arista : grafo.getAristas()) {
                Nodo n1 = grafo.getNodo(arista.getOrigen());
                Nodo n2 = grafo.getNodo(arista.getDestino());
                if (n1 != null && n2 != null) {
                    if (n1.tieneEscalera() && n2.tieneEscalera()) {
                        if (arista.getOrigen().compareTo(arista.getDestino()) < 0) {
                            g2.drawLine((int)(n1.getX()*scaleX), (int)(n1.getY()*scaleY), (int)(n2.getX()*scaleX), (int)(n2.getY()*scaleY));
                        }
                    }
                }
            }

            // Dibujar rutas alternativas en azul más notorio
            if (rutasAlternativas != null) {
                g2.setColor(new Color(30, 136, 229, 220)); // azul intenso y más opaco
                g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (ListaEnlazada<String> altRuta : rutasAlternativas) {
                    if (altRuta != null && altRuta.size() > 1) {
                        for (int i = 0; i < altRuta.size() - 1; i++) {
                            Nodo n1 = grafo.getNodo(altRuta.get(i));
                            Nodo n2 = grafo.getNodo(altRuta.get(i + 1));
                            if (n1 != null && n2 != null) {
                                g2.drawLine((int) (n1.getX() * scaleX), (int) (n1.getY() * scaleY),
                                            (int) (n2.getX() * scaleX), (int) (n2.getY() * scaleY));
                                Arista arista = grafo.getArista(altRuta.get(i), altRuta.get(i+1));
                                if (arista != null) {
                                    int px = (int)((n1.getX() + n2.getX()) * scaleX / 2);
                                    int py = (int)((n1.getY() + n2.getY()) * scaleY / 2);
                                    String distStr = String.valueOf(arista.getDistancia());
                                    g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                                    g2.setColor(new Color(30, 136, 229, 220));
                                    g2.drawString(distStr, px - g2.getFontMetrics().stringWidth(distStr)/2, py - 5);
                                    g2.setColor(new Color(30, 136, 229, 220));
                                }
                            }
                        }
                    }
                }
            }

            // Dibujar ruta actual en rojo
            if (rutaActual != null && rutaActual.size() > 1 && mostrarRutaActual) {
                g2.setColor(new Color(244, 67, 54));
                g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int i = 0; i < rutaActual.size() - 1; i++) {
                    Nodo n1 = grafo.getNodo(rutaActual.get(i));
                    Nodo n2 = grafo.getNodo(rutaActual.get(i + 1));
                    if (n1 != null && n2 != null)
                        g2.drawLine((int) (n1.getX() * scaleX), (int) (n1.getY() * scaleY), (int) (n2.getX() * scaleX), (int) (n2.getY() * scaleY));
                }
            }

            // Dibujar nodos
            for (Nodo nodo : grafo.getNodos()) {
                int x = (int) (nodo.getX() * scaleX);
                int y = (int) (nodo.getY() * scaleY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));

                if (nodo.getNombre().equals("CAFETERIA") || nodo.getNombre().equals("PORTERIA")) {
                    g2.setColor(new Color(210, 210, 210, 180));
                    g2.fillRoundRect(x - 34, y - 24, 68, 48, 15, 15);
                    g2.setColor(new Color(33, 150, 243));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(x - 34, y - 24, 68, 48, 15, 15);
                } else {
                    g2.setColor(new Color(66, 133, 244));
                    g2.fillOval(x - 18, y - 18, 36, 36);
                    g2.setColor(new Color(33, 33, 33));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(x - 18, y - 18, 36, 36);
                }

                FontMetrics fm = g2.getFontMetrics();
                int strWidth = fm.stringWidth(nodo.getNombre());
                g2.setColor(new Color(33, 33, 33));
                g2.drawString(nodo.getNombre(), x - strWidth / 2, y + 5);
            }
        }
    }
}

