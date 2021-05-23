package lab3.drawing;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class ZipperFly {
    private final Group root;

    /**
     * Constructor
     * @param root - JavaFX group object
     * */
    public ZipperFly(Group root) {
        this.root = root;
    }

    /** Drawing Zipper (Вжик) */
    public void draw() {
        Color bodyColor = Color.rgb(110, 181, 183);
        Color eyesColor = Color.rgb(255, 247, 26);
        Color shirtColor = Color.rgb(255, 54, 48);
        Color mouthColor = Color.rgb(107, 4, 16);
        Color tongueColor = Color.rgb(232, 164, 166);
        Color linesColor = Color.BLACK;
        Color blinkColor = Color.WHITE;

        // Drawing BODY --------------------------------------------------------
        Path body = new Path(
                new MoveTo(120, 217),
                new QuadCurveTo(119, 230, 112, 247),
                new QuadCurveTo(130, 260, 105, 250),
                new QuadCurveTo(107, 242, 105, 234),
                new QuadCurveTo(100, 236, 95, 234),
                new QuadCurveTo(93, 242, 95, 250),
                new QuadCurveTo(70, 260, 88, 247),
                new QuadCurveTo(81, 230, 80, 217)
        );
        Path leftHand = new Path(
                new MoveTo(122, 192),
                new CubicCurveTo(133, 210, 132, 222, 125, 228),
                new QuadCurveTo(123, 226, 127, 220),
                new CubicCurveTo(125, 222, 121, 221, 127, 215),
                new LineTo(118, 200)
        );
        Path rightHand = new Path(
                new MoveTo(78, 192),
                new CubicCurveTo(67, 210, 68, 222, 75, 228),
                new QuadCurveTo(77, 226, 73, 220),
                new CubicCurveTo(75, 222, 79, 221, 73, 215),
                new LineTo(82, 200)
        );

        Shape[] bodyParts = { body, leftHand, rightHand };
        for (Shape shape : bodyParts) {
            addObject(shape, bodyColor, linesColor);
        }
        // ---------------------------------------------------------------------

        // Drawing shirt -------------------------------------------------------
        Path shirtFront = new Path(
                new MoveTo(115, 180),
                new QuadCurveTo(123, 206, 120, 217),
                new QuadCurveTo(100, 225, 80, 217),
                new QuadCurveTo(77, 206, 85, 180)
        );
        Path collar = new Path(
                new MoveTo(116, 178),
                new LineTo(116, 184),
                new QuadCurveTo(100, 198, 85, 184),
                new LineTo(85,178)
        );
        Polyline rightSleeve = new Polyline(85, 184, 74, 194, 79, 196);
        Polyline leftSleeve = new Polyline(115, 184, 126, 194, 121, 196);

        Shape[] shirt = { shirtFront, collar, rightSleeve, leftSleeve };
        for (Shape shape : shirt) {
            addObject(shape, shirtColor, linesColor);
        }
        // ---------------------------------------------------------------------

        // Drawing Face --------------------------------------------------------
        Path head = new Path(
                new MoveTo(100, 100),
                new CubicCurveTo(115, 93, 128, 100, 142, 144),
                new CubicCurveTo(147, 150, 140, 162, 121, 178),
                new QuadCurveTo(100, 187, 82, 178),
                new CubicCurveTo(60, 162, 53, 150, 58, 145),
                new CubicCurveTo(78, 100, 85, 93, 100, 100)
        );
        Path smile = new Path(
                new MoveTo(140, 152),
                new CubicCurveTo(136, 152, 132, 154, 120, 165),
                new QuadCurveTo(100, 176, 80, 165),
                new CubicCurveTo(68, 154, 64, 152, 60, 152)
        );
        Path mouth = new Path(
                new MoveTo(120, 165),
                new QuadCurveTo(100, 190, 80, 165)
        );
        Path tongue = new Path(
                new MoveTo(108, 175),
                new QuadCurveTo(105, 170, 102, 172),
                new QuadCurveTo(100, 174, 98, 172),
                new QuadCurveTo(95, 170, 92, 175),
                new QuadCurveTo(100, 178, 108, 175)
        );
        addObject(head, bodyColor, linesColor);
        addObject(mouth, mouthColor, linesColor);
        addObject(tongue, tongueColor, linesColor);
        addObject(smile, bodyColor, linesColor);

        Path eyes = new Path(
                new MoveTo(100, 100),
                new CubicCurveTo(115, 93, 128, 100, 134, 130),
                new CubicCurveTo(136, 148, 130, 155, 100, 160),
                new CubicCurveTo(70, 155, 64, 148, 66, 130),
                new CubicCurveTo(78, 100, 85, 93, 100, 100)
        );
        addObject(eyes, eyesColor, linesColor);

        Ellipse pupil1 = new Ellipse(108, 150, 2, 3);
        Ellipse pupil2 = new Ellipse(92, 150, 2, 3);
        Shape[] pupils = { pupil1, pupil2 };
        for (Shape pupil : pupils) {
            addObject(pupil, linesColor, linesColor);
        }

        QuadCurve eyelash1 = new QuadCurve(115, 97, 116, 87, 118, 87);
        QuadCurve eyelash2 = new QuadCurve(116, 99, 119, 89, 128, 88);
        QuadCurve eyelash3 = new QuadCurve(88, 97, 88, 87, 82, 87);
        QuadCurve eyelash4 = new QuadCurve(85, 99, 82, 89, 75, 88);
        Shape[] eyelashes = { eyelash1, eyelash2, eyelash3, eyelash4 };
        for (Shape eyelash : eyelashes) {
            addObject(eyelash, Color.TRANSPARENT, linesColor);
        }

        Ellipse nose = new Ellipse(100, 158, 10, 8);
        Ellipse noseBlink = new Ellipse(100, 156, 4, 2);
        addObject(nose, linesColor, linesColor);
        addObject(noseBlink, blinkColor, linesColor);
    }

    private void addObject(Shape obj, Color fill, Color stroke) {
        obj.setFill(fill);
        obj.setStroke(stroke);
        this.root.getChildren().add(obj);
    }
}
