package lab2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main extends JPanel implements ActionListener{
    public static final String TITLE = "Lab 2";
    public static final String SIGNATURE = "Lab 2. Dzherhalova Renata, KP-82";
    public static final int WIDTH = 1200, HEIGHT = 1000, PADDING = 20;
    public static final int SCENE_WIDTH = 650, SCENE_HEIGHT = 400;
    public static final int TV_WIDTH = 300, TV_HEIGHT = 200;
    public static final int SCREEN_WIDTH = 200, SCREEN_HEIGHT = 165;
    public static final int BUTTON_RADIUS = 7, BUTTON_NUM = 3;

    Timer timer;
    private static int maxWidth;
    private static int maxHeight;

    // Transparency animation
    private float alpha = 1;
    private float delta = 0.01f;

    // Movement animation
    private double tx = 1;
    private double ty = 0;

    // Points
    private static final double[][] points = {
            { SCENE_WIDTH / 2. - TV_WIDTH / 2., SCENE_HEIGHT / 2. - TV_HEIGHT /2. },
            { SCENE_WIDTH / 2. + TV_WIDTH / 2., SCENE_HEIGHT / 2. - TV_HEIGHT /2. },
            { SCENE_WIDTH / 2. + TV_WIDTH / 2., SCENE_HEIGHT / 2. + TV_HEIGHT /2. },
            { SCENE_WIDTH / 2. - TV_WIDTH / 2., SCENE_HEIGHT / 2. + TV_HEIGHT /2. }
    };

    public Main() {
        timer = new Timer(1, this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHints(getRenderingHints());

        g2d.drawString(SIGNATURE, PADDING, maxHeight);
        BasicStroke border = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2d.setStroke(border);
        g2d.drawRect(PADDING, PADDING, maxWidth - PADDING * 2, maxHeight - PADDING * 2);

        g2d.setStroke(new BasicStroke());
        g2d.translate(maxWidth / 2 - SCENE_WIDTH / 2, maxHeight / 2 - SCENE_HEIGHT / 2);


        g2d.translate(tx, ty);
        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(comp);
        drawImage(g2d);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.add(new Main());
        frame.setVisible(true);

        Dimension size = frame.getSize();
        Insets insets = frame.getInsets();
        maxWidth = size.width - insets.left - insets.right - 1;
        maxHeight = size.height - insets.top - insets.bottom - 1;
    }

    private RenderingHints getRenderingHints() {
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return rh;
    }

    private void drawImage(Graphics2D g2d) {
        g2d.setBackground(new Color(127, 255, 0));
        g2d.clearRect(0, 0, SCENE_WIDTH, SCENE_HEIGHT);

        // Drawing tv with points
        g2d.setColor(new Color(255, 165, 0));
        GeneralPath tv = new GeneralPath();
        tv.moveTo(points[0][0], points[0][1]);
        for (double[] point : points) {
            tv.lineTo(point[0], point[1]);
        }
        tv.closePath();
        g2d.fill(tv);

        // Drawing screen with gradient
        var gradient = new GradientPaint(0, 0, new Color(128, 128, 128), 20, 0, new Color(20,
                20, 20), true);
        g2d.setPaint(gradient);
        g2d.fillRoundRect((int)(SCENE_WIDTH / 2. - TV_WIDTH / 2. + 20),
                (int)(SCENE_HEIGHT / 2. - SCREEN_HEIGHT / 2.),
                SCREEN_WIDTH, SCREEN_HEIGHT, 20, 30);


        // Drawing buttons
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < BUTTON_NUM; i++) {
            g2d.fillOval((int)(SCENE_WIDTH / 2. + TV_WIDTH / 2. - 30),
                    (int)(SCENE_HEIGHT /2. + ((i + 1) * BUTTON_RADIUS) + (i * 25)),
                    BUTTON_RADIUS * 2, BUTTON_RADIUS * 2);
        }

        // Drawing antennas
        int antennasStartX = (int)(SCENE_WIDTH / 2.);
        int antennasStartY = (int)(SCENE_HEIGHT / 2. - TV_HEIGHT / 2.);
        g2d.drawLine(antennasStartX, antennasStartY, antennasStartX - 50, antennasStartY - 50);
        g2d.drawLine(antennasStartX, antennasStartY, antennasStartX + 50, antennasStartY - 50);
    }

    public void actionPerformed(ActionEvent e) {
        // Changing transparency
        if (alpha < 0.01f || alpha > 1 - 0.01f) {
            delta = -delta;
        }
        alpha += delta;

        // Circle movements
        double canvasR = Math.pow(SCENE_HEIGHT / 2., 2);
        double dx = 0.5;
        if (tx <= 0 && ty < 0) {
            tx -= dx;
            ty = -Math.sqrt(canvasR - Math.pow(tx, 2));
        } else if (tx > 0 && ty <= 0) {
            tx -= dx;
            ty = -Math.sqrt(canvasR - Math.pow(tx, 2));
        } else if (tx >= 0 && ty > 0) {
            tx += dx;
            ty = Math.sqrt(canvasR - Math.pow(tx, 2));
        } else if (tx < 0 && ty >= 0) {
            tx += dx;
            ty = Math.sqrt(canvasR - Math.pow(tx, 2));
        }

        repaint();
    }
}
