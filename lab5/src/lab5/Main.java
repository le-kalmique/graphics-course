package lab5;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class Main extends JFrame implements ActionListener, KeyListener {
    private static final String TITLE = "Lab 5";
    private static final int WIDTH = 800, HEIGHT = 600;
    private final static String modelLoc = "C:\\Users\\RenataD\\Documents\\КПИшка\\Graphics" +
            "\\graphics-course\\lab5\\src\\resources\\chair.obj";
    private final static String backgroundLoc = "C:\\Users\\RenataD\\Documents\\КПИшка\\Graphics" +
            "\\graphics-course\\lab5\\src\\resources\\myRoom.jpg";
    private final static String clothTexture = "C:\\Users\\RenataD\\Documents\\КПИшка\\Graphics" +
            "\\graphics-course\\lab5\\src\\resources\\cloth.jpg";
    private final static String woodTexture = "C:\\Users\\RenataD\\Documents\\КПИшка\\Graphics" +
            "\\graphics-course\\lab5\\src\\resources\\wood.jpg";

    private final Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
    private final BranchGroup root = new BranchGroup();
    private final TransformGroup objGroup = new TransformGroup();

    private final Transform3D transform3D = new Transform3D();
    private final Transform3D rotateTransformX = new Transform3D();
    private final Transform3D rotateTransformY = new Transform3D();
    private final Transform3D rotateTransformZ = new Transform3D();
    private float xPos = 0;
    private float yPos = 0;
    private float zPos = 0;

    private SimpleUniverse universe;
    private Scene obj;
    private Map<String, Shape3D> nameMap;

    public Main() {
        try {
            init();
            setUpScene();
            setUpObject();

            root.compile();
            universe.addBranchGraph(root);
        } catch (IOException err) {
            System.err.println(err.getMessage());
        }
    }

    public static void main(String[] args) {
        Main win = new Main();
        win.addKeyListener(win);
        win.setVisible(true);
    }

    private void init() throws IOException {
        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas.setDoubleBufferEnable(true);
        getContentPane().add(canvas, BorderLayout.CENTER);

        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();

        canvas.addKeyListener(this);
        obj = getSceneFromObj();
    }

    private void setUpScene() throws IOException {
        TextureLoader tl = getTexture(backgroundLoc);
        Background background = new Background(tl.getImage());
        background.setImageScaleMode(Background.SCALE_FIT_MAX);
        background.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), 9999));
        background.setCapability(Background.ALLOW_IMAGE_WRITE);
        root.addChild(background);

        nameMap = obj.getNamedObjects();
        nameMap.forEach((k, v) -> System.out.println("k" + k + " v" + v));
        objGroup.addChild(obj.getSceneGroup());
        objGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(objGroup);

        DirectionalLight dirLight = new DirectionalLight(
                new Color3f(Color.YELLOW),
                new Vector3f(8.0f, -5.0f, -10.0f)
        );
        dirLight.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 9999));
        root.addChild(dirLight);

        AmbientLight ambientLight = new AmbientLight(new Color3f(Color.BLACK));
        DirectionalLight directionalLight = new DirectionalLight(new Color3f(Color.BLACK),
                new Vector3f(-1, -1, -1));
        BoundingSphere region = new BoundingSphere(new Point3d(0, 0, 0), 9999);
        ambientLight.setInfluencingBounds(region);
        directionalLight.setInfluencingBounds(region);
        root.addChild(ambientLight);
        root.addChild(directionalLight);
    }

    private void updatePosition() {
        transform3D.setTranslation(new Vector3f(xPos, yPos, zPos));
        objGroup.setTransform(transform3D);
    }


    private Material getMaterial() {
        var material = new Material();
        material.setAmbientColor(new Color3f(new Color(189, 174, 149)));
        material.setDiffuseColor(new Color3f(new Color(224, 215, 209, 72)));
        material.setSpecularColor(new Color3f(new Color(208, 180, 176)));
        material.setShininess(10);
        material.setLightingEnable(true);
        return material;
    }

    private void setUpObject() {
        transform3D.setTranslation(new Vector3f(0, 0, 0));
        var scaleT = new Transform3D();
        scaleT.setScale(0.4f);
        var rotT = new Transform3D();
        rotT.rotY(20);
        transform3D.mul(scaleT, rotT);
        objGroup.setTransform(transform3D);

        // Chair appearance
        var chairAppearance = new Appearance();
        var texture = getTexture(clothTexture).getTexture();
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(1.0f, 1.0f, 1.0f, 1.0f));
        var material = getMaterial();
        chairAppearance.setTexture(texture);
        chairAppearance.setMaterial(material);
        var objChair = nameMap.get("box001");
        objChair.setAppearance(chairAppearance);

        // Legs appearance
        var legsAppearance = new Appearance();
        texture = getTexture(woodTexture).getTexture();
        material = getMaterial();
        var color = new ColoringAttributes(new Color3f(new Color(76, 45, 19)), 0);
        legsAppearance.setTexture(texture);
        legsAppearance.setMaterial(material);
        legsAppearance.setColoringAttributes(color);
        var objLegs = nameMap.get("cylinder004");
        objLegs.setAppearance(legsAppearance);
    }

    private Scene getSceneFromObj() {
        int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
        ObjectFile f = new ObjectFile(flags);
        Scene s = null;
        try {
            s = f.load(modelLoc);
        } catch (FileNotFoundException | ParsingErrorException | IncorrectFormatException e) {
            System.err.println(e);
            System.exit(1);
        }
        return s;
    }

    private TextureLoader getTexture(String filepath) {
        return new TextureLoader(filepath, new Container());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        float delta = 0.05f;

        switch (key) {
            case KeyEvent.VK_LEFT: {
                xPos -= delta;
                updatePosition();
                break;
            }
            case KeyEvent.VK_RIGHT: {
                xPos += delta;
                updatePosition();
                break;
            }
            case KeyEvent.VK_UP: {
                yPos += delta;
                updatePosition();
                break;
            }
            case KeyEvent.VK_DOWN: {
                yPos -= delta;
                updatePosition();
                break;
            }
            case KeyEvent.VK_EQUALS: {
                zPos += delta;
                updatePosition();
                break;
            }
            case KeyEvent.VK_MINUS: {
                zPos -= delta;
                updatePosition();
                break;
            }
            case KeyEvent.VK_E: {
                rotateTransformX.rotX(delta);
                transform3D.mul(rotateTransformX);
                objGroup.setTransform(transform3D);
                break;
            }
            case KeyEvent.VK_Q: {
                rotateTransformX.rotX(-delta);
                transform3D.mul(rotateTransformX);
                objGroup.setTransform(transform3D);
                break;
            }
            case KeyEvent.VK_D: {
                rotateTransformY.rotY(delta);
                transform3D.mul(rotateTransformY);
                objGroup.setTransform(transform3D);
                break;
            }
            case KeyEvent.VK_A: {
                rotateTransformY.rotY(-delta);
                transform3D.mul(rotateTransformY);
                objGroup.setTransform(transform3D);
                break;
            }
            case KeyEvent.VK_S: {
                rotateTransformZ.rotZ(delta);
                transform3D.mul(rotateTransformZ);
                objGroup.setTransform(transform3D);
                break;
            }
            case KeyEvent.VK_W: {
                rotateTransformZ.rotZ(-delta);
                transform3D.mul(rotateTransformZ);
                objGroup.setTransform(transform3D);
                break;
            }
            case KeyEvent.VK_BACK_SPACE: {
                setUpObject();
                rotateTransformZ.setIdentity();
                rotateTransformY.setIdentity();
                rotateTransformX.setIdentity();
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
