package rutas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MapaRuta extends JFrame {
    private final JComboBox<String> inicioCombo;
    private final JComboBox<String> destinoCombo;
    private final JTextArea resultadoArea;
    private final JLabel distanciaLabel;
    private final JLabel distanciaAlternativaLabel;
    private final JLabel mensajeLabel; 
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

        grafo = new Grafos();
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.decode("#D3D3D3"), 0, getHeight(), Color.decode("#A9A9A9"));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 30, 20));
        panelSuperior.setBackground(new Color(255, 255, 255, 230));

        JPanel panelRutas = new JPanel();
        panelRutas.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelRutas.setOpaque(false);

        JLabel labelInicio = new JLabel("Inicio:");
        styleLabel(labelInicio);
        inicioCombo = new JComboBox<>();
        styleComboBox(inicioCombo);
        JLabel labelDestino = new JLabel("Destino:");
        styleLabel(labelDestino);
        destinoCombo = new JComboBox<>();
        styleComboBox(destinoCombo);

        for (String nodo : grafo.getNombresNodos()) {
            inicioCombo.addItem(nodo);
            destinoCombo.addItem(nodo);
        }

        panelRutas.add(labelInicio);
        panelRutas.add(inicioCombo);
        panelRutas.add(labelDestino);
        panelRutas.add(destinoCombo);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelBotones.setOpaque(false);

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
        agregarEdificioBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarEdificio();
            }
        });
        eliminarEdificioBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarEdificioDeRutaRecalcular();
            }
        });
        reiniciarRutaBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarRuta();
            }
        });
        evitarEscalerasBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleEvitarEscaleras();
            }
        });

        panelBotones.add(calcularBtn);
        panelBotones.add(agregarEdificioBtn);
        panelBotones.add(eliminarEdificioBtn);
        panelBotones.add(reiniciarRutaBtn);
        panelBotones.add(evitarEscalerasBtn);

        panelSuperior.add(panelRutas);
        panelSuperior.add(Box.createVerticalStrut(10));
        panelSuperior.add(panelBotones);

        backgroundPanel.add(panelSuperior, BorderLayout.NORTH);
        getContentPane().add(backgroundPanel, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());

        panelGrafo = new GrafoPanel(grafo, rutaActual);
        panelGrafo.setBackground(new Color(245, 245, 245));
        panelCentral.add(panelGrafo, BorderLayout.CENTER);

        JPanel panelDistancias = new JPanel();
        panelDistancias.setLayout(new BoxLayout(panelDistancias, BoxLayout.Y_AXIS));
        panelDistancias.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelDistancias.setBackground(new Color(245, 245, 245));

        distanciaLabel = new JLabel("Distancia total: 0 pasos");
        distanciaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        distanciaLabel.setForeground(new Color(33, 150, 243));
        distanciaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mensajeLabel = new JLabel("");
        mensajeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mensajeLabel.setForeground(new Color(33, 33, 33));
        mensajeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mensajeLabel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));

        distanciaAlternativaLabel = new JLabel("Distancia rutas alternas: 0 pasos");
        distanciaAlternativaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        distanciaAlternativaLabel.setForeground(new Color(30, 136, 229));
        distanciaAlternativaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        distanciaAlternativaLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        panelDistancias.add(distanciaLabel);
        panelDistancias.add(mensajeLabel);
        panelDistancias.add(distanciaAlternativaLabel);

        panelCentral.add(panelDistancias, BorderLayout.EAST);

        getContentPane().add(panelCentral, BorderLayout.CENTER);

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

        getContentPane().add(scrollPane, BorderLayout.SOUTH);
    }

    private void reiniciarRuta() {
        inicioCombo.setSelectedIndex(0);
        destinoCombo.setSelectedIndex(0);
        resultadoArea.setText("");
        distanciaLabel.setText("Distancia ruta corta: 0 pasos");
        distanciaAlternativaLabel.setText("Distancia rutas alternas: 0 pasos");
        mensajeLabel.setText("");
        rutaActual = new ListaEnlazada<>();
        panelGrafo.setRutaActual(rutaActual);
        panelGrafo.repaint();
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
        calcularRuta();
    }

    private class CalcularRutaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            calcularRuta();
        }
    }

    private void calcularRuta() {
        String inicio = (String) inicioCombo.getSelectedItem();
        String destino = (String) destinoCombo.getSelectedItem();

        if (inicio == null || destino == null) {
            resultadoArea.setText("Por favor, seleccione un nodo de inicio y un nodo de destino.");
            mensajeLabel.setText("");
            distanciaLabel.setText("Distancia ruta corta: 0 pasos");
            distanciaAlternativaLabel.setText("Distancia rutas alternas: 0 pasos");
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

        mostrarRutaActual = true;

        if (rutaActual == null || rutaActual.size() == 0) {
            resultadoArea.setText("No se encontró ruta entre " + inicio + " y " + destino);
            distanciaLabel.setText("Distancia ruta corta: 0 pasos");
            distanciaAlternativaLabel.setText("Distancia rutas alternas: 0 pasos");
            mensajeLabel.setText("");
        } else {
            mostrarRutasConAlternativas(rutaActual, evitarEscalerasActivo, inicio, destino);
        }
        panelGrafo.setMostrarEscaleras(evitarEscalerasActivo);
        panelGrafo.setRutaActual(rutaActual);
        panelGrafo.repaint();
    }

    private void mostrarRutasConAlternativas(ListaEnlazada<String> rutaPrincipal, boolean evitarEscaleras, String inicio, String destino) {
        if (rutaPrincipal == null || rutaPrincipal.size() == 0) {
            resultadoArea.setText("");
            distanciaLabel.setText("Distancia ruta corta: 0 pasos");
            distanciaAlternativaLabel.setText("<html>Distancia rutas alternas: 0 pasos</html>");
            mensajeLabel.setText("");
            return;
        }

        StringBuilder sb = new StringBuilder();
        int distanciaTotal = 0;
        int distanciaRutasAlternasTotal = 0;

        sb.append("Ruta más corta:\n");
        for (int i = 0; i < rutaPrincipal.size() - 1; i++) {
            String actual = rutaPrincipal.get(i);
            String siguiente = rutaPrincipal.get(i + 1);
            sb.append(actual).append(" -> ");
            Arista arista = grafo.getArista(actual, siguiente);
            if (arista != null) {
                distanciaTotal += arista.getDistancia();
            } else {
                sb.append("[Arista no encontrada entre ").append(actual).append(" y ").append(siguiente).append("]\n");
            }
        }
        sb.append(rutaPrincipal.get(rutaPrincipal.size() - 1)).append("\n\n");

        sb.append("Distancia Ruta corta : ").append(distanciaTotal).append(" pasos\n\n");

        sb.append("Rutas alternativas nodo a nodo:\n");

        StringBuilder distAlternativasHtml = new StringBuilder("<html>Distancia rutas alternas: ");

        for (int i = 0; i < rutaPrincipal.size() - 1; i++) {
            String nodoOrigen = rutaPrincipal.get(i);
            String nodoDestino = rutaPrincipal.get(i + 1);

            ListaEnlazada<ListaEnlazada<String>> variasAlternativas = grafo.obtenerVariasRutasAlternativas(nodoOrigen, nodoDestino, rutaPrincipal, evitarEscaleras);

            if (variasAlternativas != null && variasAlternativas.size() > 0) {
                for (int altIndex = 0; altIndex < variasAlternativas.size(); altIndex++) {
                    ListaEnlazada<String> rutaAlternativa = variasAlternativas.get(altIndex);
                    if (rutaAlternativa != null && rutaAlternativa.size() > 1) {
                        int distanciaAlternativa = 0;
                        for (int j = 0; j < rutaAlternativa.size() - 1; j++) {
                            Arista aristaAlt = grafo.getArista(rutaAlternativa.get(j), rutaAlternativa.get(j + 1));
                            if (aristaAlt != null) {
                                distanciaAlternativa += aristaAlt.getDistancia();
                            }
                        }
                        distanciaRutasAlternasTotal += distanciaAlternativa;

                        sb.append("De ").append(nodoOrigen).append(" a ").append(nodoDestino)
                          .append(" (alternativa ").append(distanciaAlternativa).append(" pasos): ");

                        for (int j = 0; j < rutaAlternativa.size() - 1; j++) {
                            sb.append(rutaAlternativa.get(j)).append(" -> ");
                        }
                        sb.append(rutaAlternativa.get(rutaAlternativa.size() - 1)).append("\n");

                        distAlternativasHtml.append("<br>De ").append(nodoOrigen).append(" a ").append(nodoDestino)
                            .append(" alternativa ").append(altIndex + 1).append(": ").append(distanciaAlternativa).append(" pasos");
                    } else {
                        sb.append("De ").append(nodoOrigen).append(" a ").append(nodoDestino).append(": No hay alternativa\n");
                        distAlternativasHtml.append("<br>De ").append(nodoOrigen).append(" a ").append(nodoDestino).append(": No hay alternativa");
                    }
                }
            } else {
                sb.append("De ").append(nodoOrigen).append(" a ").append(nodoDestino).append(": No hay alternativa\n");
                distAlternativasHtml.append("<br>De ").append(nodoOrigen).append(" a ").append(nodoDestino).append(": No hay alternativa");
            }
        }
        distAlternativasHtml.append("</html>");

        resultadoArea.setText(sb.toString());
        distanciaLabel.setText("Distancia ruta corta: " + distanciaTotal + " pasos");
        distanciaAlternativaLabel.setText(distAlternativasHtml.toString());

        String mensaje = String.format("<html>- Desde nodo %s hasta nodo %s hay %d pasos.<br>" +
                "- De lo contrario, las rutas alternativas tienen una distancia total extra de %d pasos.</html> ",
                inicio, destino, distanciaTotal, distanciaRutasAlternasTotal);
        mensajeLabel.setText(mensaje);
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

            JPanel panel = new JPanel(new BorderLayout(5, 5));
            JComboBox<String> nodoCombo = new JComboBox<>(nodosEnRuta);
            nodoCombo.setPreferredSize(new Dimension(200, 25));
            panel.add(new JLabel("Seleccione el nodo después del cual agregar el nuevo edificio:"), BorderLayout.NORTH);
            panel.add(nodoCombo, BorderLayout.CENTER);

            int res = JOptionPane.showConfirmDialog(this, panel, "Agregar Edificio a la Ruta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                String nodoSeleccionado = (String) nodoCombo.getSelectedItem();
                if (nodoSeleccionado != null) {
                    if (((DefaultComboBoxModel<String>) inicioCombo.getModel()).getIndexOf(nombre) == -1) {
                        inicioCombo.addItem(nombre);
                        destinoCombo.addItem(nombre);
                    }

                    ListaEnlazada<String> rutaConNuevoNodo = new ListaEnlazada<>();

                    int idxSeleccionado = rutaActual.indiceDe(nodoSeleccionado);

                    for (int i = 0; i <= idxSeleccionado; i++) {
                        rutaConNuevoNodo.agregar(rutaActual.get(i));
                    }
                    rutaConNuevoNodo.agregar(nombre);
                    for (int i = idxSeleccionado + 1; i < rutaActual.size(); i++) {
                        rutaConNuevoNodo.agregar(rutaActual.get(i));
                    }

                    ListaEnlazada<String> rutaFinalReconstruida = new ListaEnlazada<>();
                    boolean rutaValida = true;

                    for (int i = 0; i < rutaConNuevoNodo.size() - 1; i++) {
                        String desde = rutaConNuevoNodo.get(i);
                        String hasta = rutaConNuevoNodo.get(i + 1);

                        ListaEnlazada<String> subRuta = grafo.obtenerRuta(desde, hasta, evitarEscalerasActivo);
                        if (subRuta == null || subRuta.size() == 0) {
                            subRuta = grafo.obtenerRuta(desde, hasta, false);
                            if (subRuta == null || subRuta.size() == 0) {
                                rutaValida = false;
                                break;
                            }
                        }

                        if (rutaFinalReconstruida.size() > 0) {
                            for (int j = 1; j < subRuta.size(); j++) {
                                rutaFinalReconstruida.agregar(subRuta.get(j));
                            }
                        } else {
                            for (int j = 0; j < subRuta.size(); j++) {
                                rutaFinalReconstruida.agregar(subRuta.get(j));
                            }
                        }
                    }

                    if (rutaValida) {
                        rutaActual = rutaFinalReconstruida;
                        mostrarRutasConAlternativas(rutaActual, evitarEscalerasActivo, (String)inicioCombo.getSelectedItem(), (String)destinoCombo.getSelectedItem());
                        mostrarRutaActual = true;
                        panelGrafo.setRutaActual(rutaActual);
                        panelGrafo.repaint();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo recalcular una ruta válida al agregar el edificio.");
                    }
                }
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

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JComboBox<String> nodoCombo = new JComboBox<>(nodosEnRuta);
        nodoCombo.setPreferredSize(new Dimension(200, 25));
        panel.add(new JLabel("Seleccione el edificio a eliminar de la ruta:"), BorderLayout.NORTH);
        panel.add(nodoCombo, BorderLayout.CENTER);

        int res = JOptionPane.showConfirmDialog(this, panel, "Eliminar Edificio de la Ruta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            String nodoAEliminar = (String) nodoCombo.getSelectedItem();
            if (nodoAEliminar != null) {
                // Guardamos copia antes de modificar rutaActual
                ListaEnlazada<String> rutaActualCopiada = new ListaEnlazada<>();
                for (int i = 0; i < rutaActual.size(); i++) {
                    rutaActualCopiada.agregar(rutaActual.get(i));
                }

                rutaActual.eliminar(nodoAEliminar);

                if (rutaActual.size() < 2) {
                    resultadoArea.setText("Ruta demasiado corta para recalcular tras eliminar.");
                    distanciaLabel.setText("Distancia total: 0 pasos");
                    distanciaAlternativaLabel.setText("Distancia rutas alternas: 0 pasos");
                    mensajeLabel.setText("");
                    panelGrafo.setRutaActual(rutaActual);
                    panelGrafo.repaint();
                    return;
                }

                String inicio = rutaActual.get(0);
                String destino = rutaActual.get(rutaActual.size() - 1);

                ListaEnlazada<String> rutaReconstruida = new ListaEnlazada<>();
                boolean rutaValida = true;

                // Construimos nueva ruta basada en subrutas entre nodos consecutivos en rutaActual ya sin nodo eliminado
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

                    if (rutaReconstruida.size() > 0) {
                        // Evitar repetir nodo intermedio
                        for (int j = 1; j < subRuta.size(); j++) {
                            rutaReconstruida.agregar(subRuta.get(j));
                        }
                    } else {
                        for (int j = 0; j < subRuta.size(); j++) {
                            rutaReconstruida.agregar(subRuta.get(j));
                        }
                    }
                }

                if (rutaValida) {
                    rutaActual = rutaReconstruida;
                    mostrarRutasConAlternativas(rutaActual, evitarEscalerasActivo, inicio, destino);
                    resultadoArea.append("Ruta recalculada tras eliminar edificio " + nodoAEliminar + "\n");
                } else {
                    resultadoArea.setText("No fue posible recalcular ruta tras eliminar el edificio " + nodoAEliminar);
                    distanciaLabel.setText("Distancia total: 0 pasos");
                    distanciaAlternativaLabel.setText("Distancia rutas alternas: 0 pasos");
                    mensajeLabel.setText("");
                    rutaActual = new ListaEnlazada<>();
                }

                panelGrafo.setMostrarEscaleras(evitarEscalerasActivo);
                panelGrafo.setRutaActual(rutaActual);
                panelGrafo.repaint();
                mostrarRutaActual = true;
            }
        }
    }

    class GrafoPanel extends JPanel {
        private final Grafos grafo;
        private ListaEnlazada<String> rutaActual;
        private java.util.List<ListaEnlazada<ListaEnlazada<String>>> rutasAlternativasPorArista;
        private boolean mostrarEscaleras = false;
        private boolean mostrarRutaActual = true;
        private Image backgroundImage;

        private final Color[] coloresRutasAlternativas = {
            new Color(30, 136, 229, 220),
            new Color(56, 142, 60, 220),
            new Color(255, 193, 7, 220),
            new Color(244, 67, 54, 180),
            new Color(123, 31, 162, 220),
        };

        public GrafoPanel(Grafos grafo, ListaEnlazada<String> rutaActual) {
            this.grafo = grafo;
            this.rutaActual = rutaActual;
            this.rutasAlternativasPorArista = new java.util.ArrayList<>();
            this.backgroundImage = new ImageIcon("/Users/andresnunez/Downloads/istockphoto-1407271745-612x612.jpg").getImage();
        }

        public void setRutaActual(ListaEnlazada<String> ruta) {
            this.rutaActual = ruta;
            rutasAlternativasPorArista.clear();

            if (ruta != null) {
                for (int i = 0; i < ruta.size() - 1; i++) {
                    ListaEnlazada<ListaEnlazada<String>> variasAlternativas =
                        grafo.obtenerVariasRutasAlternativas(ruta.get(i), ruta.get(i + 1), ruta, mostrarEscaleras);

                    if (variasAlternativas != null && variasAlternativas.size() > 0) {
                        rutasAlternativasPorArista.add(variasAlternativas);
                    } else {
                        rutasAlternativasPorArista.add(null);
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

            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

            Dimension size = getSize();
            double scaleX = size.width / 400.0;
            double scaleY = size.height / 500.0;

            g2.setColor(new Color(100, 100, 100, 150));
            g2.setStroke(new BasicStroke(2));
            for (Arista arista : grafo.getAristas()) {
                Nodo n1 = grafo.getNodo(arista.getOrigen());
                Nodo n2 = grafo.getNodo(arista.getDestino());
                if (n1 != null && n2 != null)
                    g2.drawLine((int) (n1.getX() * scaleX), (int) (n1.getY() * scaleY),
                                (int) (n2.getX() * scaleX), (int) (n2.getY() * scaleY));
            }

            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.setColor(new Color(80, 80, 80));
            for (Arista arista : grafo.getAristas()) {
                Nodo n1 = grafo.getNodo(arista.getOrigen());
                Nodo n2 = grafo.getNodo(arista.getDestino());
                if (n1 != null && n2 != null) {
                    int x1 = (int) (n1.getX() * scaleX);
                    int y1 = (int) (n1.getY() * scaleY);
                    int x2 = (int) (n2.getX() * scaleX);
                    int y2 = (int) (n2.getY() * scaleY);

                    int mx = (x1 + x2) / 2;
                    int my = (y1 + y2) / 2;

                    String distStr = String.valueOf(arista.getDistancia());
                    int strWidth = g2.getFontMetrics().stringWidth(distStr);
                    g2.drawString(distStr, mx - strWidth / 2, my - 4);
                }
            }

            g2.setColor(new Color(255, 140, 0));
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (Arista arista : grafo.getAristas()) {
                Nodo n1 = grafo.getNodo(arista.getOrigen());
                Nodo n2 = grafo.getNodo(arista.getDestino());
                if (n1 != null && n2 != null &&
                    n1.tieneEscalera() && n2.tieneEscalera() &&
                    arista.getOrigen().compareTo(arista.getDestino()) < 0) {
                    g2.drawLine((int)(n1.getX()*scaleX), (int)(n1.getY()*scaleY),
                                (int)(n2.getX()*scaleX), (int)(n2.getY()*scaleY));
                }
            }

            for (int idxArista = 0; idxArista < rutasAlternativasPorArista.size(); idxArista++) {
                ListaEnlazada<ListaEnlazada<String>> variasAlternativas = rutasAlternativasPorArista.get(idxArista);
                if (variasAlternativas != null) {
                    for (int i = 0; i < variasAlternativas.size(); i++) {
                        ListaEnlazada<String> altRuta = variasAlternativas.get(i);
                        if (altRuta != null && altRuta.size() > 1) {
                            Color colorAlt = coloresRutasAlternativas[i % coloresRutasAlternativas.length];
                            g2.setColor(colorAlt);
                            g2.setStroke(new BasicStroke(3 + i, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                            for (int j = 0; j < altRuta.size() - 1; j++) {
                                Nodo n1 = grafo.getNodo(altRuta.get(j));
                                Nodo n2 = grafo.getNodo(altRuta.get(j + 1));
                                if (n1 != null && n2 != null) {
                                    g2.drawLine((int) (n1.getX() * scaleX), (int) (n1.getY() * scaleY),
                                                (int) (n2.getX() * scaleX), (int) (n2.getY() * scaleY));
                                    Arista arista = grafo.getArista(altRuta.get(j), altRuta.get(j+1));
                                    if (arista != null) {
                                        int px = (int)((n1.getX() + n2.getX()) * scaleX / 2);
                                        int py = (int)((n1.getY() + n2.getY()) * scaleY / 2);
                                        String distStr = String.valueOf(arista.getDistancia());
                                        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                                        g2.setColor(colorAlt);
                                        g2.drawString(distStr, px - g2.getFontMetrics().stringWidth(distStr)/2, py - 5);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (rutaActual != null && rutaActual.size() > 1 && mostrarRutaActual) {
                g2.setColor(new Color(244, 67, 54));
                g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int i = 0; i < rutaActual.size() - 1; i++) {
                    Nodo n1 = grafo.getNodo(rutaActual.get(i));
                    Nodo n2 = grafo.getNodo(rutaActual.get(i + 1));
                    if (n1 != null && n2 != null)
                        g2.drawLine((int) (n1.getX() * scaleX), (int) (n1.getY() * scaleY),
                                    (int) (n2.getX() * scaleX), (int) (n2.getY() * scaleY));
                }
            }

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

                int peso = nodo.getPeso();
                String pesoStr = String.valueOf(peso);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                int pesoStrWidth = g2.getFontMetrics().stringWidth(pesoStr);
                g2.setColor(new Color(0, 0, 0, 160));
                g2.drawString(pesoStr, x - pesoStrWidth / 2, y + 20);
            }
        }
    }
}


