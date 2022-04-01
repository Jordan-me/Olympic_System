package effects;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;

public class OlympicRings {
	public static final List<Color> STANDARD_OLYMPIC_COLORS = Collections
			.unmodifiableList(Arrays.asList(Color.BLUE, Color.YELLOW, Color.BLACK, Color.GREEN, Color.RED));
	private static final double[] TOP_START_ANGLES = new double[] { 45, 225 };
	private static final double[] BOTTOM_START_ANGLES = new double[] { 315, 135 };

	private Pane pane;
	private double radius;
	private double strokeWidth;
	private double curx;
	private double cury;
	private double topy;
	private double bottomy;
	private double startAngleL;
	private double startAngleR;
	private int prevIndexL;
	private int prevIndexR;

	public OlympicRings(Pane pane, double radius, double strokeWidth) {
		this.pane = pane;
		this.radius = radius;
		this.strokeWidth = strokeWidth;
		topy = 2 * radius;
		bottomy = 3 * radius;
		curx = 2 * radius;
		cury = topy;
		startAngleL = TOP_START_ANGLES[0];
		startAngleR = TOP_START_ANGLES[1];
		prevIndexL = 0;
		prevIndexR = 0;
	}

	public void paintNextRing(Color color) {
		addArcL(color);
		addArcR(color);
		advance();
	}

	private void addArcL(Color color) {
		Arc arcL = makeArc(startAngleL, color);
		if (cury == topy) {
			pane.getChildren().add(prevIndexL, arcL);
		} else {
			pane.getChildren().add(prevIndexR, arcL);
			prevIndexL = prevIndexR;
		}
	}

	private void addArcR(Color color) {
		Arc arcR = makeArc(startAngleR, color);
		pane.getChildren().add(arcR);
		prevIndexR = pane.getChildren().size() - 1;
	}

	private Arc makeArc(double startAngle, Color color) {
		Arc arc = new Arc(curx, cury, radius, radius, startAngle, 180);
		arc.setFill(null);
		arc.setStroke(color);
		arc.setStrokeWidth(strokeWidth);
		return arc;
	}

	private void advance() {
		curx += radius + strokeWidth;
		if (cury == topy) {
			cury = bottomy;
			startAngleL = BOTTOM_START_ANGLES[0];
			startAngleR = BOTTOM_START_ANGLES[1];
		} else {
			cury = topy;
			startAngleL = TOP_START_ANGLES[0];
			startAngleR = TOP_START_ANGLES[1];
		}
	}
}
