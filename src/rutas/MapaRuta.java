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
    private final JLabel distanciaLabel;  // Nueva etiqueta para la distancia total
    private final Grafos grafo;
    private ListaEnlazada<String> rutaActual;
    private GrafoPanel panelGrafo;

    public MapaRuta() {
        setTitle("Sistema de Rutas UPB");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        grafo = new Grafos();
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panelSuperior.setBackground(new Color(250, 250, 250));

        inicioCombo = new JComboBox<>();
        destinoCombo = new JComboBox<>();
        for (String nodo : grafo.getNombresNodos()) {
            inicioCombo.addItem(nodo);
            destinoCombo.addItem(nodo);
        }

        styleComboBox(inicioCombo);
        styleComboBox(destinoCombo);

        JButton calcularBtn = new JButton("Calcular Ruta");
        calcularBtn.setBackground(new Color(33, 150, 243));
        calcularBtn.setForeground(Color.WHITE);
        calcularBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        calcularBtn.setFocusPainted(false);
        calcularBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addHoverEffect(calcularBtn);
        calcularBtn.addActionListener(new CalcularRutaListener());

        JLabel labelInicio = new JLabel("Inicio:");
        JLabel labelDestino = new JLabel("Destino:");
        styleLabel(labelInicio);
        styleLabel(labelDestino);

        panelSuperior.add(labelInicio);
        panelSuperior.add(inicioCombo);
        panelSuperior.add(labelDestino);
        panelSuperior.add(destinoCombo);
        panelSuperior.add(calcularBtn);

        container.add(panelSuperior, BorderLayout.NORTH);

        panelGrafo = new GrafoPanel();
        panelGrafo.setBackground(new Color(245, 245, 245));
        container.add(panelGrafo, BorderLayout.CENTER);

        resultadoArea = new JTextArea(3, 20);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        resultadoArea.setBackground(Color.WHITE);
        resultadoArea.setForeground(new Color(50, 50, 50));
        resultadoArea.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(33, 150, 243), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(null, "Resultado",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(33, 150, 243)));
        scrollPane.setPreferredSize(new Dimension(0, 120));
        container.add(scrollPane, BorderLayout.SOUTH);

        // Nueva etiqueta para mostrar la distancia total
        distanciaLabel = new JLabel("Distancia total: 0 pasos");
        distanciaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        distanciaLabel.setForeground(new Color(33, 150, 243));
        panelSuperior.add(distanciaLabel);
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

    private class CalcularRutaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String inicio = (String) inicioCombo.getSelectedItem();
            String destino = (String) destinoCombo.getSelectedItem();

            if (inicio != null && destino != null) {
                rutaActual = grafo.dijkstra(inicio, destino);
                if (rutaActual == null || rutaActual.size() == 0) {
                    resultadoArea.setText("No se encontró ruta entre " + inicio + " y " + destino);
                    distanciaLabel.setText("Distancia total: 0 pasos");
                } else {
                    StringBuilder sb = new StringBuilder();
                    double distanciaTotal = 0.0;
                    for (int i = 0; i < rutaActual.size() - 1; i++) {
                        String nodoActual = rutaActual.get(i);
                        String siguienteNodo = rutaActual.get(i + 1);
                        Arista arista = grafo.getArista(nodoActual, siguienteNodo); 
                        if (arista != null) {
                            distanciaTotal += arista.getPeso(); 
                        }
                        sb.append(nodoActual).append(" -> ");
                    }
                    sb.append(rutaActual.get(rutaActual.size() - 1));
                    resultadoArea.setText("Ruta más corta:\n" + sb);

                    // Mostrar la distancia total como número entero en "pasos"
                    distanciaLabel.setText("Distancia total: " + (int) distanciaTotal + " pasos");
                }
                panelGrafo.repaint();
            }
        }
    }

    class GrafoPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Dimension size = getSize();
            double scaleX = size.width / 400.0;
            double scaleY = size.height / 500.0;

            g2.setColor(new Color(100, 100, 100, 150));
            g2.setStroke(new BasicStroke(2));
            for (Arista arista : grafo.getAristas()) {
                Nodo n1 = grafo.getNodo(arista.getOrigen());
                Nodo n2 = grafo.getNodo(arista.getDestino());
                if (n1 != null && n2 != null)
                    g2.drawLine((int) (n1.getX() * scaleX), (int) (n1.getY() * scaleY), (int) (n2.getX() * scaleX), (int) (n2.getY() * scaleY));
            }

            if (rutaActual != null && rutaActual.size() > 1) {
                g2.setColor(new Color(244, 67, 54));
                g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int i = 0; i < rutaActual.size() - 1; i++) {
                    Nodo n1 = grafo.getNodo(rutaActual.get(i));
                    Nodo n2 = grafo.getNodo(rutaActual.get(i + 1));
                    if (n1 != null && n2 != null)
                        g2.drawLine((int) (n1.getX() * scaleX), (int) (n1.getY() * scaleY), (int) (n2.getX() * scaleX), (int) (n2.getY() * scaleY));
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
                    g2.setColor(new Color(33, 33, 33));
                    FontMetrics fm = g2.getFontMetrics();
                    int strWidth = fm.stringWidth(nodo.getNombre());
                    g2.drawString(nodo.getNombre(), x - strWidth / 2, y + 5);
                } else {
                    g2.setColor(new Color(66, 133, 244));
                    g2.fillOval(x - 18, y - 18, 36, 36);
                    g2.setColor(new Color(33, 33, 33));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(x - 18, y - 18, 36, 36);
                    FontMetrics fm = g2.getFontMetrics();
                    int strWidth = fm.stringWidth(nodo.getNombre());
                    g2.drawString(nodo.getNombre(), x - strWidth / 2, y + 5);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MapaRuta app = new MapaRuta();
            app.setVisible(true);
        });
    }
}
