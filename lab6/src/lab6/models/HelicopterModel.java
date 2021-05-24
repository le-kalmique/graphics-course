package lab6.models;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Map;

public class HelicopterModel {
    private final String MODEL_PATH = "C:\\Users\\RenataD\\Documents\\КПИшка\\Graphics\\graphics" +
            "-course\\lab6\\src\\resources\\helicopter.obj";

    TransformGroup helicopterGroup;
    private BranchGroup root = new BranchGroup();
    private BranchGroup scene = new BranchGroup();
    private final SimpleUniverse su;
    private Scene helicopter;
    HelicopterParts parts;
    private Color mainColor = new Color(38, 56, 38);


    public HelicopterModel(SimpleUniverse su) {
        this.su = su;
        root.setCapability(BranchGroup.ALLOW_DETACH);
    }

    public void createInstance(Color color) {
        if (color != null) {
            mainColor = color;
        }
        load();
        create();
    }

    private void load() {
        int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
        ObjectFile f = new ObjectFile(flags);
        Scene s = null;
        try {
            s = f.load(MODEL_PATH);
        } catch (FileNotFoundException | ParsingErrorException | IncorrectFormatException e) {
            System.err.println(e);
            System.exit(1);
        }
        helicopter = s;
    }

    public void restart() {
        root.removeChild(scene);
    }

    public void create() {
        scene = new BranchGroup();
        float scaleVal = 0.3f;
        Transform3D scale = new Transform3D();
        scale.setScale(scaleVal);
        Transform3D helicopterTransform = new Transform3D();
        helicopterTransform.rotY(Math.PI);

        helicopterTransform.mul(scale);

        helicopterGroup = new TransformGroup(helicopterTransform);
        TransformGroup sceneGroup = new TransformGroup();

        BranchGroup helicopterSceneGroup = helicopter.getSceneGroup();
        helicopterSceneGroup.removeChild((Shape3D) helicopter.getNamedObjects().get("big_propeller"));
        helicopterSceneGroup.removeChild((Shape3D) helicopter.getNamedObjects().get("small_propeller"));

        sceneGroup.addChild(helicopter.getSceneGroup());
        sceneGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        helicopterGroup.addChild(sceneGroup);
        scene.addChild(helicopterGroup);

        parts = new HelicopterParts(helicopter.getNamedObjects());
        parts.style(mainColor);

        Transform3D transformForBigPropeller = new Transform3D();
        transformForBigPropeller.setTranslation(new Vector3f(0, 0, -0.2f));

        helicopterSceneGroup.addChild(addRotationNode(
                (Shape3D) helicopter.getNamedObjects().get("big_propeller"),
                transformForBigPropeller,
                900
        ));

        Transform3D transformForSmallPropeller = new Transform3D();
        transformForSmallPropeller.rotZ(Math.PI / 2);
        transformForSmallPropeller.setTranslation(new Vector3f(0, 0.06f, 0.8f));

        helicopterSceneGroup.addChild(addRotationNode(
                (Shape3D) helicopter.getNamedObjects().get("small_propeller"),
                transformForSmallPropeller,
                1000
        ));


        Transform3D transformMove = new Transform3D();
        transformMove.rotY(Math.PI / 2);

        Alpha crawlAlpha = new Alpha(
                1, Alpha.INCREASING_ENABLE, 0, 0, 9000, 0, 0, 0, 0, 0
        );
        PositionInterpolator interpolator = new PositionInterpolator(
                crawlAlpha, sceneGroup, transformMove, -15.0f, 8.5f
        );
        sceneGroup.setCapability(
                TransformGroup.ALLOW_TRANSFORM_WRITE);
        RotationInterpolator positionInterpolator =
                new RotationInterpolator(crawlAlpha,sceneGroup);
        positionInterpolator.setSchedulingBounds(new BoundingSphere(
                new Point3d(0.0,0.0,0.0),1.0));
        BoundingSphere bs = new BoundingSphere(new Point3d(0, 0, -1200), Double.MAX_VALUE);
        interpolator.setSchedulingBounds(bs);

        // TO CHANGE ANIMATION

        sceneGroup.addChild(positionInterpolator);
//        sceneGroup.addChild(interpolator);

        scene.compile();
        root.addChild(scene);
        su.addBranchGraph(root);
    }

    private void setAppearance(Color color, Shape3D shape) {
        Appearance app = new Appearance();
        var material = new Material();
        material.setAmbientColor(new Color3f(color.brighter().brighter().brighter().brighter()));
        material.setDiffuseColor(new Color3f(color.brighter()));
        material.setSpecularColor(new Color3f(color));
        material.setShininess(70);
        material.setLightingEnable(true);
        app.setMaterial(material);
        shape.setAppearance(app);
    }

    private Node addRotationNode(Shape3D shape, Transform3D transform, int rotateDuration) {
        TransformGroup transformGroup = new TransformGroup();
        transformGroup.addChild(shape.cloneTree());

        Alpha alpha = new Alpha(Integer.MAX_VALUE, Alpha.INCREASING_ENABLE, 0, 0, rotateDuration,
                0, 0, 0, 0, 0);
        RotationInterpolator rotationInterpolator = new RotationInterpolator(alpha, transformGroup,
                transform, (float) Math.PI * 2, 0.0f);

        BoundingSphere bound = new BoundingSphere(new Point3d(), Double.MAX_VALUE);
        rotationInterpolator.setSchedulingBounds(bound);

        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.addChild(rotationInterpolator);

        return transformGroup;
    }

    private class HelicopterParts {
        private final Shape3D body;
        private final Shape3D parts;
        private final Shape3D otherParts;
        private final Shape3D decal;
        private final Shape3D glass;
        private final Shape3D smallPropeller;
        private final Shape3D bigPropeller;
        private final Shape3D missile;
        private final Shape3D missileHeading;

        public HelicopterParts(Map<String, Shape3D> modelNameMap) {
            body = modelNameMap.get("main_body_");
            decal = modelNameMap.get("decal");
            glass = modelNameMap.get("glass");
            smallPropeller = modelNameMap.get("small_propeller");
            bigPropeller = modelNameMap.get("big_propeller");
            parts = modelNameMap.get("main_");
            otherParts = modelNameMap.get("alpha");
            missileHeading = modelNameMap.get("missile_gl");
            missile = modelNameMap.get("missile_1");
        }

        public void style(Color mainColor) {
            setAppearance(mainColor.darker().darker(), body);
            setAppearance(mainColor, decal);
            setAppearance(Color.LIGHT_GRAY, glass);
            setAppearance(Color.BLACK, smallPropeller);
            setAppearance(Color.BLACK, bigPropeller);
            setAppearance(mainColor.darker().darker(), parts);
            setAppearance(mainColor, otherParts);
            setAppearance(Color.BLACK, missileHeading);
            setAppearance(mainColor, missile);
        }
    }
}
