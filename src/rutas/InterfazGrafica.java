package rutas;

import javax.swing.*;
import java.awt.*;

public class InterfazGrafica extends JPanel {
    
    public InterfazGrafica() {
        setPreferredSize(new Dimension(800, 600));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujar nodos
        drawNode(g, "I", 100, 100);
        drawNode(g, "K", 150, 200);
        drawNode(g, "L", 100, 50);
        drawNode(g, "H", 200, 100);
        drawNode(g, "E", 300, 150);
        drawNode(g, "D", 400, 200);
        drawNode(g, "J", 500, 250);
        drawNode(g, "A", 600, 300);
        drawNode(g, "B", 650, 350);
        drawNode(g, "C", 700, 400);
        drawNode(g, "G", 350, 100);
        drawNode(g, "F", 250, 50);
        drawNode(g, "ZV", 450, 50);
        
        // Dibujar conexiones
        drawLine(g, 100, 100, 150, 200);
        drawLine(g, 100, 50, 100, 100);
        drawLine(g, 150, 200, 300, 150);
        drawLine(g, 200, 100, 300, 150);
        drawLine(g, 300, 150, 400, 200);
        drawLine(g, 400, 200, 500, 250);
        drawLine(g, 500, 250, 600, 300);
        drawLine(g, 600, 300, 650, 350);
        drawLine(g, 650, 350, 700, 400);
        drawLine(g, 350, 100, 300, 150);
        drawLine(g, 250, 50, 300, 150);
        drawLine(g, 450, 50, 500, 250);
    }
    
    private void drawNode(Graphics g, String label, int x, int y) {
        g.setColor(Color.WHITE);
        g.fillOval(x - 15, y - 15, 30, 30);
        g.setColor(Color.BLACK);
        g.drawOval(x - 15, y - 15, 30, 30);
        g.drawString(label, x - 5, y + 5);
    }
    
    private void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        g.setColor(Color.BLACK);
        g.drawLine(x1, y1, x2, y2);
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Diagrama de Nodos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GraphInterface());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static class GraphInterface extends PopupMenu {

        public GraphInterface() {
        }
    }
}
