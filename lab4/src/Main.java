import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends Applet implements ActionListener {
    private final Button btnX = new Button("x");
    private final Button btnY = new Button("y");
    private final TransformGroup cubeTransform = new TransformGroup();
    private final Transform3D cubeTransform3dX = new Transform3D();
    private final Transform3D cubeTransform3dY = new Transform3D();
    private boolean rotateY = false;
    private boolean rotateX = false;
    private double angleY = 0;
    private double angleX = 0;

    public Main() {
        Timer timer = new Timer(50, this);
        timer.start();

        setLayout(new BorderLayout());
        GraphicsConfiguration cfg = SimpleUniverse.getPreferredConfiguration();
        Canvas3D c = new Canvas3D(cfg);
        add("Center", c);

        Panel p = new Panel();
        p.add(btnX);
        p.add(btnY);
        add("South", p);
        btnX.addActionListener(this);
        btnY.addActionListener(this);

        SimpleUniverse u = new SimpleUniverse(c);
        BranchGroup branchGroup = createScene();
        u.getViewingPlatform().setNominalViewingTransform();
        u.addBranchGraph(branchGroup);
    }

    public static void main(String[] args) {
        int WIDTH = 600, HEIGHT = 600;
        var obj = new Main();
        MainFrame mf = new MainFrame(obj, WIDTH, HEIGHT);
        mf.run();
    }

    private BranchGroup createScene() {
        BranchGroup root = new BranchGroup();
        Background bg = new Background();

        TextureLoader loader = new TextureLoader("C:\\Users\\RenataD\\Documents\\КПИшка\\Graphics\\graphics-course\\lab4\\assets\\childrens_room.jpg", "LUMiNANCE",
                new Container());
        ImageComponent2D texture = loader.getImage();

        bg.setImage(texture);
        bg.setImageScaleMode(Background.SCALE_FIT_MAX);
        bg.setCapability(Background.ALLOW_IMAGE_WRITE);
        BoundingSphere sphere = new BoundingSphere(new Point3d(0, 0, 0), 9999);
        bg.setApplicationBounds(sphere);
        root.addChild(bg);

        cubeTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);


        Color3f light1Color = new Color3f(1f, 1f, 1f);
        BoundingSphere bounds =
                new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Vector3f light1Direction =
                new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 =
                new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        root.addChild(light1);

        buildRubicsCube();
        root.addChild(cubeTransform);

        return root;
    }

    private void buildRubicsCube() {
        BranchGroup rubicsCube = new BranchGroup();

        float step = 1 / 3f;
        float width = 1 / 6.5f;
        float start = -1 / 2f + width;

        int dimension = 3;
        for (int z = 0; z < dimension; z++) {
            for (int y = 0; y < dimension; y++) {
                for (int x = 0; x < dimension; x++) {
                    TransformGroup cube = new TransformGroup();
                    Transform3D transform = new Transform3D();

                    transform.setTranslation(new Vector3f(start + step * x, start + step * y,
                            start + step * z));
                    cube.setTransform(transform);

                    cube.addChild(buildShape(width));
                    rubicsCube.addChild(cube);
                }
            }
        }

        Appearance insideAp = new Appearance();
        insideAp.setColoringAttributes(new ColoringAttributes(new Color3f(Color.DARK_GRAY),
                ColoringAttributes.SHADE_FLAT));
        cubeTransform.addChild(new Box(0.4f, 0.4f, 0.4f, Box.GENERATE_TEXTURE_COORDS, insideAp));
        cubeTransform.addChild(rubicsCube);
    }

    private Node buildShape(float width) {
        BranchGroup group = new BranchGroup();

        Box box = new Box(width, width, width, Box.GENERATE_TEXTURE_COORDS, null);
        group.addChild(box);

        Color3f emissive = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f ambient = new Color3f(0.9f, .15f, .15f);
        Color3f diffuse = new Color3f(0.7f, .15f, .15f);
        Color3f specular = new Color3f(0.2f, 0.2f, 0.2f);

        int[] sides = {Box.BACK, Box.TOP, Box.BOTTOM, Box.LEFT, Box.RIGHT, Box.FRONT};
        Color[] colors = {new Color(255, 140, 0), Color.WHITE, Color.YELLOW, Color.GREEN,
                Color.BLUE,
                Color.RED};

        for (int i = 0; i < sides.length; i++) {
            Appearance appearance = new Appearance();
            appearance.setColoringAttributes(new ColoringAttributes(new Color3f(colors[i]),
                    ColoringAttributes.SHADE_FLAT));
            appearance.setMaterial(new Material(ambient, emissive, diffuse, specular, 1.0f));
            box.setAppearance(sides[i], appearance);
        }

        return group;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnX) {
            rotateX = !rotateX;
        } else if (e.getSource() == btnY) {
            rotateY = !rotateY;
        } else {
            double angleDelta = 0.05;
            if (rotateX && rotateY) {
                cubeTransform3dY.rotX(angleY);
                angleY += angleDelta;

                cubeTransform3dX.rotY(angleX);
                angleX += angleDelta;

                cubeTransform3dY.mul(cubeTransform3dX);
                cubeTransform.setTransform(cubeTransform3dY);
            } else if (rotateY) {
                cubeTransform3dY.rotX(angleY);
                angleY += angleDelta;

                cubeTransform.setTransform(cubeTransform3dY);
            } else if (rotateX) {
                cubeTransform3dX.rotY(angleX);
                angleX += angleDelta;

                cubeTransform.setTransform(cubeTransform3dX);
            }
        }
    }
}
