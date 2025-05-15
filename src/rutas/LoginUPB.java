package rutas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import javax.imageio.ImageIO;

public class LoginUPB extends JFrame {

    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private JButton loginButton;
    private BufferedImage fondo;
    private ImageIcon logoUPB;

    public LoginUPB() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar Look and Feel Nimbus: " + e.getMessage());
        }

        setTitle("Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        try {
            fondo = ImageIO.read(new File("/Users/andresnunez/Downloads/116870048_3121971067887241_4623098511793942255_n.jpg"));
            ImageIcon icono = new ImageIcon("/Users/andresnunez/Downloads/Fondo de “escudo-3” eliminado.png");
            Image img = icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            logoUPB = new ImageIcon(img);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen de fondo o logo: " + e.getMessage());
            e.printStackTrace();
        }

        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fondo != null) {
                    g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(0, 0, new Color(13, 71, 161), 0, getHeight(), new Color(204, 0, 0));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        fondoPanel.setLayout(new GridBagLayout());
        setContentPane(fondoPanel);

        JPanel panelLogin = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                int arc = 15;
                int offsetY = 20;

                g2.setColor(new Color(0, 0, 0, 120));
                g2.fillRoundRect(10, 10 + offsetY, getWidth() - 20, getHeight() - 20 - offsetY, arc, arc);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, offsetY, getWidth() - 20, getHeight() - 20 - offsetY, arc, arc);

                g2.setColor(new Color(13, 71, 161));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(0, offsetY, getWidth() - 20, getHeight() - 20 - offsetY, arc, arc);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        panelLogin.setOpaque(false);
        panelLogin.setLayout(new GridBagLayout());
        panelLogin.setPreferredSize(new Dimension(520, 480));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 25, 15, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel tituloPanel = new JPanel(new BorderLayout());
        tituloPanel.setOpaque(false);
        tituloPanel.setPreferredSize(new Dimension(490, 80));

        JPanel textoLogoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        textoLogoPanel.setOpaque(false);

        JLabel logoLabel = new JLabel();
        if (logoUPB != null) logoLabel.setIcon(logoUPB);

        JLabel titulo = new JLabel("Acceder a su cuenta");
        titulo.setFont(new Font("Montserrat", Font.BOLD, 24));
        titulo.setForeground(new Color(13, 71, 161));

        textoLogoPanel.add(logoLabel);
        textoLogoPanel.add(titulo);
        tituloPanel.add(textoLogoPanel, BorderLayout.CENTER);

        JPanel franjaRoja = new JPanel();
        franjaRoja.setBackground(new Color(204, 0, 0));
        franjaRoja.setPreferredSize(new Dimension(9999, 6));
        tituloPanel.add(franjaRoja, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panelLogin.add(tituloPanel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel usuarioLabel = new JLabel("Nombre de usuario:");
        usuarioLabel.setForeground(Color.BLACK);
        usuarioLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelLogin.add(usuarioLabel, gbc);

        usuarioField = new JTextField(15);
        usuarioField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usuarioField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(13, 71, 161), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelLogin.add(usuarioField, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaLabel.setForeground(Color.BLACK);
        contrasenaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelLogin.add(contrasenaLabel, gbc);

        contrasenaField = new JPasswordField(15);
        contrasenaField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contrasenaField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(13, 71, 161), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelLogin.add(contrasenaField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel bienvenida = new JLabel("Bienvenido a la UPB");
        bienvenida.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        bienvenida.setForeground(new Color(13, 71, 161));
        bienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        panelLogin.add(bienvenida, gbc);

        JPanel botonYTextoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        botonYTextoPanel.setOpaque(false);

        loginButton = new JButton("Acceder") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(51, 102, 204));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                int y = (getHeight() - textHeight) / 2 + fm.getAscent() - 2;
                g2.drawString(getText(), (getWidth() - textWidth) / 2, y);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {}
        };

        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(180, 45));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel textoAlLado = new JLabel("Ingresar al sistema institucional");
        textoAlLado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textoAlLado.setForeground(Color.DARK_GRAY);

        botonYTextoPanel.add(loginButton);
        botonYTextoPanel.add(textoAlLado);

        gbc.insets = new Insets(5, 25, 15, 25);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panelLogin.add(botonYTextoPanel, gbc);

        gbc.insets = new Insets(15, 25, 15, 25);

        JPanel linksPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        linksPanel.setOpaque(false);
        linksPanel.setPreferredSize(new Dimension(490, 40));

        JPanel olvidoPanel = new JPanel(new BorderLayout());
        olvidoPanel.setOpaque(false);
        JLabel olvido = new JLabel("<html><u><font color='#CC0000'>¿Olvidé mi contraseña?</font></u></html>");
        olvido.setCursor(new Cursor(Cursor.HAND_CURSOR));
        olvido.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        olvidoPanel.add(olvido, BorderLayout.CENTER);
        JPanel franjaOlvido = new JPanel();
        franjaOlvido.setBackground(new Color(204, 0, 0));
        franjaOlvido.setPreferredSize(new Dimension(0, 3));
        olvidoPanel.add(franjaOlvido, BorderLayout.SOUTH);

        JPanel cambiarPanel = new JPanel(new BorderLayout());
        cambiarPanel.setOpaque(false);
        JLabel cambiar = new JLabel("<html><u><font color='#CC0000'>Cambiar Contraseña</font></u></html>");
        cambiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cambiar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cambiarPanel.add(cambiar, BorderLayout.CENTER);
        JPanel franjaCambiar = new JPanel();
        franjaCambiar.setBackground(new Color(204, 0, 0));
        franjaCambiar.setPreferredSize(new Dimension(0, 3));
        cambiarPanel.add(franjaCambiar, BorderLayout.SOUTH);

        linksPanel.add(olvidoPanel);
        linksPanel.add(cambiarPanel);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelLogin.add(linksPanel, gbc);

        getContentPane().add(panelLogin);

        loginButton.addActionListener(e -> autenticar());
        usuarioField.addActionListener(e -> loginButton.doClick());
        contrasenaField.addActionListener(e -> loginButton.doClick());

        olvido.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginUPB.this,
                        "Funcionalidad de recuperación de contraseña aún no implementada.",
                        "¿Olvidé mi contraseña?", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        cambiar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginUPB.this,
                        "Funcionalidad para cambiar contraseña aún no implementada.",
                        "Cambiar Contraseña", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void autenticar() {
        String usuario = usuarioField.getText();
        char[] contraChars = contrasenaField.getPassword();
        String contra = new String(contraChars);

        if (usuario.equals("admin") && contra.equals("1234")) {
            dispose();
            new rutas.MapaRuta().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        java.util.Arrays.fill(contraChars, '0');
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginUPB login = new LoginUPB();
            login.setVisible(true);
        });
    }
}
