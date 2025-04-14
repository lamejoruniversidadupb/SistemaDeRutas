package rutas;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InterfazGrafica extends JFrame {
    private final JComboBox<String> inicioCombo;
    private final JComboBox<String> destinoCombo;
    private final JTextArea resultadoArea;
    private final Grafos grafo;
    private List<String> rutaActual;
    private GrafoPanel panelGrafo;

    public InterfazGrafica() {
        setTitle("Sistema de Rutas UPB");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setUndecorated(true); 

        grafo = new Grafos();
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout());

        inicioCombo = new JComboBox<>();
        destinoCombo = new JComboBox<>();
        for (String nodo : grafo.getNombresNodos()) {
            inicioCombo.addItem(nodo);
            destinoCombo.addItem(nodo);
        }

        JButton calcularBtn = new JButton("Calcular Ruta");
        calcularBtn.addActionListener(new CalcularRutaListener());

        panelSuperior.add(new JLabel("Inicio:"));
        panelSuperior.add(inicioCombo);
        panelSuperior.add(new JLabel("Destino:"));
        panelSuperior.add(destinoCombo);
        panelSuperior.add(calcularBtn);

        container.add(panelSuperior, BorderLayout.NORTH);

        panelGrafo = new GrafoPanel();
        container.add(panelGrafo, BorderLayout.CENTER);

        resultadoArea = new JTextArea(3, 20);
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        container.add(scrollPane, BorderLayout.SOUTH);
    }

    private class CalcularRutaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String inicio = (String) inicioCombo.getSelectedItem();
            String destino = (String) destinoCombo.getSelectedItem();

            if (inicio != null && destino != null) {
                rutaActual = grafo.dijkstra(inicio, destino);
                if (rutaActual.isEmpty()) {
                    resultadoArea.setText("No se encontró ruta entre " + inicio + " y " + destino);
                } else {
                    resultadoArea.setText("Ruta más corta: \n" + String.join(" -> ", rutaActual));
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

            Dimension size = getSize(); 
            double scaleX = size.width / 600.0; 
            double scaleY = size.height / 500.0; 

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            for (Arista arista : grafo.getAristas()) {
                Nodo n1 = grafo.getNodo(arista.getOrigen());
                Nodo n2 = grafo.getNodo(arista.getDestino());
                if (n1 != null && n2 != null)
                    g2.drawLine((int)(n1.getX() * scaleX), (int)(n1.getY() * scaleY), (int)(n2.getX() * scaleX), (int)(n2.getY() * scaleY));
            }

            // Verifica si hay una ruta y dibuja la línea roja
            if (rutaActual != null && rutaActual.size() > 1) {
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(3));
                for (int i = 0; i < rutaActual.size() - 1; i++) {
                    Nodo n1 = grafo.getNodo(rutaActual.get(i));
                    Nodo n2 = grafo.getNodo(rutaActual.get(i + 1));
                    if (n1 != null && n2 != null)
                        g2.drawLine((int)(n1.getX() * scaleX), (int)(n1.getY() * scaleY), (int)(n2.getX() * scaleX), (int)(n2.getY() * scaleY));
                }
            }

            // Dibujar los nodos (circulares y rectangulares para ciertos nodos)
            for (Nodo nodo : grafo.getNodos()) {
                if (nodo.getNombre().equals("CAFETERIA") || nodo.getNombre().equals("PORTERIA")) {
                    g2.setColor(Color.GRAY);
                    g2.fillRect((int)(nodo.getX() * scaleX) - 30, (int)(nodo.getY() * scaleY) - 20, 60, 40);
                    g2.setColor(Color.BLACK);
                    g2.drawRect((int)(nodo.getX() * scaleX) - 30, (int)(nodo.getY() * scaleY) - 20, 60, 40);
                    g2.drawString(nodo.getNombre(), (int)(nodo.getX() * scaleX) - 20, (int)(nodo.getY() * scaleY) + 5);
                } else {
                    g2.setColor(Color.GRAY);
                    g2.fillOval((int)(nodo.getX() * scaleX) - 15, (int)(nodo.getY() * scaleY) - 15, 30, 30);
                    g2.setColor(Color.BLACK);
                    g2.drawOval((int)(nodo.getX() * scaleX) - 15, (int)(nodo.getY() * scaleY) - 15, 30, 30);
                    g2.drawString(nodo.getNombre(), (int)(nodo.getX() * scaleX) - 10, (int)(nodo.getY() * scaleY) + 5);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazGrafica app = new InterfazGrafica();
            app.setVisible(true);
        });
    }
}
