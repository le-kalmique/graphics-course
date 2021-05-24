package lab6;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import lab6.models.HelicopterModel;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Scene extends JFrame implements ActionListener {
    private final String BACK_PATH = "C:\\Users\\RenataD\\Documents\\КПИшка\\Graphics\\graphics" +
            "-course\\lab6\\src\\resources\\sky.jpg";

    private final String TITLE = "Lab 6. Helicopter";
    private final int WIDTH = 1024, HEIGHT = 680;

    private final SimpleUniverse universe;
    private final HelicopterModel hm;
    private final Canvas3D canvas;
    private Color color;

    public Scene() {
        setUpWindow();

        canvas = createCanvas();
        getContentPane().add("Center", canvas);
        setVisible(true);

        universe = createUniverse();
        setUpUniverse(universe);

        var root = new BranchGroup();
        var light = createLight();
        var background = createBackground();
        root.addChild(light);
        root.addChild(background);

        color = new Color(120, 0, 38);

        hm = new HelicopterModel(universe);
        hm.createInstance(color);


        universe.addBranchGraph(root);
    }

    public static void main(String[] args) {
        Scene s = new Scene();
        s.setVisible(true);
    }

    private void setUpWindow() {
        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private Canvas3D createCanvas() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        return new Canvas3D(config);
    }

    private SimpleUniverse createUniverse() {
        SimpleUniverse su = new SimpleUniverse(canvas);
        su.getViewingPlatform().setNominalViewingTransform();
        return su;
    }

    private void setUpUniverse(SimpleUniverse su) {
        canvas.repaint();
        OrbitBehavior ob = new OrbitBehavior(canvas);
        BoundingSphere bs = new BoundingSphere(new Point3d(0, 0, 10), Double.MAX_VALUE);
        ob.setSchedulingBounds(bs);

        su.getViewingPlatform().setViewPlatformBehavior(ob);
    }

    private DirectionalLight createLight() {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Color3f color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f dir = new Vector3f(-1.0f, 0.0f, -0.5f);
        DirectionalLight light = new DirectionalLight(color, dir);
        light.setInfluencingBounds(bounds);
        return light;
    }

    private Background createBackground() {
        TextureLoader tl = new TextureLoader(BACK_PATH, canvas);
        Background background = new Background(tl.getImage());
        background.setImageScaleMode(Background.SCALE_FIT_MAX);
        background.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), Double.MAX_VALUE));
        background.setCapability(Background.ALLOW_IMAGE_WRITE);
        return background;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}